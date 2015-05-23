package com.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindow implements InfoWindowAdapter
{
	private Context context;
	private GoogleMap map;
	
	private static Marker mark = null;
	private static boolean firstTime = true;
	
	public CustomInfoWindow(Context context, GoogleMap map)
	{
		this.context = context;
		this.map = map;
		mark = null;
		firstTime = true;
	}
	
	@Override
	public View getInfoWindow(Marker mark) 
	{
		return null;
	}
	
	@Override
	public View getInfoContents(Marker mark) 
	{        
		CustomInfoWindow.mark = mark;
		
		/*******Création de la route vers la Zone d'observation******/
		Location loc = map.getMyLocation();
        LatLng myPos = new LatLng(loc.getLatitude(), loc.getLongitude());
        LatLng destination = mark.getPosition();
        
        if(firstTime)
        {
        	RoadDrawer roadDrawer = new RoadDrawer(map, context, RoadDrawer.FRENCH);
        	roadDrawer.drawRoad(myPos, destination, false);
        	firstTime = false;
        }
		
		String[] path = mark.getTitle().split("/");
		String[] nameImg = path[path.length-1].split("\\.");
		String title = nameImg[0];
		
		LinearLayout horizontalLayout = new LinearLayout(this.context);
		horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		/**********Affichage d'un aperçu de la Zone d'observation*************/
        Bitmap originalBmp = BitmapFactory.decodeFile(mark.getTitle());
        ImageView image = new ImageView(this.context);
        double ratio = 200/(double)originalBmp.getWidth();
        image.setImageBitmap(Bitmap.createScaledBitmap(originalBmp, 200, (int)(originalBmp.getHeight()*ratio), false));
        horizontalLayout.addView(image);
		
		LinearLayout verticalLayout = new LinearLayout(this.context);
		verticalLayout.setOrientation(LinearLayout.VERTICAL);
		verticalLayout.setGravity(Gravity.CENTER_VERTICAL);
		
		TextView text = new TextView(this.context);
		text.setText(title);
		text.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		verticalLayout.addView(text);

		/**************Affichage de la distance**********************/
		int distance = RoadDrawer.getDistance();
		
		TextView distanceView = new TextView(this.context);
		String distanceText = "Distance : ";
		if(distance/1000 > 0)
			distanceText += distance/1000 + "km ";
		if (distance%1000 > 0)
			distanceText += distance%1000 + "m";
		if(distance == 0)
			distanceView.setText("Distance : Calcul en cours...");
		else
			distanceView.setText(distanceText);
		
		distanceView.setGravity(Gravity.CENTER_VERTICAL);
		distanceView.setPadding(5, distanceView.getPaddingTop(), distanceView.getPaddingRight(), 
									distanceView.getPaddingBottom());
		verticalLayout.addView(distanceView);
		
		/****************Affichage du temps estimé*****************/
		//Vitesse moyenne de marche d'un homme 1.4 m/s
		int tempsSeconde = (int)(distance/1.4);
		TextView timeView = new TextView(this.context);
		String timeText = "Temps estimé : ";
		
		if(tempsSeconde/3600 > 0)
		{
			timeText += tempsSeconde/3600 + "h ";
			tempsSeconde = tempsSeconde%3600;
		}
		
		if (tempsSeconde/60 > 0)
			timeText += tempsSeconde/60 + "min ";
		if (tempsSeconde%60 >= 0)
			timeText += tempsSeconde%60 + "s ";
		if(distance == 0)
			timeView.setText("Temps estimé : Calcul en cours...");
		else
			timeView.setText(timeText);
		
		timeView.setGravity(Gravity.CENTER_VERTICAL);
		timeView.setPadding(5, timeView.getPaddingTop(), timeView.getPaddingRight(), timeView.getPaddingBottom());
		
		verticalLayout.addView(timeView);
		horizontalLayout.addView(verticalLayout);
		
		return horizontalLayout;
	}
	
	public static void updateView()
	{
		CustomInfoWindow.mark.showInfoWindow();
		CustomInfoWindow.firstTime = true;
	}
	
	public static Marker getFocusedMark()
	{
		return CustomInfoWindow.mark;
	}
}
