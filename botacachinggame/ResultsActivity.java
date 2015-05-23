package com.botacachinggame;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.db.bdd.BddNiveau;
import com.db.object.Niveau;
import com.util.BotaButton;
import com.util.BotaTextView;
import com.util.Constant;
import com.util.SizeCalculator;

public class ResultsActivity extends RootActivity{

	private int levelID;
	private int nbOfFalseQuestion;
	private int nbOfRightQuestion;
	private int experienceEarned;
	private boolean nextLevelsUnlock;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/**
		 * La structure de cette activity est:
		 * 
		 * LinearLayout(verticale)(variable: mainLayout)
		 * 		TextView(QUBE X)
		 * 		Question		
		 * 		Propositions de reponse
		 * 		Bouton valider
		 * 
		 **/
		
		final Intent intent = this.getIntent();
		this.levelID = intent.getIntExtra(Constant.levelID,-1);
		this.nbOfFalseQuestion = intent.getIntExtra(Constant.nbOfFalseQuestion,-1);
		this.nbOfRightQuestion = intent.getIntExtra(Constant.nbOfRightQuestion,-1);
		this.experienceEarned = intent.getIntExtra(Constant.experienceEarned,-1);
		this.nextLevelsUnlock = intent.getBooleanExtra(Constant.nextLevelsUnlock,false);
		
		loadResults();
		
	}
	
	private void loadResults() {
		
		/********************************** Creation du mainLayout ***********************************/
		
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		
		/***************************** Creation et ajout du titre ************************************/
		
		createAndAddTitle(mainLayout);
		
		/******************** Creation et ajout du nb de question reussie ****************************/
		
		createAndAddNbOfRightQuestion(mainLayout);
		
		/********************** Creation et ajout du nb de question rate *****************************/
		
		createAndAddNbOfFalseQuestion(mainLayout);
		
		/******************** Creation et ajout de l'experience gagne ********************************/
		
		createAndAddEarnedExperience(mainLayout);
		
		/************* Creation et ajout du text pour le deblocage des niveaux suivant ***************/
		
		createAndAddLevelsUnlock(mainLayout);
		
		/****************************** Creation et ajout du bouton ok *******************************/
		
		createAndAddSuivantButton(mainLayout);
		
		/********************************** Ajout du mainLayout a l'actvity **************************/
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
		   		   							   							 LinearLayout.LayoutParams.MATCH_PARENT);
		
		this.addContentView(mainLayout, params);
		
	}

	private void createAndAddTitle(LinearLayout mainLayout) {
		
		/***************************** Creation du titre ************************************/
		
		BotaTextView title = new BotaTextView(this);
		title.setText(Constant.ResultsTitle);
		float titleSize = SizeCalculator.getTextSizeFor(this, Constant.resultsTitleSize);
		title.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
		
		/********************************** Ajout du titre au mainLayout *********************/
		
		int titleMarginBottom = (int)SizeCalculator.getYSizeFor(this, Constant.subTitleMarginBottom);
		int titleMarginTop = (int)SizeCalculator.getYSizeFor(this, Constant.subTitleMarginTop);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
																		 LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		params.setMargins(0, titleMarginTop, 0, titleMarginBottom);
		mainLayout.addView(title, params);
	}

	private void createAndAddNbOfRightQuestion(LinearLayout mainLayout) {

		/***************************** Creation du texte ************************************/
		
		BotaTextView rightQuestion = new BotaTextView(this);
		rightQuestion.setText("Questions réussies : " + this.nbOfRightQuestion);
		float textSize = SizeCalculator.getTextSizeFor(this, Constant.resultsTextSize);
		rightQuestion.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		rightQuestion.setGravity(Gravity.CENTER);
		
		/********************************** Ajout du texte au mainLayout ********************/
		
		int textMarginBottom = (int)SizeCalculator.getYSizeFor(this, Constant.resultsTextMarginBottom);
		int textMarginTop = (int)SizeCalculator.getYSizeFor(this, Constant.resultsTextMarginTop);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				 														 LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		params.setMargins(0, textMarginTop, 0, textMarginBottom);
		mainLayout.addView(rightQuestion, params);
	}

	private void createAndAddNbOfFalseQuestion(LinearLayout mainLayout) {
		
		/***************************** Creation du texte ************************************/
		
		BotaTextView falseQuestion = new BotaTextView(this);
		falseQuestion.setText("Questions ratées : " + this.nbOfFalseQuestion);
		float textSize = SizeCalculator.getTextSizeFor(this, Constant.resultsTextSize);
		falseQuestion.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		falseQuestion.setGravity(Gravity.CENTER);
		
		/********************************** Ajout du texte au mainLayout ********************/
		
		int textMarginBottom = (int)SizeCalculator.getYSizeFor(this, Constant.resultsTextMarginBottom);
		int textMarginTop = (int)SizeCalculator.getYSizeFor(this, Constant.resultsTextMarginTop);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				 														 LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		params.setMargins(0, textMarginTop, 0, textMarginBottom);
		mainLayout.addView(falseQuestion, params);
		
	}
	
	private void createAndAddEarnedExperience(LinearLayout mainLayout) {

		/***************************** Creation du texte ************************************/
		
		BotaTextView experienceEarned = new BotaTextView(this);
		experienceEarned.setText("Expérience gagnée : " + this.experienceEarned);
		float textSize = SizeCalculator.getTextSizeFor(this, Constant.resultsTextSize);
		experienceEarned.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		experienceEarned.setGravity(Gravity.CENTER);
		
		/********************************** Ajout du texte au mainLayout ********************/
		
		int textMarginBottom = (int)SizeCalculator.getYSizeFor(this, Constant.resultsTextMarginBottom);
		int textMarginTop = (int)SizeCalculator.getYSizeFor(this, Constant.resultsTextMarginTop);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				 														 LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		params.setMargins(0, textMarginTop, 0, textMarginBottom);
		mainLayout.addView(experienceEarned, params);
		
	}

	private void createAndAddLevelsUnlock(LinearLayout mainLayout) {
		
		/***************************** Creation du texte ************************************/
		
		BotaTextView text = new BotaTextView(this);
		text.setText("\nFélicitations !\n\nVous avez débloqué le(s) niveau(x) suivant(s) !!");
		float textSize = SizeCalculator.getTextSizeFor(this, Constant.resultsTextSize);
		text.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		text.setGravity(Gravity.CENTER);
		
		/********************************** Ajout du texte au mainLayout ********************/
		
		int textMarginBottom = (int)SizeCalculator.getYSizeFor(this, Constant.resultsTextMarginBottom);
		int textMarginTop = (int)SizeCalculator.getYSizeFor(this, Constant.resultsTextMarginTop);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				 														 LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		params.setMargins(0, textMarginTop, 0, textMarginBottom);
		mainLayout.addView(text, params);
		
		if(!this.nextLevelsUnlock)
			text.setVisibility(TextView.INVISIBLE);
		
	}
	
	private void createAndAddSuivantButton(LinearLayout mainLayout) {
		
		BotaButton suivantButton = new BotaButton(this);
		suivantButton.setText(Constant.suivantButtonText);
        
        float textSize = SizeCalculator.getTextSizeFor(this, Constant.textButtonSize);
        suivantButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        
        suivantButton.setOnClickListener(onClickSuivant());

        int marginTop = (int)SizeCalculator.getYSizeFor(this, Constant.resultsSuivantButtonMarginTop);
		int width  = (int)SizeCalculator.getXSizeFor(this, Constant.button_width);
		int height = (int)SizeCalculator.getYSizeFor(this, Constant.button_height);
		
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)width,(int)height);
		
		params.setMargins(0, marginTop, 0, 0);
		params.gravity = Gravity.CENTER;
		mainLayout.addView(suivantButton, params);
		
	}
	
	private OnClickListener onClickSuivant() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ResultsActivity.this.onBackKeyPressed();
				
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

		BddNiveau bddNiveau = new BddNiveau(this);
		bddNiveau.open();
		Niveau level = bddNiveau.getNiveauWithId(this.levelID);
		bddNiveau.close();
		
		return level;
	}

	@Override
	protected void onBackKeyPressed() {

		Intent intent = new Intent(ResultsActivity.this, LevelSelectionMenu.class);

		startActivity(intent);
    	onDestroy();
    	finish();	
		
	}
}
