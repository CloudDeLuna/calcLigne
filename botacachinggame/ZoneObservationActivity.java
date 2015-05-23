package com.botacachinggame;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.db.bdd.BddLiaison;
import com.db.bdd.BddQube;
import com.db.bdd.BddZoneObservation;
import com.db.object.Liaison;
import com.db.object.Niveau;
import com.db.object.Qube;
import com.db.object.ZoneObservation;
import com.util.BotaButton;
import com.util.BotaTextView;
import com.util.Constant;
import com.util.SizeCalculator;

public class ZoneObservationActivity extends RootActivity{

	private int levelID;
	private int notionID;
	private int qubeID;
	private int qubeNumber;
	private int nbOfFalseQuestion;
	private int nbOfRightQuestion;
	private int experienceEarned;
	private boolean nextLevelsUnlock;
	private int zoneObsId;
	
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
        this.zoneObsId = intent.getIntExtra(Constant.zoneObsID, -1);
        
		final BddQube bddQube = new BddQube(this);
		bddQube.open();
		
		final Qube currentQube = bddQube.getQubeWithId(this.qubeID);
		
		bddQube.close();
		
		//le qube courant est une fiche informative, pas besoin de géolocalisation
		if(currentQube.isFicheInfo() || this.zoneObsId == -1)
			skip();
		else
			loadZoneObservation();
    }

	private void loadZoneObservation() 
	{
        BddZoneObservation bddZoneObs = new BddZoneObservation(this);
        bddZoneObs.open();
        ZoneObservation zone = bddZoneObs.getZoneObservationWithId(this.zoneObsId);
        bddZoneObs.close();
        
        /********** Creation du geolocalisationLayout **********************/
        LinearLayout zoneObservationLayout = new LinearLayout(this);
        zoneObservationLayout.setOrientation(LinearLayout.VERTICAL);
        
        /***************** Parametre pour le titre ************************/
        LinearLayout.LayoutParams  params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				   														  LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		
		int titleMarginBottom = (int)SizeCalculator.getYSizeFor(this, Constant.subTitleMarginBottom);
		int titleMarginTop = (int)SizeCalculator.getYSizeFor(this, Constant.subTitleMarginTop);
		
		params.setMargins(0, titleMarginTop, 0, titleMarginBottom);
        
		/************** Creation du titre *****************************/
        BotaTextView title = new BotaTextView(this);
        title.setText(zone.getName());
        float titleSize = SizeCalculator.getTextSizeFor(this, Constant.zoneObservationTitleSize);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
        
        zoneObservationLayout.addView(title,params);
        
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
        RelativeLayout relativeLayout = new RelativeLayout(this);
        Bitmap originalBmp = BitmapFactory.decodeFile(zone.getPathToImg());
        ImageView image = new ImageView(this);
        double ratio = metrics.widthPixels/30*28/(double)originalBmp.getWidth();

        Bitmap newBmp = Bitmap.createScaledBitmap(originalBmp, metrics.widthPixels/30*28, (int) (originalBmp.getHeight()*ratio), false);
        image.setImageBitmap(newBmp);
        image.setX(metrics.widthPixels/30);
        image.setY(0);
        relativeLayout.addView(image);
        
        BddLiaison bddLiaison = new BddLiaison(this);
        bddLiaison.open();
        Liaison liaison = bddLiaison.getQubesWithZoneObsId(zone.getZoneId());
        bddLiaison.close();
        ArrayList<Integer> qubeIds = liaison.getQubeOuZoneObsList();
        int index = qubeIds.indexOf(this.qubeID);
        ArrayList<String> zoneObsAnnotation = liaison.getAnnotationsZoneObs();
        String posAnnotation[] = zoneObsAnnotation.get(index).split(";");
        
    	ImageView annotation = new ImageView(this);
		annotation.setImageResource(R.drawable.annotation);
		annotation.setX((int)(metrics.widthPixels/30 + newBmp.getWidth()*Double.parseDouble(posAnnotation[0])));
        annotation.setY((int)(newBmp.getHeight()*Double.parseDouble(posAnnotation[1])));
        relativeLayout.addView(annotation);
  
        
        zoneObservationLayout.addView(relativeLayout);
        /**************** Bouton "Je vois la plante" *******************/
        int width  = (int)SizeCalculator.getXSizeFor(this, Constant.button_width);
		int height = (int)SizeCalculator.getYSizeFor(this, Constant.button_height);
		int margin = (int)SizeCalculator.getYSizeFor(this, Constant.margin_between_menu_elements);
		
		params = new LinearLayout.LayoutParams(width, height);
		
		params.setMargins(0, margin, 0, 0);
		params.gravity = Gravity.CENTER;
        
        BotaButton passButton = new BotaButton(this);
        passButton.setText("Continuer");
        float textSize = SizeCalculator.getTextSizeFor(this, Constant.textButtonSize);
        passButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        
        passButton.setOnClickListener(new OnClickListener() 
        {	
			@Override
			public void onClick(View arg0) 
			{
				ZoneObservationActivity.this.skip();	
			}
		});
        
        zoneObservationLayout.addView(passButton, params);
        
        /******************* Ajout du profilLayout a l'actvity ***********************/
        
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				   							   LinearLayout.LayoutParams.MATCH_PARENT);
        
        this.addContentView(zoneObservationLayout,params);
	}
	
	private void skip()
	{
		Intent intent = new Intent(ZoneObservationActivity.this, QuestionInterface.class);
		intent.putExtra(Constant.notionID, this.notionID);
		intent.putExtra(Constant.levelID, this.levelID);
		intent.putExtra(Constant.qubeID, this.qubeID);
		intent.putExtra(Constant.qubeNumber, this.qubeNumber);
		intent.putExtra(Constant.nbOfFalseQuestion, this.nbOfFalseQuestion);
		intent.putExtra(Constant.nbOfRightQuestion, this.nbOfRightQuestion);
		intent.putExtra(Constant.experienceEarned, this.experienceEarned);
		intent.putExtra(Constant.nextLevelsUnlock, this.nextLevelsUnlock);
		intent.putExtra(Constant.zoneObsID, this.zoneObsId);

    	startActivity(intent);
    	onDestroy();
    	finish();
	}
	
	@Override
	protected boolean hasReturnButton() 
	{
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
		Intent intent = new Intent(ZoneObservationActivity.this, GeolocalisationActivity.class);
		intent.putExtra(Constant.notionID, this.notionID);
		intent.putExtra(Constant.levelID, this.levelID);
		intent.putExtra(Constant.qubeID, this.qubeID);
		intent.putExtra(Constant.nbOfFalseQuestion, this.nbOfFalseQuestion);
		intent.putExtra(Constant.nbOfRightQuestion, this.nbOfRightQuestion);
		intent.putExtra(Constant.experienceEarned, this.experienceEarned);
		intent.putExtra(Constant.nextLevelsUnlock, this.nextLevelsUnlock);
		
		startActivity(intent);
    	onDestroy();
    	finish();
	}
	
}
