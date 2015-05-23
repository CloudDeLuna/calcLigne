package com.botacachinggame;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.db.bdd.BddNiveau;
import com.db.bdd.BddNotion;
import com.db.bdd.BddParcours;
import com.db.bdd.BddPrerequis;
import com.db.bdd.BddQube;
import com.db.object.Niveau;
import com.db.object.Notion;
import com.db.object.Prerequis;
import com.db.object.Qube;
import com.util.BotaButton;
import com.util.BotaProgressBar;
import com.util.BotaTextView;
import com.util.Constant;
import com.util.QubeGiver;
import com.util.SizeCalculator;

public class LevelSelectionMenu extends RootActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/**
		 * La structure de cette activity est:
		 * 
		 * LinearLayout(verticale)(variable: mainLayout)
		 * 		TextView(Title)
		 * 		ScrollView
		 * 			LinearLayout(verticale)(variable: notionLayout)
		 * 				Pour chaque notions:
		 * 					ProgressBar
		 * 					LinearLayout (horizontale)(variable: row)
		 * 						Boutons des notions
		 * 						ImageView
		 **/
		
		loadNotionMenu();
	}

	private void loadNotionMenu() {
		
		/************************************* Creation du notionLayout *****************************************/
		
		LinearLayout notionLayout = createNotionAndLevelLayout();

		/********************************** Creation du mainLayout  ******************************************/
		
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		
		/********************************** Ajout du titre au mainLayout *************************************/
		
		createAndAddTitle(mainLayout);
		
		/*********************** Creation de la scrollView et ajout du notionLayout  **************************/
		
		createAndAddScrollView(mainLayout, notionLayout);
		
		/********************************** Ajout du mainLayout a l'actvity **********************************/
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				 							   							 LinearLayout.LayoutParams.MATCH_PARENT);
		this.addContentView(mainLayout, params);
	
	}
	
	private void createAndAddScrollView(LinearLayout mainLayout, LinearLayout notionLayout) {

		/*********************** Creation de la scrollView et ajout du notionLayout  **************************/
		
		ScrollView scrollView = new ScrollView(this);
		scrollView.setScrollbarFadingEnabled(Constant.scollBarFaddingEnabled);
		scrollView.addView(notionLayout);
		
		/********************************** Ajout de la scrollView au mainLayout *****************************/
		
		int left  = (int)SizeCalculator.getXSizeFor(this, Constant.levelSelectionMenuScrollViewMarginLeft);
		int right  = (int)SizeCalculator.getXSizeFor(this, Constant.levelSelectionMenuScrollViewMarginRight);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				   							   							 LinearLayout.LayoutParams.MATCH_PARENT);
		
		params.setMargins(left, 0, right, 0);
		mainLayout.addView(scrollView, params);
		
	}

	private void createAndAddTitle(LinearLayout mainLayout) {

		/********************************** Creation du titre  ***********************************************/
		
		BotaTextView title = new BotaTextView(this);
		title.setText(Constant.levelSelectionMenuTitle);
		title.setGravity(Gravity.CENTER);
		float textSize = SizeCalculator.getTextSizeFor(this, Constant.levelSelectionMenuTitleSize);
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

	private LinearLayout createNotionAndLevelLayout(){
		
		/************************* creation du notionLayout *******************************/
		
		LinearLayout notionLayout = new LinearLayout(this);
		notionLayout.setOrientation(LinearLayout.VERTICAL);
		
		/************************* récupération des notions *******************************/
		
		BddParcours bddParcours = new BddParcours(this);
		bddParcours.open();
		int parcoursID = bddParcours.getAllParcours().get(0).getId();//TODO a changer quand il y aura plusieur parcours
		bddParcours.close();
		
		BddNotion bddNotions = new BddNotion(this);
		bddNotions.open();
		ArrayList<Notion> notions = bddNotions.getNotionsWithParcoursId(parcoursID);
		bddNotions.close();
		
		/************************* récupération des prérequis *******************************/
		
		ArrayList<Notion> orderedNotion = createOrderForNotions(notions);
		
		/**************** récupération des niveaux de chaques notions ***********************/
		
		ArrayList<Niveau> levelForOrderedNotions [] = getLevelsForNotions(orderedNotion);
		
		/************************* création des boutons *************************************/
		
		ArrayList<Niveau> levels;
		Notion notion;

		boolean levelAdded = true;
		int currentNumLevel = 0;
		
		
		while(levelAdded){
		
			levelAdded = false;

			for(int i = 0; i < orderedNotion.size() ; ++i){
				
				notion = orderedNotion.get(i);
				levels = levelForOrderedNotions[i];
				
				//ajout du niveau
				if(currentNumLevel < levels.size()){
					
					levelAdded = true;
					
					createAndAddRow(notionLayout, levels.get(currentNumLevel),notion);
					
				}
			}
			
			++currentNumLevel;
		}
		
		return notionLayout;
	}
	
	private void createAndAddRow(LinearLayout notionLayout, Niveau level, Notion notion) {
		
		BddQube bddQube = new BddQube(this);
		bddQube.open();
		
		ArrayList<Qube> questions = bddQube.getQubesWithNiveauId(level.getIdNiveau());
		
		bddQube.close();

		int scoreMax = 0;
		
		for(int i =0; i < questions.size(); ++i){
			if(!questions.get(i).isFicheInfo())
				++scoreMax;
		}

		createAndAddProgressBar(notionLayout, level, scoreMax);
		
		LinearLayout row = new LinearLayout(this);
		row.setOrientation(LinearLayout.HORIZONTAL);
		
		/*************************** level button ***************************/
		
		createAndAddLevelButton(row, level, notion, scoreMax);
	
		/*************************** Etoile *********************************/
		
		createAndAddStar(row,level);
		
		/****************** Ajout de row au notioLayout *********************/
		
		notionLayout.addView(row);
		
		
	}

	private void createAndAddProgressBar(LinearLayout notionLayout, Niveau level, int scoreMax) {

		int margin = (int)SizeCalculator.getYSizeFor(this, Constant.margin_between_menu_elements);
		int progressBarHeight = (int)SizeCalculator.getYSizeFor(this, Constant.progressBarHeight);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, progressBarHeight);
		params.setMargins(0, margin, 0, 0);
		
		BotaProgressBar bar = new BotaProgressBar(this,android.R.attr.progressBarStyleHorizontal, scoreMax, level);
		
		notionLayout.addView(bar,params);
	}

	private void createAndAddLevelButton(LinearLayout row, Niveau level, Notion notion, int scoreMax) {

		/**************** parametres des boutons levels ******************/
		
		int height = (int)SizeCalculator.getYSizeFor(this, Constant.button_height);
		int width = (int)SizeCalculator.getXSizeFor(this, Constant.levelSelectionMenuButtonWidth);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		int margin = (int)SizeCalculator.getYSizeFor(this, Constant.margin_between_menu_elements);
		//int left = (int)SizeCalculator.getYSizeFor(this, Constant.notionButtonMarginLeft);
		int right = (int)SizeCalculator.getYSizeFor(this, Constant.levelSelectionMenuButtonMarginRight);
		
		params.setMargins(0, margin, right, margin);

		float textSize = SizeCalculator.getTextSizeFor(this, Constant.textButtonSize);
		
		/************************ level button ***************************/
		
		BotaButton level_button = new BotaButton(this);
		level_button.setText(notion.getName() + " lvl." + level.getNumNiveau());
		level_button.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		level_button.setMinHeight(height);
		
		level_button.setOnClickListener(onClickLevel(notion.getId(),level.getIdNiveau()));

		if(!level.isPlayable()){
			level_button.setEnabled(false);
		}
		
		row.addView(level_button,params);
		
	}

	private void createAndAddStar(LinearLayout row, Niveau level) {

		ImageView img = new ImageView(this);

		if(level.getScoreActuel() >= level.getScoreToUnlock())
			img.setImageResource(R.drawable.etoile);
		else
			img.setImageResource(R.drawable.etoile_grise);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
		  							   		   							 LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		row.addView(img,params);
		
	}
	
	public ArrayList<Notion> createOrderForNotions(ArrayList<Notion> notions){

		ArrayList<Notion> orderedNotion = new ArrayList<Notion>();
		
		Prerequis prerequis;
		
		BddPrerequis bddPrerquis = new BddPrerequis(this);
		bddPrerquis.open();
		HashMap<Integer, Prerequis> prerequisMap = new HashMap<Integer, Prerequis>();
		
		
		//les premieres notions sont les notions qui n'ont pas de prerequis
		for(int i = 0; i < notions.size(); ++i){
			prerequis = bddPrerquis.getPrerequisWithId(notions.get(i).getId());
			prerequisMap.put(notions.get(i).getId(), prerequis);
			
			if(prerequis == null){
				orderedNotion.add(notions.get(i));
			}
		}
		
		bddPrerquis.close();

		BddNotion bddNotions = new BddNotion(this);
		bddNotions.open();

		Notion current_notion;
		Notion current_prerequis;
		ArrayList<Integer> prerequisList;
		int nbNotionTraite = orderedNotion.size();
		final int nbNotionATraite = notions.size();
		int indice = 0;
		
		
		while(nbNotionTraite < nbNotionATraite){
			
			current_notion = notions.get(indice % nbNotionATraite);

			
			if(!orderedNotion.contains(current_notion)){
				//la notion n'est pas déjà rangée dans la list
				
				prerequis = prerequisMap.get(current_notion.getId());
				boolean canAdd = true;

				if(prerequis != null){
					//si la notion a des prérequis
					prerequisList = prerequis.getNotionsPrerequises();
					for(int i = 0; i < prerequisList.size(); ++i){
						
						current_prerequis = bddNotions.getNotionWithId(prerequisList.get(i));
						
						if(!orderedNotion.contains(current_prerequis)){
							canAdd = false;
							break;
						}
					}
				}
				if(canAdd){
					orderedNotion.add(current_notion);
					++nbNotionTraite;
				}
			}
			++indice;
		}
		
		bddNotions.close();
		
		return orderedNotion;
	}
	
	private ArrayList<Niveau>[] getLevelsForNotions(ArrayList<Notion> orderedNotion) {

		ArrayList<Niveau> niveaux[] = new ArrayList[orderedNotion.size()]; 
		
		BddNiveau bddNiveaux = new BddNiveau(this);
		bddNiveaux.open();
		
		ArrayList<Niveau> currentLevels;
		
		for(int i = 0; i < orderedNotion.size(); ++i){
			
			currentLevels = bddNiveaux.getNiveauxWithNotionId(orderedNotion.get(i).getId());
			
			//TODO
			/** Ligne a decommenté pour que les zones d'observations soit prise en compte dans l'affichage des boutons 
			 * * /
			
			currentLevels = QubeGiver.getLevelsWithZO(this, currentLevels);
			/***/
			niveaux[i] = currentLevels;
			
		}
		
		bddNiveaux.close();
		return niveaux;
	}
	
	private OnClickListener onClickLevel(final int notionID, final int levelID) {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				BddNiveau bddLevel = new BddNiveau(LevelSelectionMenu.this);
				bddLevel.open();
				
				Niveau level = bddLevel.getNiveauWithId(levelID);
				
				bddLevel.close();
				
				Qube firstQube = null;
				
				//si ordre aléatoire
				if(level.isRandom()){
					QubeGiver.setQubeList(LevelSelectionMenu.this, levelID);
					firstQube = QubeGiver.getNextQube();
				}
				//si ordre normale
				else{
					BddQube bddQube = new BddQube(LevelSelectionMenu.this);
					bddQube.open();
					ArrayList<Qube> qubes = bddQube.getQubesWithNiveauId(levelID);				
					bddQube.close();
					
					int cpt = 0;
					while(cpt < qubes.size()){
						if(firstQube == null || (firstQube.getNumQube() > qubes.get(cpt).getNumQube() && qubes.get(cpt).getEtat() != Qube.QUESTION_REUSSI) || (qubes.get(cpt).getEtat() != Qube.QUESTION_REUSSI && firstQube.getEtat() == Qube.QUESTION_REUSSI))
							firstQube = qubes.get(cpt);
						
						++cpt;
					}
				}
				
				Intent intent = new Intent(LevelSelectionMenu.this, GeolocalisationActivity.class);
				intent.putExtra(Constant.notionID, notionID);
				intent.putExtra(Constant.levelID, levelID);
				if(firstQube != null)
					intent.putExtra(Constant.qubeID, firstQube.getIdQube());
				intent.putExtra(Constant.qubeNumber, 1);
				intent.putExtra(Constant.nbOfFalseQuestion, 0);
				intent.putExtra(Constant.nbOfRightQuestion, 0);
				intent.putExtra(Constant.experienceEarned, 0);
				intent.putExtra(Constant.nextLevelsUnlock, false);
		    	
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
	protected boolean hasHomeButton() {
		return true;
	}	
	
	@Override
	protected boolean hasExitButton() {
		return true;
	}
	
	@Override
	protected Niveau getCurrentLevel() {
		return null;
	}
	
	protected void onBackKeyPressed() {
		Intent intent = new Intent(LevelSelectionMenu.this, MainMenu.class);
//		intent.putExtra(Constant.parcoursID, this.parcoursID);
		
    	startActivity(intent);
    	onDestroy();
    	finish();
	}

}
