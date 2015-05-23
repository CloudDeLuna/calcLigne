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

import com.db.bdd.BddNotion;
import com.db.object.Niveau;
import com.db.object.Notion;
import com.util.BotaButton;
import com.util.BotaTextView;
import com.util.Constant;
import com.util.SizeCalculator;

public abstract class AbstractNotionMenu extends RootActivity{

	protected abstract int checkLevelsInNotion(Notion current_notion);
	protected abstract Class<?> getNextActivity();
	protected abstract Class<?> getPreviousActivity();
	
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
		
		loadNotionMenu();
		
	}
	
	private void loadNotionMenu() {
		
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
		title.setText(Constant.infoNotionMenuTitle);
		title.setGravity(Gravity.CENTER);
		float textSize = SizeCalculator.getTextSizeFor(this, Constant.infoNotionMenuTitleSize);
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
		
		createAndAddNotionLayout(scrollView);
		
		/*************************** Ajout de la scrollView au mainLayout ************************************/
		
		int left  = (int)SizeCalculator.getXSizeFor(this, Constant.infoSheetScrollViewMarginLeft);
		int right  = (int)SizeCalculator.getXSizeFor(this, Constant.infoSheetScrollViewMarginRight);
		int top    = (int)SizeCalculator.getYSizeFor(this, Constant.infoSheetScrollViewMarginTop);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				   							   							 LinearLayout.LayoutParams.MATCH_PARENT);
		
		params.setMargins(left, top, right, 0);
		mainLayout.addView(scrollView, params);
	}

	private void createAndAddNotionLayout(ScrollView scrollView) {

		/***************************** Creation du notionLayout ***************************************/
		
		LinearLayout notionLayout = new LinearLayout(this);
		notionLayout.setOrientation(LinearLayout.VERTICAL);
		
		/***************************** Recuperation de toutes les notions *****************************/
		
		BddNotion bddNotions = new BddNotion(this);
		bddNotions.open();
		
		//bddNotions.getNotionsWithParcoursId(parcoursId);
		ArrayList<Notion> notions = bddNotions.getAllNotions();
		
		bddNotions.close();
		
		/***************************** Creation des parametres pour les boutons ***********************/
		
		int height = (int)SizeCalculator.getYSizeFor(this, Constant.button_height);
		int width  = (int)SizeCalculator.getXSizeFor(this, Constant.levelSelectionMenuButtonWidth);
		int bottom = (int)SizeCalculator.getYSizeFor(this, Constant.margin_between_menu_elements);
		float textSize = SizeCalculator.getTextSizeFor(this, Constant.textButtonSize);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, 0, bottom);
		params.gravity = Gravity.CENTER;
		
		/******************************** Creation des boutons ***************************************/
		
		int etat;
		Notion current_notion;
		BotaButton notionButton;
		for(int i = 0; i < notions.size(); ++i){
		
			current_notion = notions.get(i);
			etat = checkLevelsInNotion(current_notion);
			
			//la notions courantes n'a aucune fiche informative
			if(etat == -1)
				continue;
			
			notionButton = new BotaButton(this);
			notionButton.setText(current_notion.getName());
			notionButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
			notionButton.setMinHeight(height);
			notionButton.setOnClickListener(onClickNotion(current_notion));
			
			//la notion courante possède des fiches infos mais aucune n'est débloquée
			if(etat == 0){
				notionButton.setEnabled(false);
			}

			notionLayout.addView(notionButton,params);
			
		}
		
		scrollView.addView(notionLayout);
	}

	private OnClickListener onClickNotion(final Notion notion) {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(AbstractNotionMenu.this, getNextActivity());
				intent.putExtra(Constant.notionID, notion.getId());
		    	
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
		
		Intent intent = new Intent(this, getPreviousActivity());
		startActivity(intent);
    	onDestroy();
    	finish();
	}
	
}
