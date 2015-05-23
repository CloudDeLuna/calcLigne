package com.botacachinggame;

import java.lang.reflect.Method;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.db.bdd.BddLiaison;
import com.db.bdd.BddQube;
import com.db.bdd.BddZoneObservation;
import com.db.object.Liaison;
import com.db.object.Niveau;
import com.db.object.Qube;
import com.db.object.ZoneObservation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.util.BotaButton;
import com.util.Constant;
import com.util.CustomInfoWindow;
import com.util.RoadDrawer;
import com.util.SizeCalculator;

public class GeolocalisationActivity  extends RootActivity
{
	private int levelID;
	private int notionID;
	private int qubeID;
	private int qubeNumber;
	private int nbOfFalseQuestion;
	private int nbOfRightQuestion;
	private int experienceEarned;
	private boolean nextLevelsUnlock;
	
	private boolean firstTime = true;
	private Location myLocation;
	private ArrayList<Marker> markerList;
	private ArrayList<Integer> zoneObsIds;
	private BotaButton passButton;
	private ProgressDialog progressDialog;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        final Intent intent = this.getIntent();
		this.levelID = intent.getIntExtra(Constant.levelID,-1);
		this.notionID = intent.getIntExtra(Constant.notionID,-1);
		this.qubeID = intent.getIntExtra(Constant.qubeID,-1);
		this.qubeNumber = intent.getIntExtra(Constant.qubeNumber,-1);
		this.nbOfFalseQuestion = intent.getIntExtra(Constant.nbOfFalseQuestion,-1);
		this.nbOfRightQuestion = intent.getIntExtra(Constant.nbOfRightQuestion,-1);
		this.experienceEarned = intent.getIntExtra(Constant.experienceEarned,-1);
		this.nextLevelsUnlock = intent.getBooleanExtra(Constant.nextLevelsUnlock,false);
        
		this.markerList = new ArrayList<Marker>();
		
		final BddQube bddQube = new BddQube(this);
		bddQube.open();
		
		final Qube currentQube = bddQube.getQubeWithId(this.qubeID);
		bddQube.close();
		
		//le qube courant est une fiche informative, pas besoin de géolocalisation
		if(currentQube.isFicheInfo())
			continuer();
		else
			loadGeolocalisation(savedInstanceState);
    }
	
	private void loadGeolocalisation(Bundle savedInstanceState) 
	{   
        setContentView(R.layout.geolocalisation_layout);

		if(!this.isNetworkEnabled())
			this.showNetworkDisabledDial();
		
		final LocationManager locationManager = (LocationManager)GeolocalisationActivity.this.getSystemService(Context.LOCATION_SERVICE);
		boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        
		if (!isGPSEnabled) 
			showGPSDisabledDial();
		
        /***************************** Map **********************************/
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
        LinearLayout layoutMap = (LinearLayout)findViewById(R.id.mapContainer);
        RelativeLayout.LayoutParams paramMapLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
        												 RelativeLayout.LayoutParams.MATCH_PARENT);
        paramMapLayout.height = metrics.heightPixels/3*2;
        paramMapLayout.width = metrics.widthPixels/8*7;
        layoutMap.setLayoutParams(paramMapLayout);
        layoutMap.setY(metrics.heightPixels/32);
        layoutMap.setX(metrics.widthPixels/16);
        
        final GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.clear();
		map.setMyLocationEnabled(true);
		map.setIndoorEnabled(true);
		map.setBuildingsEnabled(true);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		map.getUiSettings().setZoomControlsEnabled(true);
		map.getUiSettings().setCompassEnabled(true);
		map.getUiSettings().setMapToolbarEnabled(false);

		map.setOnMyLocationChangeListener(new OnMyLocationChangeListener() 
		{
			@Override
			public void onMyLocationChange(Location location) 
			{
				if(firstTime && map != null)
				{				
					LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
					map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
					firstTime = false;
			        passButton.setEnabled(true);
				    progressDialog.hide();        
				}
				
				//Actualiser la route uniquement si la nouvelle position de l'appareil est 
				//supérieur à l'ancienne d'au moins de 3m
				boolean actualise = true;
				if(GeolocalisationActivity.this.myLocation != null)
					actualise = GeolocalisationActivity.this.myLocation.distanceTo(location) >= 3;
					
				if(actualise)
				{
					Marker mark = CustomInfoWindow.getFocusedMark();
					if(mark != null && mark.isInfoWindowShown())
					{
						RoadDrawer.setShowInfoWindow(false);
						mark.showInfoWindow();
					}
					
					GeolocalisationActivity.this.myLocation = location;
				}
			}
		});
		
		map.setInfoWindowAdapter(new CustomInfoWindow(this, map));
	    
		map.setOnMarkerClickListener(new OnMarkerClickListener() 
	    {
			@Override
			public boolean onMarkerClick(Marker arg0) 
			{
				RoadDrawer.setShowInfoWindow(true);

				if(!GeolocalisationActivity.this.isNetworkEnabled())
					GeolocalisationActivity.this.showNetworkDisabledDial();
				
				
				return false;
			}
		});
	    
		progressDialog = new ProgressDialog(this);
	    progressDialog.setMessage("Récupération de votre position en cours, veuillez patienter ...");
	    progressDialog.setIndeterminate(true);
	    
	    if(this.isNetworkEnabled())
	    	progressDialog.show();
		
	    /**************************Chargement des Zones d'observations*****************/		
		BddLiaison bddLiaison = new BddLiaison(this);
		bddLiaison.open();		
		Liaison liaison = bddLiaison.getZoneObsWithQubeId(qubeID);
		
		if(liaison != null)
		{
			zoneObsIds = liaison.getQubeOuZoneObsList();

	    	BddZoneObservation bddZoneObs = new BddZoneObservation(this);
	        bddZoneObs.open();
	        ZoneObservation zone;
	        
	        for(int idZone : zoneObsIds)
	        {
	        	zone = bddZoneObs.getZoneObservationWithId(idZone);
	        	
		        LatLng pos = new LatLng(zone.getLatitude(), zone.getLongitude());         
		        this.markerList.add(map.addMarker(new MarkerOptions().position(pos)
		        								.title(zone.getPathToImg())));
		    }
	        
	        map.moveCamera(CameraUpdateFactory.newLatLngZoom(this.markerList.get(0).getPosition(), 4));
	        bddZoneObs.close();
	    }
        else
        {
            Toast.makeText(this, "Fail pas de Zones d'observations", Toast.LENGTH_LONG).show();
        	//Bug ceci n'est pas censée arriver, si une questions n'a pas de zone d'observations 
        	//ne pas l'afficher
        }

	    bddLiaison.close();
        
        /***************************** Bouton passer **********************************/
        int width  = (int)SizeCalculator.getXSizeFor(this, Constant.button_width);
		int height = (int)SizeCalculator.getYSizeFor(this, Constant.button_height);
		int margin = (int)SizeCalculator.getYSizeFor(this, Constant.margin_between_menu_elements);
		
		LinearLayout.LayoutParams paramButton = new LinearLayout.LayoutParams(width,height);
		
		paramButton.setMargins(0, margin*12, 0, 0);
		paramButton.gravity = Gravity.CENTER;
        
        passButton = new BotaButton(this);
        passButton.setText("Continuer");
        float textSize = SizeCalculator.getTextSizeFor(this, Constant.textButtonSize);
        passButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        passButton.setLayoutParams(paramButton);
        passButton.setY((int)(metrics.heightPixels/3*2 + metrics.heightPixels/16));
        passButton.setX(metrics.widthPixels/6);
        passButton.setEnabled(false);
        
        passButton.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0)
			{
				GeolocalisationActivity.this.continuer();
			}
		});
        
        RelativeLayout mainContainer = (RelativeLayout)findViewById(R.id.mainContainer);
        mainContainer.addView(passButton);
        
        LinearLayout.LayoutParams paramLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					  LinearLayout.LayoutParams.MATCH_PARENT);
        this.setContentView(new View(this));
        this.addContentView(mainContainer, paramLayout);
	} 
	
	private void continuer()
	{
		Marker marker = CustomInfoWindow.getFocusedMark();
		int index = 0;
		int idZone = -1;
		int distanceMin = 10;
		
		if(marker == null)
		{
			if(!markerList.isEmpty())
				Toast.makeText(this, "Veuillez séléctionner une zone d'observation", Toast.LENGTH_LONG).show();
			else//FIXME a enelever après que Bastien n'affichera plus les questions sans ZO
			{
				Intent intent = new Intent(GeolocalisationActivity.this, ZoneObservationActivity.class);
				intent.putExtra(Constant.notionID, this.notionID);
				intent.putExtra(Constant.levelID, this.levelID);
				intent.putExtra(Constant.qubeID, this.qubeID);
				intent.putExtra(Constant.zoneObsID, -1);
				intent.putExtra(Constant.qubeID, this.qubeID);
				intent.putExtra(Constant.qubeNumber, this.qubeNumber);
				intent.putExtra(Constant.nbOfFalseQuestion, this.nbOfFalseQuestion);
				intent.putExtra(Constant.nbOfRightQuestion, this.nbOfRightQuestion);
				intent.putExtra(Constant.experienceEarned, this.experienceEarned);
				intent.putExtra(Constant.nextLevelsUnlock, this.nextLevelsUnlock);
				
		    	startActivity(intent);
		    	onDestroy();
		    	finish();
			}
		}
		else
		{
			index = markerList.indexOf(marker);
			idZone = zoneObsIds.get(index);
			
			float[] results = new float[1];
			Location.distanceBetween(myLocation.getLatitude(), myLocation.getLongitude(),
			                marker.getPosition().latitude, marker.getPosition().longitude, results);
			
			if(results[0] <= distanceMin)
			{
				Intent intent = new Intent(GeolocalisationActivity.this, ZoneObservationActivity.class);
				intent.putExtra(Constant.notionID, this.notionID);
				intent.putExtra(Constant.levelID, this.levelID);
				intent.putExtra(Constant.qubeID, this.qubeID);
				intent.putExtra(Constant.zoneObsID, idZone);
				intent.putExtra(Constant.qubeID, this.qubeID);
				intent.putExtra(Constant.qubeNumber, this.qubeNumber);
				intent.putExtra(Constant.nbOfFalseQuestion, this.nbOfFalseQuestion);
				intent.putExtra(Constant.nbOfRightQuestion, this.nbOfRightQuestion);
				intent.putExtra(Constant.experienceEarned, this.experienceEarned);
				intent.putExtra(Constant.nextLevelsUnlock, this.nextLevelsUnlock);
				
		    	startActivity(intent);
		    	onDestroy();
		    	finish();
			}
			else
				Toast.makeText(this, "Rapproché vous de la zone à observer", Toast.LENGTH_LONG).show();	
		}
	}
	
	private boolean isNetworkEnabled()
	{
		boolean mobileDataEnabled = false;
        ConnectivityManager connectManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        
        try 
        {
            Class<?> connectManagerClass = Class.forName(connectManager.getClass().getName());
            Method method = connectManagerClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true);// Make the method callable
            mobileDataEnabled = (Boolean)method.invoke(connectManager);
        } 
        catch (Exception e) 
        {
        }
        
		if(!mobileDataEnabled && !wifi.isWifiEnabled())
			return false;
		else
			return true;
	}
	
	private void showNetworkDisabledDial()
	{
		AlertDialog.Builder dial = new AlertDialog.Builder(this);
    	dial.setMessage("Veuillez activer le Wifi ou les données mobile");
		
    	dial.setPositiveButton("Activer", new DialogInterface.OnClickListener() 
		{
		   public void onClick(DialogInterface dialog, int id) 
		   {
			   Intent myIntent = new Intent(Settings.ACTION_SETTINGS);
			   GeolocalisationActivity.this.startActivity(myIntent);
		   }
		});
		
    	dial.setNegativeButton("Retour", new DialogInterface.OnClickListener() 
		{
           public void onClick(DialogInterface dialog, int id) 
           {
        	   dialog.dismiss();
           }
        });
		
    	dial.show();
	}
	
	private void showGPSDisabledDial() 
	{
		AlertDialog.Builder dial = new AlertDialog.Builder(this);
    	dial.setMessage("Veuillez activer votre géolocalisation pour pouvoir prendre une photo");
		
    	dial.setPositiveButton("Activer", new DialogInterface.OnClickListener() 
		{
		   public void onClick(DialogInterface dialog, int id) 
		   {
			   Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			   GeolocalisationActivity.this.startActivity(myIntent);
		   }
		});
		
    	dial.setNegativeButton("Retour", new DialogInterface.OnClickListener() 
		{
           public void onClick(DialogInterface dialog, int id) 
           {
        	   dialog.dismiss();
           }
        });
		
    	dial.show();
	} 
	
	@Override
	protected boolean hasReturnButton() 
	{	
		this.setReturnWarning("Voulez vous retourner au menu des notions ?");	
		return true;
	}

	@Override
	protected boolean hasHomeButton() 
	{
		this.setHomeWarning(true);
		return true;
	}	
	
	@Override
	protected boolean hasExitButton() 
	{
		return true;
	}
	
	@Override
	protected Niveau getCurrentLevel() 
	{	
		return null;
	}
	
	@Override
	protected void onBackKeyPressed() 
	{
		Intent intent;
		if(this.nbOfFalseQuestion != 0 || this.nbOfRightQuestion != 0){
			
			intent = new Intent(GeolocalisationActivity.this, ResultsActivity.class);
			intent.putExtra(Constant.levelID, this.levelID);
			intent.putExtra(Constant.notionID, this.notionID);
			intent.putExtra(Constant.nbOfFalseQuestion, this.nbOfFalseQuestion);
			intent.putExtra(Constant.nbOfRightQuestion, this.nbOfRightQuestion);
			intent.putExtra(Constant.experienceEarned, this.experienceEarned);
			intent.putExtra(Constant.nextLevelsUnlock, this.nextLevelsUnlock);
		}
		else{
			
			intent = new Intent(GeolocalisationActivity.this, LevelSelectionMenu.class);
		}
		
		startActivity(intent);
    	onDestroy();
    	finish();
	}
}
