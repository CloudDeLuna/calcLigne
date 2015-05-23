package com.example.util;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

public class GPSTracker extends Service implements LocationListener 
{ 
    private final Context mContext;
    private boolean isGPSEnabled 	= false;
    private boolean isNetworkEnabled= false;
    private boolean canGetLocalisation 	= false;
    private Location localisation;
    private double 	latitude;
    private double 	longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5; // 5 metres
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1; // 30 secondes
    private LocationManager localisationManager;
 
    public GPSTracker(Context context) 
    {
        this.mContext = context;
        getLocation();
    }
    
    public Location getLocation() 
    {
	    try 
	    {
            localisationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = localisationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = localisationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            if (!isGPSEnabled && !isNetworkEnabled) 
            {
                Toast.makeText(mContext, "GPS indisponible", Toast.LENGTH_LONG).show();
                //Pas de connexion internet et le gps n'est pas activé
            	return null;
            } 
           
            this.canGetLocalisation = true;
            
            //D'abord on essaye d'avoir la position avec internet(3g ou wifi)
            if (isNetworkEnabled) 
            {
                localisationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
							                            MIN_TIME_BW_UPDATES,
							                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                if (localisationManager != null) 
                {
                    localisation = localisationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (localisation != null) 
                    {
                        latitude = localisation.getLatitude();
                        longitude = localisation.getLongitude();
                    }
                    else
                    {
                        //Toast.makeText(mContext, "NOOOOO", Toast.LENGTH_LONG).show();
                    }
                }
            }
            
            //Si on y arrive pas on essaye avec le GPS
            if (localisation == null && isGPSEnabled) 
            {
                localisationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
						                                MIN_TIME_BW_UPDATES,
						                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (localisationManager != null) 
                {
                    localisation = localisationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (localisation != null) 
                    {
                        latitude = localisation.getLatitude();
                        longitude = localisation.getLongitude();
                    }
                }
            }
        } 
    	catch (Exception e) 
    	{
            e.printStackTrace();
        }
 
        return localisation;
    }
    
    /***********Appeler cette fonction quand on n'utilise plus le GPS************/
    public void stopLoctaionUpdate()
    {
        if(localisationManager != null)
            localisationManager.removeUpdates(GPSTracker.this);
    }

    public double getLatitude()
    {
        if(localisation != null)
            latitude = localisation.getLatitude();
        return latitude;
    }
    
    public double getLongitude()
    {
        if(localisation != null)
            longitude = localisation.getLongitude();
        return longitude;
    }
    
    public boolean canGetLocalisation()
    {
    	return canGetLocalisation;
    }
    
	public static void showGPSDisabledDial(final Context context) 
	{
		AlertDialog.Builder dial = new AlertDialog.Builder(context);
    	dial.setMessage("Veuillez activer votre géolocalisation pour pouvoir prendre une photo");
		
    	dial.setPositiveButton("Activer", new DialogInterface.OnClickListener() 
		{
		   public void onClick(DialogInterface dialog, int id) 
		   {
			   Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			   context.startActivity(myIntent);
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
	public void onLocationChanged(Location localisation) 
	{
		this.localisation = localisation; 	
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) 
	{
		// TODO Auto-generated method stub	
	}

	@Override
	public void onProviderEnabled(String provider) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderDisabled(String provider) 
	{
		// TODO Auto-generated method stub	
	}

	@Override
	public IBinder onBind(Intent intent) 
	{
		// TODO Auto-generated method stub
		return null;
	}   
}