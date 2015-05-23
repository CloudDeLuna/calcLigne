package com.botacachinggame;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.db.bdd.BddNiveau;
import com.db.bdd.BddQube;
import com.db.object.Niveau;
import com.db.object.Qube;
import com.util.BotaButton;
import com.util.BotaTextView;
import com.util.Constant;
import com.util.SizeCalculator;

public class InformationSheetMenu extends RootActivity{

	private int notionID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/**
		 * La structure de cette activity est:
		 * 
		 * LinearLayout(verticale)(variable: mainLayout)
		 * 		ScrollView
		 * 			LinearLayout(verticale)(variable: notionLayout)
		 * 				Boutons des notions
		 **/
		
		final Intent intent = this.getIntent();
		this.notionID = intent.getIntExtra(Constant.notionID,-1); 
		
		loadInformationSheetMenu();
		
	}
	
	private void loadInformationSheetMenu() {
		
		/********************************** Creation du mainLayout  ******************************************/
		
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		
		/********************************** Ajout du titre au mainLayout *************************************/
		
		createAndAddTitle(mainLayout);
		
		/******************************* Creation et ajout de la scrollView **********************************/
		
		createAndAddScrollView(mainLayout);
		
		/********************************** Ajout du mainLayout a l'actvity **********************************/
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
																		 LinearLayout.LayoutParams.MATCH_PARENT);
		this.addContentView(mainLayout, params);
	}

	private void createAndAddTitle(LinearLayout mainLayout) {

		/********************************** Creation du titre  ***********************************************/
		
		BotaTextView title = new BotaTextView(this);
		title.setText(Constant.infoSheetMenuTitle);
		title.setGravity(Gravity.CENTER);
		float textSize = SizeCalculator.getTextSizeFor(this, Constant.infoSheetMenuTitleSize);
		title.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		
		/********************************** Parametres du titre  ***********************************************/
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
						 												 LinearLayout.LayoutParams.WRAP_CONTENT);

		int titleMarginTop    = (int)SizeCalculator.getYSizeFor(this, Constant.subTitleMarginTop);
		int titleMarginBottom = (int)SizeCalculator.getYSizeFor(this, Constant.subTitleMarginBottom);
		
		params.gravity = Gravity.CENTER;
		params.setMargins(0, titleMarginTop, 0, titleMarginBottom);
		mainLayout.addView(title, params);
		
	}
	
	private void createAndAddScrollView(LinearLayout mainLayout) {

		/*********************************** Creation de la scrollView ***************************************/
		
		ScrollView scrollView = new ScrollView(this);
		scrollView.setScrollbarFadingEnabled(Constant.scollBarFaddingEnabled);
		
		/***************************** Creation et ajout du notionLayout ***************************************/
		
		createAndAddInfoLayout(scrollView);
		
		/*************************** Ajout de la scrollView au mainLayout ************************************/
		
		int left  = (int)SizeCalculator.getXSizeFor(this, Constant.infoSheetScrollViewMarginLeft);
		int right  = (int)SizeCalculator.getXSizeFor(this, Constant.infoSheetScrollViewMarginRight);
		int top    = (int)SizeCalculator.getYSizeFor(this, Constant.infoSheetScrollViewMarginTop);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				   							   							 LinearLayout.LayoutParams.MATCH_PARENT);
		
		params.setMargins(left, top, right, 0);
		mainLayout.addView(scrollView, params);
	}
	
	private void createAndAddInfoLayout(ScrollView scrollView) {
		
		/***************************** Creation du infoLayout ***************************************/
		
		LinearLayout infoLayout = new LinearLayout(this);
		infoLayout.setOrientation(LinearLayout.VERTICAL);
		
		/***************************** Recuperation de tout les niveaux *****************************/
		
		BddNiveau bddLevel = new BddNiveau(this);
		bddLevel.open();
		
		ArrayList<Niveau> niveaux = bddLevel.getNiveauxWithNotionId(this.notionID);
		
		bddLevel.close();
		
		/***************************** Creation des parametres pour les boutons ***********************/
		
		int height = (int)SizeCalculator.getYSizeFor(this, Constant.button_height);
		int width  = (int)SizeCalculator.getXSizeFor(this, Constant.levelSelectionMenuButtonWidth);
		int bottom = (int)SizeCalculator.getYSizeFor(this, Constant.margin_between_menu_elements);
		float textSize = SizeCalculator.getTextSizeFor(this, Constant.textButtonSize);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
		params.setMargins(0, 0, 0, bottom);
		params.gravity = Gravity.CENTER;
		
		/******************************** Creation des boutons ***************************************/
		
		BddQube bddQube = new BddQube(this);
		bddQube.open();
		
		ArrayList<Qube> fichesInfo;
		BotaButton infoButton;
		
		int numFicheInfo = 1;
		
		for(int i = 0; i < niveaux.size(); ++i){
			
			fichesInfo = bddQube.getFicheInfoWithLevelID(niveaux.get(i).getIdNiveau());
			
			if(fichesInfo == null)
				continue;
			
			for(int j = 0; j < fichesInfo.size(); ++j){
				
				infoButton = new BotaButton(this);
				infoButton.setText("Fiche Info. N°" + (numFicheInfo++));
				infoButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
//				infoButton.setMinHeight(height);
				infoButton.setOnClickListener(onClickInfoSheet(fichesInfo.get(j)));
				
				if(fichesInfo.get(j).getEtat() != Qube.QUESTION_REUSSI)
					infoButton.setEnabled(false);
			
				infoLayout.addView(infoButton,params);
				
			}
		}
		
		bddQube.close();
		
		scrollView.addView(infoLayout);
	}

	private OnClickListener onClickInfoSheet(final Qube ficheInfo) {
		
		return new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent intent = new Intent(InformationSheetMenu.this, InformationSheet.class);
				intent.putExtra(Constant.notionID, InformationSheetMenu.this.notionID);
				intent.putExtra(Constant.ficheInfoID, ficheInfo.getIdQube());
				startActivity(intent);
		    	onDestroy();
		    	finish();
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
		
		Intent intent = new Intent(InformationSheetMenu.this, InformationNotionMenu.class);
		startActivity(intent);
    	onDestroy();
    	finish();
	}

}
