package com.botacachinggame;

import java.util.ArrayList;
import java.util.Random;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.db.bdd.BddJoueur;
import com.db.bdd.BddLiaison;
import com.db.bdd.BddNiveau;
import com.db.bdd.BddPrerequis;
import com.db.bdd.BddQube;
import com.db.object.Joueur;
import com.db.object.Liaison;
import com.db.object.Niveau;
import com.db.object.Prerequis;
import com.db.object.Qube;
import com.util.BotaButton;
import com.util.BotaTextView;
import com.util.Constant;
import com.util.QubeGiver;
import com.util.RadioGroupGrid;
import com.util.SizeCalculator;

public class QuestionInterface extends RootActivity{
	
	private int levelID;
	private int notionID;
	private int qubeID;
	private int qubeNumber;
	private int nbOfFalseQuestion;
	private int nbOfRightQuestion;
	private int experienceEarned;
	private boolean nextLevelsUnlock;
	private int zoneObsId;
	
	private String returnWarning = "Voulez vous quittez cette question ?";
	
	private RadioGroupGrid radioGroup;
	
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
		this.notionID = intent.getIntExtra(Constant.notionID,-1); 
		this.qubeID = intent.getIntExtra(Constant.qubeID,-1);
		this.qubeNumber = intent.getIntExtra(Constant.qubeNumber,-1);
		this.nbOfFalseQuestion = intent.getIntExtra(Constant.nbOfFalseQuestion,-1);
		this.nbOfRightQuestion = intent.getIntExtra(Constant.nbOfRightQuestion,-1);
		this.experienceEarned = intent.getIntExtra(Constant.experienceEarned,-1);
		this.nextLevelsUnlock = intent.getBooleanExtra(Constant.nextLevelsUnlock,false);
		this.zoneObsId = intent.getIntExtra(Constant.zoneObsID,-1);
		
		loadQuestionInterface();
	}
	
    private void loadQuestionInterface(){

    	final BddQube bddQube = new BddQube(this);
		bddQube.open();
		
		final Qube currentQube = bddQube.getQubeWithId(this.qubeID);


		BddNiveau bddLevel = new BddNiveau(QuestionInterface.this);
		bddLevel.open();
		
		Niveau level = bddLevel.getNiveauWithId(QuestionInterface.this.levelID);
		
		bddLevel.close();
		
		Qube nextQube = null;
		if(level.isRandom())
			nextQube = QubeGiver.getNextQube();
		else{
			Qube firstNextQube = bddQube.getNextQube(currentQube);
			
			if(qubeNumber > level.getScoreToUnlock())
				nextQube = null;
			else if(firstNextQube == null || firstNextQube.getEtat() != Qube.QUESTION_REUSSI)
				nextQube = firstNextQube;
			else{
				
				do{
					firstNextQube = bddQube.getNextQube(firstNextQube);
				}
				while(firstNextQube != null && firstNextQube.getEtat() == Qube.QUESTION_REUSSI);
				
				if(firstNextQube == null)
					nextQube = bddQube.getNextQube(currentQube);
				else
					nextQube = firstNextQube;
			}
		}
		
		bddQube.close();

		if(!currentQube.isFicheInfo()){
			createAndAddMainLayoutQuestion(currentQube, nextQube);
		}
		else{
			createAndAddMainLayoutFicheInfo(currentQube, nextQube);
		}
		
	}

	private void createAndAddMainLayoutQuestion(Qube currentQube, Qube nextQube) {

		/********************************** Creation du mainLayout ***********************************/
		
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		
		/***************************** Creation et ajout du titre ************************************/
		
		//String title = Constant.qubeTitle + (currentQube.getNumQube() + 1);
		String title = Constant.qubeTitle + (this.qubeNumber++);
		createAndAddTitle(mainLayout, title);
		
		/************************************ Creation subLayout *************************************/
		
		LinearLayout subLayout = new LinearLayout(this);
		subLayout.setOrientation(LinearLayout.VERTICAL);
		
		/******************************** creation et ajout de la question ***************************/
		
		createAndAddQuestion(subLayout,currentQube);
		
		/******************************** creation des radioButtons **********************************/
		
		createAndAddResponses(subLayout,currentQube);

		/**************************** Ajout du subLayout a la scrollView ****************************/
		
		ScrollView scrollView = new ScrollView(this);
		scrollView.addView(subLayout);

		/**************************** Ajout de la scrollView au mainLayout ****************************/
		
		int scrollHeight = (int) SizeCalculator.getYSizeFor(this, Constant.qubeScrollViewHeight);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,scrollHeight);
		
		mainLayout.addView(scrollView,params);
		
        /*************************** Création du bouton Valider *****************************/
		
		createAndAddValiderButton(mainLayout, currentQube, nextQube);
		
		/********************************** Ajout du mainLayout a l'actvity **********************************/
		
		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
		   		   							   LinearLayout.LayoutParams.MATCH_PARENT);
		
		this.addContentView(mainLayout, params);
		
	}

	private void createAndAddResponses(LinearLayout mainLayout, Qube currentQube) {

		radioGroup = new RadioGroupGrid(this);
		radioGroup.setLayoutParams(new TableLayout.LayoutParams(2,2));

		/************* parametres pour les reponses *****************/
		
		int maxWidth  = (int)SizeCalculator.getXSizeFor(this, Constant.qubeOptionMaxWidth);
		int optionMarginRight = (int) SizeCalculator.getXSizeFor(this, Constant.qubeOptionMarginRight);
		int optionMarginBottom = (int) SizeCalculator.getYSizeFor(this, Constant.qubeOptionMarginBottom);

		TableRow.LayoutParams params2 = new TableRow.LayoutParams(maxWidth, TableRow.LayoutParams.WRAP_CONTENT);
		params2.setMargins(0, 0, optionMarginRight, optionMarginBottom);
		
		/**************** creations des reponses *******************/

		ArrayList<String> propositions = new ArrayList<String>();
		
		int nbPropositionsToAdd = 0;
		
		if(currentQube.getProposition1().length() != 0){
			propositions.add(currentQube.getProposition1());
			++nbPropositionsToAdd;
		
			if(currentQube.getProposition2().length() != 0){
				propositions.add(currentQube.getProposition2());
				++nbPropositionsToAdd;
			
				if(currentQube.getProposition3().length() != 0){
					propositions.add(currentQube.getProposition3());
					++nbPropositionsToAdd;
				
					if(currentQube.getProposition4().length() != 0){
						propositions.add(currentQube.getProposition4());
						++nbPropositionsToAdd;
					}
				}
			}
		}

		TableRow tr1 = new TableRow(this);
		TableRow tr2 = new TableRow(this);
		RadioButton rep;
		
		int nbPropositionAdded = 0;
		int num;
		Random rand = new Random();
		
		while(nbPropositionAdded != nbPropositionsToAdd){

			num = Math.abs(rand.nextInt()) % nbPropositionsToAdd;
			
			if(propositions.get(num) == null)
				continue;

			++nbPropositionAdded;
			
			rep = new RadioButton(this);
			rep.setTextColor(Color.parseColor("#3B170B"));
			rep.setText(propositions.get(num));
			rep.setId(num+1);
			
			if(tr1.getChildCount() != 2)
				tr1.addView(rep,params2);
			else
				tr2.addView(rep,params2);
			
			propositions.set(num, null);
		}
		
//		if(currentQube.getProposition1().length() != 0){
//			
//			RadioButton rep = new RadioButton(this);
//			rep.setText(currentQube.getProposition1());
//			rep.setId(1);
//			tr1.addView(rep,params2);
//		
//			if(currentQube.getProposition2().length() != 0){
//		
//				rep = new RadioButton(this);
//				rep.setText(currentQube.getProposition2());
//				rep.setId(2);
//				tr1.addView(rep,params2);
//				
//				if(currentQube.getProposition3().length() != 0){
//					
//					rep = new RadioButton(this);
//					rep.setText(currentQube.getProposition3());
//					rep.setId(3);
//					tr2.addView(rep,params2);
//					
//					if(currentQube.getProposition4().length() != 0){
//						
//						rep = new RadioButton(this);
//						rep.setText(currentQube.getProposition4());
//						rep.setId(4);
//						tr2.addView(rep,params2);
//					}
//				}
//			}
//		}
		
		if(tr1.getChildCount() != 0){
			
			radioGroup.addView(tr1);
			
			if(tr2.getChildCount() != 0){
				radioGroup.addView(tr2);	
			}
		}
		
		int optionGroupMarginBottom = (int) SizeCalculator.getYSizeFor(this, Constant.qubeOptionGroupMarginBottom);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				   														 LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		params.setMargins(0, 0, 0, optionGroupMarginBottom);
		
		mainLayout.addView(radioGroup, params);
		
	}

	private void createAndAddTitle(LinearLayout mainLayout, String titleStr) {

		/***************************** Creation du titre ************************************/
		
		BotaTextView title = new BotaTextView(this);
		title.setText(titleStr);
		float titleSize = SizeCalculator.getTextSizeFor(this, Constant.qubeTitleSize);
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

	private void createAndAddQuestion(LinearLayout mainLayout, Qube currentQube) {

		BotaTextView question = new BotaTextView(this);
		question.setText(currentQube.getQuestion()); 
		float textSize = SizeCalculator.getTextSizeFor(this, Constant.qubeQuestionTextSize);
		question.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		question.setGravity(Gravity.CENTER);
		
		int questionMarginTop = (int)SizeCalculator.getYSizeFor(this, Constant.questionMarginTop);
		int questionMarginBottom = (int)SizeCalculator.getYSizeFor(this, Constant.questionMarginBottom);
		int questionWidth = (int) SizeCalculator.getXSizeFor(this, Constant.qubeQuestionSize);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(questionWidth,LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		params.setMargins(0, questionMarginTop, 0, questionMarginBottom);
		
		mainLayout.addView(question, params);
		
	}
	
	private void createAndAddValiderButton(LinearLayout mainLayout, Qube currentQube, Qube nextQube) {

        BotaButton validerButton = new BotaButton(this);
        validerButton.setText(Constant.validerButtonText);
        
        float textSize = SizeCalculator.getTextSizeFor(this, Constant.textButtonSize);
        validerButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        
        validerButton.setOnClickListener(onClickValider(currentQube, nextQube));

        int marginTop = (int)SizeCalculator.getYSizeFor(this, Constant.validerButtonMarginTop);
		int width  = (int)SizeCalculator.getXSizeFor(this, Constant.button_width);
		int height = (int)SizeCalculator.getYSizeFor(this, Constant.button_height);
		
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)width,(int)height);
		
		params.setMargins(0, marginTop, 0, 0);
		params.gravity = Gravity.CENTER;
		mainLayout.addView(validerButton, params);
		
	}
	
	private OnClickListener onClickValider(final Qube currentQube, final Qube nextQube){

		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 
				if(radioGroup.getCheckedRadioButtonId() == -1){
					Toast.makeText(QuestionInterface.this,"Aucune réponse sélectionnée !", Toast.LENGTH_LONG).show();
				}
				else{

					BddNiveau bddLevel = new BddNiveau(QuestionInterface.this);
					bddLevel.open();
					
					Niveau level = bddLevel.getNiveauWithId(QuestionInterface.this.levelID);
					RadioButton checkedAnswer = radioGroup.getCheckedRadioButton();
					
					if(checkedAnswer != null && checkedAnswer.getText().equals(currentQube.getReponse())){
						
						++QuestionInterface.this.nbOfRightQuestion;
						
						if(currentQube.getEtat() != Qube.QUESTION_REUSSI){

							//cette fonction doit etre appelée avant de changer l'etat du qube
							QuestionInterface.this.updatePlayerExperience(currentQube);
							
							currentQube.setEtat(Qube.QUESTION_REUSSI);
							
							level.setScoreActuel(level.getScoreActuel()+ 1);
							bddLevel.updateNiveau(level.getIdNiveau(), level);
							
							bddLevel.close();
							
							if(level.getScoreActuel()-1 < level.getScoreToUnlock() && level.getScoreActuel() >= level.getScoreToUnlock()){
								//Toast.makeText(QuestionInterface.this,"Vous avez debloqué le(s) niveau(x) suivant(s) !!", Toast.LENGTH_LONG).show();
								QuestionInterface.this.nextLevelsUnlock = true;
								QuestionInterface.this.unlockRelatedNotionsAndLevels(level);
							}
							
						}
						
					}
					else{
						++QuestionInterface.this.nbOfFalseQuestion;
						currentQube.setEtat(Qube.QUESTION_RATE);
					}
					
					BddQube bddQube = new BddQube(QuestionInterface.this);
					
					bddQube.open();
					bddQube.updateQube(currentQube.getIdQube(), currentQube);
					bddQube.close();
					
					QuestionInterface.this.chekZoneObsAndGoToNextQuestion(currentQube, nextQube);
				}
			}
		};
	}

	private void updatePlayerExperience(Qube currentQube) {

		BddJoueur bddJoueur = new BddJoueur(this);
		bddJoueur.open();
		
		Joueur player = bddJoueur.getJoueurWhoIsPlaying();
		
		if(currentQube.getEtat() == Qube.QUESTION_NON_TRAITE){
			player.setExperience(player.getExperience() + 100);
			this.experienceEarned += 100;
		}
		else{//la question etait fausse avant
			player.setExperience(player.getExperience() + 60);
			this.experienceEarned += 60;
		}
		
		bddJoueur.updateJoueur(player.getId(), player);
		
		bddJoueur.close();
	}

	private void unlockRelatedNotionsAndLevels(Niveau level) {
		
		//on regarde si il y a un prochain niveau a la notion courante,
		//si c'est le cas on doit aussi verifier les prerequis pour savoir si on peut debloquer le niveau
		
		BddNiveau bddNiveau = new BddNiveau(this);
		bddNiveau.open();
		
		Niveau nextLevel  = bddNiveau.getNiveauWithNotionIdAndNumLevel(this.notionID, (level.getNumNiveau()+1));
		
		if(nextLevel != null){
			boolean isOk = checkPrerequisForLevel(nextLevel);
			
			if(isOk){
				nextLevel.setPlayable(true);
				bddNiveau.updateNiveau(nextLevel.getIdNiveau(), nextLevel);
			}
		}

		//il faut aussi debloquer les notions qui on pour prerequis la notions courante
		
		BddPrerequis bddPrerequis = new BddPrerequis(this);
		bddPrerequis.open();
		
		ArrayList<Integer> notions = bddPrerequis.getNotionsIdWithPrerequis(this.notionID);
		
		bddPrerequis.close();
		
		boolean isOk;
		
		for(int i = 0; i < notions.size(); ++i){
			nextLevel = bddNiveau.getNiveauWithNotionIdAndNumLevel(notions.get(i), (level.getNumNiveau()));

			if(nextLevel != null){
				
				Log.v("Erreur", nextLevel.getIdNotion() + " lvl " + nextLevel.getNumNiveau());
				
				isOk = checkPrerequisForLevel(nextLevel);
				
				if(isOk){
					nextLevel.setPlayable(true);
					bddNiveau.updateNiveau(nextLevel.getIdNiveau(), nextLevel);
				}
			}
		}

		bddNiveau.close();
	}

	private boolean checkPrerequisForLevel(Niveau nextLevel) {
		
		BddPrerequis bddPrerequis = new BddPrerequis(this);
		bddPrerequis.open();
		
		Prerequis prerequis = bddPrerequis.getPrerequisWithId(nextLevel.getIdNotion());
		
		bddPrerequis.close();
		
		if(prerequis == null)
			return true;
		
		ArrayList<Integer> notionsPrerequise = prerequis.getNotionsPrerequises();
		
		BddNiveau bddNiveau = new BddNiveau(this);
		bddNiveau.open();
		Niveau level;
		
		for(int i = 0; i < notionsPrerequise.size(); ++i){
			level = bddNiveau.getNiveauWithNotionIdAndNumLevel(notionsPrerequise.get(i), nextLevel.getNumNiveau());
			
			//si il n'y a pas de niveau ayant le meme numeros, on considere le prerequis comme validé
			//si le scoreActuel et plus grand que le score pour deverouiller le prerequis est validé
			if(level != null && level.getScoreActuel() < level.getScoreToUnlock()){
				Log.v("Erreur", level.getIdNotion() + " score " + level.getScoreActuel() + " to unlock : " + level.getScoreToUnlock());
				return false;
			}
		}
		
		bddNiveau.close();
		
		return true;
	}

	private void createAndAddMainLayoutFicheInfo(Qube currentQube, final Qube nextQube) {

		this.returnWarning = "";
		
		addWarning(currentQube);
		
		/********************************** Creation du mainLayout ***********************************/
		
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		
		/***************************** Creation et ajout du titre ************************************/
		
		createAndAddTitle(mainLayout,Constant.ficheInfoTitle);
		
		/******************************** creation du contenu ***********************************/
		
		createAndAddInfoContent(mainLayout,currentQube);
		
        /*************************** Création du bouton Suivant *****************************/
		
		createAndAddSuivantButton(mainLayout, currentQube, nextQube);
		
		/********************************** Ajout du mainLayout a l'actvity **********************************/
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
											   							 LinearLayout.LayoutParams.MATCH_PARENT);
		
		this.addContentView(mainLayout, params);
		
	}

	private void addWarning(Qube currentQube) {

		BddQube bddQube = new BddQube(QuestionInterface.this);
		currentQube.setEtat(Qube.QUESTION_REUSSI);
		bddQube.open();
		bddQube.updateQube(currentQube.getIdQube(), currentQube);
		bddQube.close();
		
		BddJoueur bddJoueur = new BddJoueur(QuestionInterface.this);
		bddJoueur.open();
		Joueur joueur = bddJoueur.getJoueurWhoIsPlaying();
		bddJoueur.close();
		
    	final CheckBox checkBox = new CheckBox(this);
    	checkBox.setText("Ne plus montrer ce message");
    	
    	AlertDialog.Builder dial = new AlertDialog.Builder(QuestionInterface.this);
    	dial.setMessage("Les fiches informatves débloquées peuvent être revues à partir du menu\n");
	    dial.setView(checkBox);
    	
    	dial.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		{
		   public void onClick(DialogInterface dialog, int id) 
		   {

			   if(checkBox != null && checkBox.isChecked()){
				   BddJoueur bddJoueur = new BddJoueur(QuestionInterface.this);
				   bddJoueur.open();
				   
				   Joueur joueur = bddJoueur.getJoueurWhoIsPlaying();
				   joueur.setWarningVisible(false);
				   bddJoueur.updateJoueur(joueur.getId(), joueur);
				   
				   bddJoueur.close();
			   }
		   }
		});
		
    	if(joueur.isWarningVisible())
    		dial.show();
		
	}

	private void createAndAddInfoContent(LinearLayout mainLayout, Qube currentQube) {

		/***************** creation de la scollView ***********************/
		ScrollView scrollView = new ScrollView(this);
		scrollView.setScrollbarFadingEnabled(false);
		
		/**************** creation du contenu **********************/
		
		BotaTextView content = new BotaTextView(this);
		content.setText(currentQube.getQuestion()); 
		float textSize = SizeCalculator.getTextSizeFor(this, Constant.qubeFicheInfoTextSize);
		content.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		content.setGravity(Gravity.CENTER);
		
		/*********************** Ajout du contenu a la scrollView ***********/
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
														   				 LinearLayout.LayoutParams.WRAP_CONTENT);
		scrollView.addView(content,params);
		
		/****************** Ajout de la scrollView au mainLayout ****************/

		int infoMarginTop = (int)SizeCalculator.getYSizeFor(this, Constant.qubeFicheInfoMarginTop);
		int infoWidth = (int) SizeCalculator.getXSizeFor(this, Constant.qubeFicheInfoWidth);
		int infoHeight = (int) SizeCalculator.getXSizeFor(this, Constant.qubeFicheInfoHeight);
		
		params = new LinearLayout.LayoutParams(infoWidth, infoHeight);
		params.gravity = Gravity.CENTER;
		params.setMargins(0, infoMarginTop, 0, 0);

		mainLayout.addView(scrollView, params);
		
	}

	private void createAndAddSuivantButton(LinearLayout mainLayout, Qube currentQube, Qube nextQube) {

		BotaButton nextButton = new BotaButton(this);
        nextButton.setText(Constant.suivantButtonText);
        
        float textSize = SizeCalculator.getTextSizeFor(this, Constant.textButtonSize);
        nextButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        
        nextButton.setOnClickListener(onClickSuivant(currentQube, nextQube));

        int marginTop = (int)SizeCalculator.getYSizeFor(this, Constant.suivantButtonMarginTop);
		int width  = (int)SizeCalculator.getXSizeFor(this, Constant.button_width);
		int height = (int)SizeCalculator.getYSizeFor(this, Constant.button_height);
		
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)width,(int)height);
		
		params.setMargins(0, marginTop, 0, 0);
		params.gravity = Gravity.CENTER;
		mainLayout.addView(nextButton, params);
	}

	private OnClickListener onClickSuivant(final Qube currentQube, final Qube nextQube) {
		
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				QuestionInterface.this.chekZoneObsAndGoToNextQuestion(currentQube, nextQube);
			}
		};
	}
	
	private void chekZoneObsAndGoToNextQuestion(Qube currentQube, final Qube nextQube)
	{
		BddLiaison bddLiaison = new BddLiaison(QuestionInterface.this);
		bddLiaison.open();
		Liaison liaison = bddLiaison.getZoneObsWithQubeId(currentQube.getIdQube());
		bddLiaison.close();
		
		ArrayList<Integer> listZoneObsIds ;
		
		if(liaison != null)
			listZoneObsIds = liaison.getQubeOuZoneObsList();
		else
			listZoneObsIds = new ArrayList<Integer>();

		
		if(nextQube != null && !nextQube.isFicheInfo() && listZoneObsIds.contains(QuestionInterface.this.zoneObsId))
		{
			AlertDialog.Builder dial = new AlertDialog.Builder(QuestionInterface.this);
	    	dial.setMessage("La prochaine question peut se faire sur la même zone d'observation, voulez vous rester sur celle-ci ou changer de zone ?");
			
	    	dial.setPositiveButton("Rester", new DialogInterface.OnClickListener() 
			{
			   public void onClick(DialogInterface dialog, int id) 
			   {
					QuestionInterface.this.gotoNextQuestion(nextQube, true);	   
			   }
			});
			
	    	dial.setNegativeButton("Changer", new DialogInterface.OnClickListener() 
			{
	           public void onClick(DialogInterface dialog, int id) 
	           {
					QuestionInterface.this.gotoNextQuestion(nextQube, false);
	           }
	        });
			
	    	dial.show();	
		}
		else
			QuestionInterface.this.gotoNextQuestion(nextQube, false);
	}

	private void gotoNextQuestion(Qube nextQube, boolean stayOnSameZoneObs){
		
		Intent intent;
		
		if(nextQube == null){
			//Toast.makeText(QuestionInterface.this,"Plus de questions, TODO afficher les resultats", Toast.LENGTH_LONG).show();
			intent = new Intent(QuestionInterface.this, ResultsActivity.class);
		}
		else if(stayOnSameZoneObs)
		{
			intent = new Intent(QuestionInterface.this, QuestionInterface.class);
			intent.putExtra(Constant.qubeID, nextQube.getIdQube());
			intent.putExtra(Constant.qubeNumber, this.qubeNumber);
			intent.putExtra(Constant.zoneObsID, this.zoneObsId);
		}
		else{
			intent = new Intent(QuestionInterface.this, GeolocalisationActivity.class);
			intent.putExtra(Constant.qubeID, nextQube.getIdQube());
			intent.putExtra(Constant.qubeNumber, this.qubeNumber);
		}
		
		intent.putExtra(Constant.levelID, QuestionInterface.this.levelID);
		intent.putExtra(Constant.notionID, QuestionInterface.this.notionID);
		intent.putExtra(Constant.nbOfFalseQuestion, this.nbOfFalseQuestion);
		intent.putExtra(Constant.nbOfRightQuestion, this.nbOfRightQuestion);
		intent.putExtra(Constant.experienceEarned, this.experienceEarned);
		intent.putExtra(Constant.nextLevelsUnlock, this.nextLevelsUnlock);

		startActivity(intent);
    	onDestroy();
    	finish();
	}
	
	@Override
	protected boolean hasReturnButton() {
		
		this.setReturnWarning(this.returnWarning);
		
		return true;
	}

	@Override
	protected boolean hasHomeButton() {
		
		this.setHomeWarning(true);
		
		return true;
	}	
    
    @Override
	protected boolean hasExitButton() {
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
		
		Intent intent;
		if(this.nbOfFalseQuestion != 0 || this.nbOfRightQuestion != 0){
			
			intent = new Intent(QuestionInterface.this, ResultsActivity.class);
			intent.putExtra(Constant.levelID, this.levelID);
			intent.putExtra(Constant.notionID, this.notionID);
			intent.putExtra(Constant.nbOfFalseQuestion, this.nbOfFalseQuestion);
			intent.putExtra(Constant.nbOfRightQuestion, this.nbOfRightQuestion);
			intent.putExtra(Constant.experienceEarned, this.experienceEarned);
			intent.putExtra(Constant.nextLevelsUnlock, this.nextLevelsUnlock);
		}
		else{
			intent = new Intent(QuestionInterface.this, LevelSelectionMenu.class);
		}
		
		startActivity(intent);
    	onDestroy();
    	finish();
	}

}
