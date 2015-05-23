package com.botacachinggame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.db.object.Niveau;
import com.util.BotaButton;
import com.util.BotaTextView;
import com.util.Constant;
import com.util.SizeCalculator;

public class MainMenu extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	/**
		 * La structure de cette activity est:
		 * LinearLayout(verticale)(variable: mainLayout)
		 * 		TextView(Botacaching)
		 * 		Bouton jouer (playButton)
		 * 		Bouton Fiche informatives (infoButton)
		 * 		Bouton profil du joueur (profilButton)
		 * 		Bouton Options (optButton)
		 **/
        
        loadMainMenu();
        
    }
    
	private void loadMainMenu() {
		
        int margin = (int)SizeCalculator.getYSizeFor(this, Constant.margin_between_menu_elements);
        
        /**************************** Creation du mainLayout  *****************************/
        
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);

		/********************* Creation et ajout du titre au mainLayout ********************/
		
		createAndAddTitle(mainLayout);
		
		/*************************** Parametre pour les boutons *****************************/
		
		int width = (int)SizeCalculator.getXSizeFor(this, Constant.mainMenuButton_width);
		int height = (int)SizeCalculator.getYSizeFor(this, Constant.mainMenuButton_height);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
		
		params.setMargins(0, margin, 0, margin);
		params.gravity = Gravity.CENTER;
		
        /*************************** Création du bouton jouer *****************************/
		
        BotaButton playButton = createPlayButton();
        mainLayout.addView(playButton,params);
        
        /********************** Création du bouton Fiche informative **********************/
        
        BotaButton infoButton = createInfoButton();
        mainLayout.addView(infoButton,params);
        
        /************************** Création du bouton profil ****************************/
        
        BotaButton profilButton = createProfilButton();
        mainLayout.addView(profilButton,params);
        
        /************************** Création du bouton options ****************************/
        /*
        BotaButton optButton = createOptionButton();
        mainLayout.addView(optButton,params);
        */
        /********************************** Ajout du mainLayout a l'actvity **********************************/
        
		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
											   LinearLayout.LayoutParams.MATCH_PARENT);
        
        this.addContentView(mainLayout, params);
		
	}

	private void createAndAddTitle(LinearLayout mainLayout) {
		
		/*************************** Parametre pour le titre *****************************/
		
		LinearLayout.LayoutParams  params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				   														  LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		
		int titleMarginBottom = (int)SizeCalculator.getYSizeFor(this, Constant.mainMenuTitleMarginBottom);
		int titleMarginTop = (int)SizeCalculator.getYSizeFor(this, Constant.mainMenuTitleMarginTop);
		
		params.setMargins(0, titleMarginTop, 0, titleMarginBottom);
		
		/*************************** Creation du titre *****************************/
		
		BotaTextView title = new BotaTextView(this);
		title.setText(Constant.mainMenuTitle);
		float textSize = SizeCalculator.getTextSizeFor(this, Constant.mainMenuTitleSize);
		
		title.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		mainLayout.addView(title, params);
	}

	private BotaButton createPlayButton() {
    	
        BotaButton playButton = new BotaButton(this);
        playButton.setText(Constant.playButtonText);
        
        float textSize = SizeCalculator.getTextSizeFor(this, Constant.mainMenuTextButtonSize);
        playButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        
        playButton.setOnClickListener(onClickPlay());
        
        return playButton;
	}

    private OnClickListener onClickPlay() {
    	
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
		       final LocationManager locationManager = (LocationManager)MainMenu.this.getSystemService(Context.LOCATION_SERVICE);

		       boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		       
		        if (isGPSEnabled) {

					//Intent intent = new Intent(MainMenu.this, NotionMenu.class);
		        	Intent intent = new Intent(MainMenu.this, MiseAJourBD.class);
					
					startActivity(intent);
			    	onDestroy();
			    	finish();
		        } 
		        else {
		        	// Provider not enabled, prompt user to enable it
		        	AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
					builder.setMessage("Veuillez activer votre géolocalisation pour pouvoir jouer");
					
					builder.setPositiveButton("Activer", new DialogInterface.OnClickListener() {
					               public void onClick(DialogInterface dialog, int id) {
					            	   Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					            	   MainMenu.this.startActivity(myIntent);
					               }
					           });
					
					builder.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int id) {
			            	   dialog.cancel();
			               }
			           });
					
					builder.show();
		        }
			}
		};
	}

	private BotaButton createInfoButton() {
    	
  	  	BotaButton infoButton = new BotaButton(this);
        infoButton.setText(Constant.infoButtonText);

        float textSize = SizeCalculator.getTextSizeFor(this, Constant.mainMenuTextButtonSize);
        infoButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        infoButton.setOnClickListener(onClickInformationSheet());
        
        return infoButton;
	}

	private OnClickListener onClickInformationSheet() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {

	        	Intent intent = new Intent(MainMenu.this, InformationNotionMenu.class);
				startActivity(intent);
		    	onDestroy();
		    	finish();
				
			}
		};
	}

	private BotaButton createProfilButton() {
		
		BotaButton profilButton = new BotaButton(this);
        profilButton.setText(Constant.profilButtonText);

        float textSize = SizeCalculator.getTextSizeFor(this, Constant.mainMenuTextButtonSize);
        profilButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        
        profilButton.setOnClickListener(onClickProfil());
        
        return profilButton;
	}
    
	private OnClickListener onClickProfil() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainMenu.this, Profil.class);
		    	startActivity(intent);
		    	onDestroy();
		    	finish();
			
			}
		};
	}

	/*private BotaButton createOptionButton() {
		
		BotaButton optButton = new BotaButton(this);
        optButton.setText(Constant.optButtonText);

        float textSize = SizeCalculator.getTextSizeFor(this, Constant.mainMenuTextButtonSize);
        optButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        
        return optButton;
	}*/
    
	@Override
	protected boolean hasReturnButton() {
		return true;
	}

	@Override
	protected boolean hasHomeButton() {
		return false;
	}	
	
	@Override
	protected boolean hasExitButton() {
		return true;
	}
	
	@Override
	protected Niveau getCurrentLevel() {
		return null;
	}
	
	@Override
	protected void onBackKeyPressed() {
    	exit();
	}
	

}
