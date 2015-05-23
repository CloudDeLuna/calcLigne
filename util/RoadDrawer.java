package com.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Html;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class RoadDrawer 
{
	 private GoogleMap map;
	 private Context context;
	 private String lang;
	 
	 public  static String ENGLISH = "en";
	 public  static String FRENCH = "fr";
	 
	 private static ArrayList<Polyline> polyLineList = new ArrayList<Polyline>();
	 private static String transportType = "walking";//"driving", ...
	 private static int distance;
	 private static boolean showInfoWindow;
	 
	 public RoadDrawer(GoogleMap map, Context context, String language)
	 {
		 this.map = map;
		 this.context = context;
		 this.lang = language;
	 }
	 
	 public boolean drawRoad(ArrayList<LatLng> points, boolean withIndications, boolean optimize)
	 {		 
		 this.resetMarkerList();
		 
		 if(points.size() == 2)
		 {
			 String url = makeURL(points.get(0).latitude,points.get(0).longitude,points.get(1).latitude,points.get(1).longitude);
			 new ConnectAsyncTask(url, withIndications).execute();
			 return true;
		 }
		 else if(points.size() > 2)
		 {
			 String url = makeURL(points, optimize);
			 new ConnectAsyncTask(url, withIndications).execute();
			 return true;
		 }
		 
		 return false;
	 }
	  
	 public void drawRoad(LatLng source, LatLng dest, boolean withIndications)
	 {
		 this.resetMarkerList();
		 
		 String url = makeURL(source.latitude,source.longitude,dest.latitude,dest.longitude);
		 new ConnectAsyncTask(url,withIndications).execute();
	 }
	 
	 private void resetMarkerList()
	 {
		 for(Polyline line :RoadDrawer.polyLineList)
			 line.remove();
		 
		 RoadDrawer.polyLineList = new ArrayList<Polyline>();
		 RoadDrawer.distance = 0;
	 }
	 
	 public static int getDistance()
	 {
		 return RoadDrawer.distance;
	 }
	 
	 public static void setShowInfoWindow(boolean bool)
	 {
		 RoadDrawer.showInfoWindow = bool;
	 }
	 
	 private String makeURL(ArrayList<LatLng> points, boolean optimize)
	 {
		StringBuilder urlString = new StringBuilder();
		
		String mode = transportType;
		
		urlString.append("http://maps.googleapis.com/maps/api/directions/json");
		urlString.append("?origin=");// from
		urlString.append( points.get(0).latitude);
		urlString.append(',');
		urlString.append(points.get(0).longitude);
		urlString.append("&destination=");
		urlString.append(points.get(points.size()-1).latitude);
		urlString.append(',');
		urlString.append(points.get(points.size()-1).longitude);
		
		urlString.append("&waypoints=");
		
		if(optimize)
			urlString.append("optimize:true|");
		
		urlString.append( points.get(1).latitude);
		urlString.append(',');
		urlString.append(points.get(1).longitude);
		    
		for(int i=2;i<points.size()-1;i++)
		{
			urlString.append('|');
			urlString.append( points.get(i).latitude);
		    urlString.append(',');
		    urlString.append(points.get(i).longitude);
		}	
		
		urlString.append("&sensor=true&mode="+mode);

		return urlString.toString();
	}
	
	 private String makeURL (double sourcelat, double sourcelog, double destlat, double destlog)
	{
		StringBuilder urlString = new StringBuilder();
		String mode = transportType;
		 
		urlString.append("http://maps.googleapis.com/maps/api/directions/json");
		urlString.append("?origin=");// from
		urlString.append(Double.toString(sourcelat));
		urlString.append(",");
		urlString.append(Double.toString( sourcelog));
		urlString.append("&destination=");// to
		urlString.append(Double.toString( destlat));
		urlString.append(",");
		urlString.append(Double.toString( destlog));
		urlString.append("&sensor=false&mode="+mode+"&alternatives=true&language="+lang);
		
		return urlString.toString();		
	}

	 private List<LatLng> decodePoly(String encoded) 
	{
		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;
		
		while (index < len) 
		{
		    int b, shift = 0, result = 0;
		    do 
		    {
		        b = encoded.charAt(index++) - 63;
		        result |= (b & 0x1f) << shift;
		        shift += 5;
		    } while (b >= 0x20);
		    
		    int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
		    lat += dlat;
		
		    shift = 0;
		    result = 0;
		    
		    do 
		    {
		        b = encoded.charAt(index++) - 63;
		        result |= (b & 0x1f) << shift;
		        shift += 5;
		    } while (b >= 0x20);
		    
		    int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
		    lng += dlng;
		
		    LatLng p = new LatLng( ((double) lat / 1E5), ((double) lng / 1E5) );
		    poly.add(p);
		}
		
		return poly;
	 }
	 
	 private void drawPath(String  result, boolean withSteps) 
	 {
		try 
		{
			//Tranform the string into a json object
			final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            for(int z = 0; z < list.size()-1; ++z)
            {
                LatLng src= list.get(z);
                LatLng dest= list.get(z+1);
                polyLineList.add(map.addPolyline(new PolylineOptions()
					                .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude, dest.longitude))
					                .width(10)
					                .color(Color.CYAN).geodesic(true)));
            }
           
			JSONArray arrayLegs = routes.getJSONArray("legs");
			JSONObject legs = arrayLegs.getJSONObject(0);
			JSONArray stepsArray = legs.getJSONArray("steps");
	
			if(withSteps)
		    {
				for(int i=0; i < stepsArray.length(); ++i)
				{
				    Step step = new Step(stepsArray.getJSONObject(i));
				    map.addMarker(new MarkerOptions().position(step.location)
				          		.title(step.distance)
				          		.snippet(step.instructions)
				          		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
				    
				    String dist [] = step.distance.split(" ");
				    RoadDrawer.distance += Integer.parseInt(dist[0]);
				}
            }
			else
			{
				for(int i=0; i < stepsArray.length(); ++i)
				{
				    Step step = new Step(stepsArray.getJSONObject(i));

				    String dist [] = step.distance.split(" ");
				    if(dist[1].equals("m"))
				    	RoadDrawer.distance += Integer.parseInt(dist[0]);
				    else//km
				    {
				    	String[] nbKm = dist[0].split(",");
				    	RoadDrawer.distance += Integer.parseInt(nbKm[0]) * 1000 + Integer.parseInt(nbKm[1]) * 100;
				    }
				}
			}
	    } 
	    catch (JSONException e) 
	    {	
	    }
	 }
	 
	 /*****debut connectAsyncTask******/
	 private class ConnectAsyncTask extends AsyncTask<Void, Void, String>
	 {
		private ProgressDialog progressDialog;
		private String url;
		private boolean steps;
		
		ConnectAsyncTask(String urlPass, boolean withSteps)
		{
		    url = urlPass;
		    steps = withSteps;    
		}
		
		@Override
		protected void onPreExecute() 
		{
		    super.onPreExecute();
		    progressDialog = new ProgressDialog(context);
		    progressDialog.setMessage("Calcul de l'itinéraire, veuillez patienter...");
		    progressDialog.setIndeterminate(true);
		    if(showInfoWindow)
		    	progressDialog.show();
		}
		
		@Override
		protected String doInBackground(Void... params) 
		{
		    JsonParser jParser = new JsonParser();
		    String json = jParser.getJSONFromUrl(url);
		    return json;
		}
		
		@Override
		protected void onPostExecute(String result) 
		{
		    super.onPostExecute(result);   
		    progressDialog.hide();        
		    
		    if(result!=null)
		    {
		    	drawPath(result,steps);
		    	CustomInfoWindow.updateView();
		    }
		}
	 }
	 /*****fin connectAsyncTask******/

	 /**
	  * Represent every step of the directions. It store distance, location and instructions
	  */
	private class Step
	{
		public String distance;
		public LatLng location;
		public String instructions;
		 
		Step(JSONObject stepJSON)
		{
			JSONObject startLocation;
			try 
			{	
				distance = stepJSON.getJSONObject("distance").getString("text");
				startLocation = stepJSON.getJSONObject("start_location");
				location = new LatLng(startLocation.getDouble("lat"),startLocation.getDouble("lng"));
				
				try 
				{
					instructions = URLDecoder.decode(Html.fromHtml(stepJSON.getString("html_instructions")).toString(), "UTF-8");
				} 
				catch (UnsupportedEncodingException e) 
				{
					e.printStackTrace();
				}				
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
		}		 
	}
}