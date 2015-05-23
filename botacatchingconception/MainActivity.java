package com.example.botacatchingconception;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.util.BotaButton;
import com.example.util.Draw;
import com.example.util.GPSTracker;
import com.example.util.SizeCalculator;


public class MainActivity extends RootActivity 
{
	private DisplayMetrics 	metrics;
	private RelativeLayout RLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
        RLayout = new RelativeLayout(this);
    	/**** TITLE ****/
    	TextView title = new TextView(this); 
    	title.setText(R.string.main_title);
    	float titleSize = SizeCalculator.getTextSizeFor(this, 0.2f);
    	title.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
    	title.setTextColor(Color.parseColor("#3B170B"));
    	RelativeLayout.LayoutParams RLPTitle = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	RLPTitle.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    	RLPTitle.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
    	title.setLayoutParams(RLPTitle);
    	RLPTitle.setMargins(0, metrics.heightPixels/10, 0, 0);
    	
    	
    	TextView subtitle = new TextView(this); 
    	subtitle.setText(R.string.main_subtitle);
    	float subtitleSize = SizeCalculator.getTextSizeFor(this, 0.05f);
    	subtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, subtitleSize);
    	subtitle.setTextColor(Color.parseColor("#61380B"));
    	RelativeLayout.LayoutParams RLPSubtitle = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	RLPSubtitle.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    	RLPSubtitle.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
    	subtitle.setLayoutParams(RLPSubtitle);
    	RLPSubtitle.setMargins(0, metrics.heightPixels/3, 0, 0);
    	
    	RLayout.addView(subtitle);
    	RLayout.addView(title);

    	float buttonSize = SizeCalculator.getTextSizeFor(this, 0.07f);
    	
        BotaButton buttonParcours = new BotaButton (this);
        buttonParcours.setText(R.string.button_parcours);
        buttonParcours.setTextSize(TypedValue.COMPLEX_UNIT_SP, buttonSize);
        buttonParcours.setTextColor(Color.parseColor("#FFFFFF"));
        buttonParcours.setMaxWidth(metrics.widthPixels / 3);
        Draw.setBackground(buttonParcours, getResources().getDrawable(R.drawable.backgroundgreenbigbutton));
        
        RelativeLayout.LayoutParams RLPParcours = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        RLPParcours.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        RLPParcours.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        RLPParcours.setMargins(metrics.widthPixels/10, 0, 0, metrics.heightPixels/4);
        buttonParcours.setLayoutParams(RLPParcours);
    	
        
        buttonParcours.setOnClickListener(new OnClickListener() 
        {	
	        @Override
	        public void onClick(View v) 
	        {
	        	Intent intent = new Intent(MainActivity.this, ParcoursEditLayout.class);
	        	startActivity(intent);
				finish();
	      	}
        });
        
        BotaButton buttonZoneObs = new BotaButton (this);
        buttonZoneObs.setText(R.string.button_zone_observation);
        buttonZoneObs.setTextSize(TypedValue.COMPLEX_UNIT_SP, buttonSize);
        buttonZoneObs.setTextColor(Color.parseColor("#FFFFFF"));
        buttonZoneObs.setMaxWidth(metrics.widthPixels / 3);
        Draw.setBackground(buttonZoneObs, getResources().getDrawable(R.drawable.backgroundgreenbigbutton));
        
        RelativeLayout.LayoutParams RLPZO = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        RLPZO.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        RLPZO.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        RLPZO.setMargins(0, 0, metrics.widthPixels/10, metrics.heightPixels/4);
        buttonZoneObs.setLayoutParams(RLPZO);
        
        buttonZoneObs.setOnClickListener(new OnClickListener() 
        {	
			@Override
			public void onClick(View v) 
			{	
				final LocationManager locationManager = (LocationManager)MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
				boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	            
				if (isGPSEnabled || isNetworkEnabled) 
				{
					Intent intent = new Intent(MainActivity.this, ZoneObservationLayout.class);
					startActivity(intent);
					finish();
				} 
				else 
				{
					GPSTracker.showGPSDisabledDial(MainActivity.this);
				}
			}
		});
        
        loadReturnButton();
        
        RLayout.addView(buttonParcours);
        RLayout.addView(buttonZoneObs);
        
        setContentView(RLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        int id = item.getItemId();
        if (id == R.id.action_settings) 
        {
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) 
    {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
    
    private void loadReturnButton()
    {
    	BotaButton returnButton = new BotaButton(this);
    	float textSize = SizeCalculator.getTextSizeFor(this, 0.025f);
    	returnButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
     	returnButton.setText("Quitter");
    	
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
    	params.setMargins(50, 50, 0, 0);

    	returnButton.setLayoutParams(params);
    	Draw.setBackground(returnButton, getResources().getDrawable(R.drawable.backgroundbuttonexit));
    	RLayout.addView(returnButton);
    	
    	returnButton.setOnClickListener(new OnClickListener() 
    	{	
	        @Override
	        public void onClick(View v) 
	        {
	        	MainActivity.this.onKeyDown(KeyEvent.KEYCODE_BACK, null);
	      	}
    	});	  	
    }
    
    protected void onFinish()
    {
		AlertDialog.Builder dial = new AlertDialog.Builder(MainActivity.this);
    	dial.setMessage("Voulez vous vraiment quitter l'application ?");
		
    	dial.setPositiveButton("Oui", new DialogInterface.OnClickListener() 
		{
		   public void onClick(DialogInterface dialog, int id) 
		   {
			   MainActivity.this.finish();
			   android.os.Process.killProcess(android.os.Process.myPid());
		   }
		});
		
    	dial.setNegativeButton("Non", new DialogInterface.OnClickListener() 
		{
           public void onClick(DialogInterface dialog, int id) 
           {
        	   dialog.dismiss();
           }
        });
    	
    	dial.show();
    }
}
