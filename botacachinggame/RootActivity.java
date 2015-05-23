package com.botacachinggame;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.db.bdd.BddQube;
import com.db.object.Niveau;
import com.db.object.Qube;
import com.util.BotaProgressBar;
import com.util.Constant;
import com.util.SizeCalculator;

public abstract class RootActivity extends Activity{
	
    private static ArrayList<Activity> activities=new ArrayList<Activity>();
    private boolean homeWarning = false;
    private String returnWarning = "";
    public static boolean majBD = true;
    
    @Override
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        activities.add(this);
    }

    protected abstract boolean hasReturnButton();
    protected abstract boolean hasExitButton();
    protected abstract boolean hasHomeButton();
    protected abstract void onBackKeyPressed();

    protected void setHomeWarning(boolean bool){
    	this.homeWarning = bool;
    }
    
    protected void setReturnWarning(String warning){
    	this.returnWarning = warning;
    }
    
    protected abstract Niveau getCurrentLevel();
    
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

        	if(RootActivity.this.returnWarning.length() > 0)
				RootActivity.this.returnWithWarning();
			else
				RootActivity.this.onBackKeyPressed();
        	
        	return true;
        }

        return super.onKeyDown(keyCode, event);
    }

	@Override
	public void addContentView(View view, LayoutParams params) {
		
		if(! this.hasHomeButton() && !this.hasReturnButton() && !this.hasExitButton()){
			view.setBackgroundColor(Color.parseColor("#E3D29D"));
			super.addContentView(view, params);
		}
		else{
			LinearLayout mainLayout = new LinearLayout(this);
			mainLayout.setOrientation(LinearLayout.VERTICAL);
			
			addButtonToTheView(mainLayout, view, params);
			
			params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					   							   LinearLayout.LayoutParams.MATCH_PARENT);
			
			
			mainLayout.setBackgroundColor(Color.parseColor("#E3D29D"));
			super.addContentView(mainLayout, params);
		}
	}

	private void addButtonToTheView (LinearLayout mainLayout, View contentView, LayoutParams params){
		
		/*********************** ajout des boutons *****************************/
		
		createAndAddButtons(mainLayout);
		
		/*********************** ajout du separateur **************************/
		
		createAndAddSeparator(mainLayout);
		
		/*********************** ajout de la bar de proression si besoin est ************/
		
		Niveau level = getCurrentLevel();
		
		if(level != null)
			addProgressBarAndStarToTheView(mainLayout, level);
		
		/*********************** ajout du contenu ********************/
		
		mainLayout.addView(contentView,params);
		
	}
	
	private void createAndAddButtons(LinearLayout mainLayout) {
		
		LinearLayout buttonLayout = new LinearLayout(this);
		buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		/******************************* Bouton exit ***************************************/
		
		createAndAddExitButton(buttonLayout);
		
		/******************************* Bouton home ***************************************/
		
		createAndAddHomeButton(buttonLayout);
		
		/*******************************  Bouton retour ************************************/
		
		createAndAddReturnButton(buttonLayout);
		
		/**********************  Ajout des boutons au mainLayout ***************************/
		
		mainLayout.addView(buttonLayout);
		
	}

	private void createAndAddExitButton(LinearLayout buttonLayout) {

		ImageView exitButton = new ImageView(this);
		exitButton.setImageResource(R.drawable.exit);
		
		exitButton.setX(SizeCalculator.getXSizeFor(this, Constant.exitButtonPositionX));
		exitButton.setY(SizeCalculator.getYSizeFor(this, Constant.exitButtonPositionY));
		exitButton.setOnClickListener(onClickExit());

		int width = (int)SizeCalculator.getXSizeFor(this, Constant.returnHomeAndExitButtonWidth);
		int bottomMargin = (int)SizeCalculator.getYSizeFor(this, Constant.exitButtonMarginBottom);
		
		LinearLayout.LayoutParams exitParams = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
		exitParams.setMargins(0, 0, 0, bottomMargin);
		
		if(!hasExitButton()){
			exitButton.setVisibility(Button.INVISIBLE);
		}
		
		buttonLayout.addView(exitButton, exitParams);
		
	}
	
	private void createAndAddHomeButton(LinearLayout buttonLayout) {
		
		ImageView homeButton = new ImageView(this);
		homeButton.setImageResource(R.drawable.home);
		
		homeButton.setX(SizeCalculator.getXSizeFor(this, Constant.homeButtonPositionX));
		homeButton.setY(SizeCalculator.getYSizeFor(this, Constant.homeButtonPositionY));
		homeButton.setOnClickListener(onClickHome());

		int width = (int)SizeCalculator.getXSizeFor(this, Constant.returnHomeAndExitButtonWidth);
		int bottomMargin = (int)SizeCalculator.getYSizeFor(this, Constant.homeButtonMarginBottom);
		
		LinearLayout.LayoutParams homeParams = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
		homeParams.setMargins(0, 0, 0, bottomMargin);
		
		if(!hasHomeButton()){
			homeButton.setVisibility(Button.INVISIBLE);
		}
		
		buttonLayout.addView(homeButton, homeParams);
	}

	private void createAndAddReturnButton(LinearLayout buttonLayout) {

		ImageView returnButton = new ImageView(this);
		returnButton.setImageResource(R.drawable.retour);
		
		returnButton.setX(SizeCalculator.getXSizeFor(this, Constant.returnButtonPositionX));
		returnButton.setY(SizeCalculator.getYSizeFor(this, Constant.returnButtonPositionY));

		returnButton.setOnClickListener(onClickReturn());
		
		int width = (int)SizeCalculator.getXSizeFor(this, Constant.returnHomeAndExitButtonWidth);
		int bottomMargin = (int)SizeCalculator.getYSizeFor(this, Constant.returnButtonMarginBottom);
		
		LinearLayout.LayoutParams returnParams = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
		returnParams.setMargins(0, 0, 0, bottomMargin);
		
		buttonLayout.addView(returnButton, returnParams);
		
		if(!hasReturnButton()){
			returnButton.setVisibility(Button.INVISIBLE);
		}
		
	}

	private void createAndAddSeparator(LinearLayout mainLayout) {
		
		View separator = new View(this);
		separator.setBackgroundColor(Constant.separatorColor);
		
		int separatorHeight = (int)SizeCalculator.getYSizeFor(this, Constant.separatorHeight);
		int topMargin = (int)SizeCalculator.getYSizeFor(this, Constant.separatorMarginTop);
		
		LinearLayout.LayoutParams separatorParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, separatorHeight);
		separatorParams.setMargins(0, topMargin, 0, 0);
		
		mainLayout.addView(separator,separatorParams);
		
	}

	private void addProgressBarAndStarToTheView(LinearLayout mainLayout, Niveau level) {
		
		BddQube bddQube = new BddQube(this);
		bddQube.open();
		
		ArrayList<Qube> questions = bddQube.getQubesWithNiveauId(level.getIdNiveau());
		
		bddQube.close();

		int scoreMax = 0;
		
		for(int i =0; i < questions.size(); ++i){
			if(!questions.get(i).isFicheInfo())
				++scoreMax;
		}
		
		LinearLayout row = new LinearLayout(this);
		row.setOrientation(LinearLayout.HORIZONTAL);
		
		/****************** ajout de la bar de progression a row *************/
		
		int margin = (int)SizeCalculator.getYSizeFor(this, Constant.margin_between_menu_elements);
		int progressBarHeight = (int)SizeCalculator.getYSizeFor(this, Constant.progressBarHeight);
		int progressBarWidth = (int)SizeCalculator.getXSizeFor(this, Constant.progressBarWidth);
		int marginRight = (int)SizeCalculator.getYSizeFor(this, Constant.progressBarMarginRight);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(progressBarWidth, progressBarHeight);
		params.setMargins(0, margin, marginRight, 0);
		params.gravity = Gravity.CENTER;
		
		BotaProgressBar bar = new BotaProgressBar(this,android.R.attr.progressBarStyleHorizontal, scoreMax, level);		
		row.addView(bar,params);
		
		/**************** ajout de l'etoile a row *******************/
		
		ImageView img = new ImageView(this);

		if(level.getScoreActuel() >= level.getScoreToUnlock())
			img.setImageResource(R.drawable.etoile);
		else
			img.setImageResource(R.drawable.etoile_grise);
		
		int marginTop = (int)SizeCalculator.getYSizeFor(this, Constant.progressRowStarMarginTop);
		
		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
		  							   		   LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		params.setMargins(0, marginTop, 0, 0);
		row.addView(img,params);
		
		/******************** ajout de row au mainLayout ***********************/
		
		int width = (int)SizeCalculator.getXSizeFor(this, Constant.progressRowWidth);
		marginTop = (int)SizeCalculator.getYSizeFor(this, Constant.progressRowMarginTop);
		
		params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);		
		params.gravity = Gravity.CENTER;
		params.setMargins(0, marginTop, 0, 0);
		
		mainLayout.addView(row, params);
		
	}
	
	private OnClickListener onClickExit() {

		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RootActivity.this.exit();
			}
		};
	}
	
	private OnClickListener onClickHome() {

		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(RootActivity.this.homeWarning)
					RootActivity.this.homeWithWarning();
				else
					RootActivity.this.home();
			}

		};
	}

	private OnClickListener onClickReturn() {
		
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(RootActivity.this.returnWarning.length() > 0)
					RootActivity.this.returnWithWarning();
				else
					RootActivity.this.onBackKeyPressed();
			}
		};
	}
	
	protected void exit(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Voulez vous vraiment quitter l'application ?");
		builder.setPositiveButton("Continuer", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   RootActivity.finishAll();
               }
           });
		
		builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
	
		builder.show();
		
	}
	
	protected void homeWithWarning() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Voulez vous retourner au menu principale ?");
		builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   RootActivity.this.home();
               }
           });
		
		builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
	
		builder.show();
		
	}

	protected void home() {

		Intent intent = new Intent(this, MainMenu.class);
    	startActivity(intent);
    	onDestroy();
    	finish();
		
	}

	protected void returnWithWarning() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(this.returnWarning);
		builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
            	   RootActivity.this.onBackKeyPressed();
               }
           });
		
		builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
	
		builder.show();
		
	}
	
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        activities.remove(this);
    }

    public static void finishAll()
    {
        for(Activity activity:activities)
           activity.finish();
    }
}