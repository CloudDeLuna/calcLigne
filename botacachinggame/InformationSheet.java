package com.botacachinggame;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.db.bdd.BddQube;
import com.db.object.Niveau;
import com.db.object.Qube;
import com.util.BotaButton;
import com.util.BotaTextView;
import com.util.Constant;
import com.util.SizeCalculator;

public class InformationSheet extends RootActivity{

	private int notionID;
	private int ficheInfoID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Intent intent = this.getIntent();
		this.notionID = intent.getIntExtra(Constant.notionID,-1);
		this.ficheInfoID = intent.getIntExtra(Constant.ficheInfoID,-1);
		
		loadInformationSheet();
	}
	
	private void loadInformationSheet() {

		/********************************** Creation du mainLayout ***********************************/
		
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		
		/***************************** Creation et ajout du titre ************************************/
		
		createAndAddTitle(mainLayout);
		
		/******************************** creation du contenu ***********************************/
		
		createAndAddInfoContent(mainLayout);
		
        /*************************** Création du bouton Suivant *****************************/
		
		createAndAddRetourButton(mainLayout);
		
		/********************************** Ajout du mainLayout a l'actvity **********************************/
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
											   							 LinearLayout.LayoutParams.MATCH_PARENT);
		
		this.addContentView(mainLayout, params);
	}

	private void createAndAddTitle(LinearLayout mainLayout) {

		/***************************** Creation du titre ************************************/
		
		BotaTextView title = new BotaTextView(this);
		title.setText(Constant.infoSheetTitle);
		float titleSize = SizeCalculator.getTextSizeFor(this, Constant.infoSheetTitleSize);
		title.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
		
		/********************************** Ajout du titre au mainLayout *************************************/
		
		int titleMarginBottom = (int)SizeCalculator.getYSizeFor(this, Constant.subTitleMarginBottom);
		int titleMarginTop = (int)SizeCalculator.getYSizeFor(this, Constant.subTitleMarginTop);
	
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
																		 LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		params.setMargins(0, titleMarginTop, 0, titleMarginBottom);
		mainLayout.addView(title, params);
		
	}

	private void createAndAddInfoContent(LinearLayout mainLayout) {

		BddQube bddQube = new BddQube(this);
		bddQube.open();
		
		Qube currentQube = bddQube.getQubeWithId(this.ficheInfoID);
		
		bddQube.close();
		
		/***************** creation de la scrollView ***********************/
		ScrollView scrollView = new ScrollView(this);
		scrollView.setScrollbarFadingEnabled(false);
		
		/**************** creation du contenu **********************/
		
		BotaTextView content = new BotaTextView(this);
		content.setText(currentQube.getQuestion()); 
		float textSize = SizeCalculator.getTextSizeFor(this, Constant.infoSheetTextSize);
		content.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		content.setGravity(Gravity.CENTER);
		
		/*********************** Ajout du contenu a la scrollView ***********/
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
														   				 LinearLayout.LayoutParams.WRAP_CONTENT);
		scrollView.addView(content,params);
		
		/****************** Ajout de la scrollView au mainLayout ****************/

		int infoMarginTop = (int)SizeCalculator.getYSizeFor(this, Constant.infoSheetScrollViewMarginTop);
		int infoWidth = (int) SizeCalculator.getXSizeFor(this, Constant.infoSheetScrollViewWidth);
		int infoHeight = (int) SizeCalculator.getXSizeFor(this, Constant.infoSheetScrollViewHeight);
		
		params = new LinearLayout.LayoutParams(infoWidth, infoHeight);
		params.gravity = Gravity.CENTER;
		params.setMargins(0, infoMarginTop, 0, 0);

		mainLayout.addView(scrollView, params);
	}
	
	private void createAndAddRetourButton(LinearLayout mainLayout) {

		BotaButton nextButton = new BotaButton(this);
        nextButton.setText(Constant.returnButtonText);
        
        float textSize = SizeCalculator.getTextSizeFor(this, Constant.textButtonSize);
        nextButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        
        nextButton.setOnClickListener(onClickRetour());

        int marginTop = (int)SizeCalculator.getYSizeFor(this, Constant.infoSheetRetourButtonMarginTop);
		int width  = (int)SizeCalculator.getXSizeFor(this, Constant.button_width);
		int height = (int)SizeCalculator.getYSizeFor(this, Constant.button_height);
		
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)width,(int)height);
		
		params.setMargins(0, marginTop, 0, 0);
		params.gravity = Gravity.CENTER;
		mainLayout.addView(nextButton, params);
	}
	
	private OnClickListener onClickRetour() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				InformationSheet.this.onBackKeyPressed();
			}
		};
	}

	@Override
	protected boolean hasReturnButton() {
		return true;
	}

	@Override
	protected boolean hasExitButton() {
		return true;
	}

	@Override
	protected boolean hasHomeButton() {
		return true;
	}

	@Override
	protected Niveau getCurrentLevel() {
		return null;
	}

	@Override
	protected void onBackKeyPressed() {
	
		Intent intent = new Intent(InformationSheet.this, InformationSheetMenu.class);
		intent.putExtra(Constant.notionID, this.notionID);
		startActivity(intent);
    	onDestroy();
    	finish();
	}
}
