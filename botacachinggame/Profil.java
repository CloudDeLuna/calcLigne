package com.botacachinggame;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.db.bdd.BddJoueur;
import com.db.object.Joueur;
import com.db.object.Niveau;
import com.util.BotaButton;
import com.util.BotaTextView;
import com.util.Constant;
import com.util.SizeCalculator;

public class Profil extends RootActivity{

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	/**
		 * La structure de cette activity est:
		 * LinearLayout(verticale)(variable: profilLayout)
		 * 		TextView(Profil)(variable: title)
		 * 		TextView(Pseudo)(variable: pseudoView)
		 * 		TextView(Niveau)(variable: levelView)
		 * 		TextView(Experience)(variable: xpView)
		 **/
        
        loadProfil();
        
    }

	private void loadProfil() {

		BddJoueur bddJoueur = new BddJoueur(this);
        bddJoueur.open();
		
        Joueur joueur = bddJoueur.getJoueurWhoIsPlaying();

        bddJoueur.close();
        /*************************** Creation du profilLayout *****************************/
        
        LinearLayout profilLayout = new LinearLayout(this);
        profilLayout.setOrientation(LinearLayout.VERTICAL);
        
        /************ Creation et ajout du titre au profil Layout *************************/
        
        createAndAddTitle(profilLayout);
        
        /*************************** Creation de pseudoView *****************************/
        
        createAndAddPseudoLevelAndXP(profilLayout, joueur);
        
        /***************************  *****************************/
        
        createAndAddStatButton(profilLayout);
        
        /********************************** Ajout du profilLayout a l'actvity **********************************/
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				   							   							 LinearLayout.LayoutParams.MATCH_PARENT);
        
        this.addContentView(profilLayout,params);
	}
	
	private void createAndAddTitle(LinearLayout profilLayout) {

		/*************************** Parametre pour le titre *****************************/
        
        LinearLayout.LayoutParams  params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				   														  LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		
		int titleMarginBottom = (int)SizeCalculator.getYSizeFor(this, Constant.subTitleMarginBottom);
		int titleMarginTop 	  = (int)SizeCalculator.getYSizeFor(this, Constant.subTitleMarginTop);
		
		params.setMargins(0, titleMarginTop, 0, titleMarginBottom);
        
		/*************************** Creation du titre *****************************/
		
        BotaTextView title = new BotaTextView(this);
        title.setText(Constant.profilTitle);
        float titleSize = SizeCalculator.getTextSizeFor(this, Constant.profilTitleSize);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
        
        profilLayout.addView(title,params);
		
	}
	
	private void createAndAddPseudoLevelAndXP(LinearLayout profilLayout, Joueur joueur) {

		/*************************** Creation de pseudoView *****************************/
		
		float textSize = SizeCalculator.getTextSizeFor(this, Constant.profilTextSize);
        int leftMargin = (int)SizeCalculator.getXSizeFor(this, Constant.profilTextLeftMargin);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					  						   							 LinearLayout.LayoutParams.WRAP_CONTENT);
        
        params.setMargins(leftMargin,0,0,0);
        
        BotaTextView pseudoView = new BotaTextView(this);
        pseudoView.setText("\nPseudo : \t\t\t\t\t\t\t" + joueur.getPseudo() + "\n");
        pseudoView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        
        profilLayout.addView(pseudoView,params);
        
        /*************************** Creation de levelView *****************************/
		
        BotaTextView levelView = new BotaTextView(this);
        levelView.setText("Niveau : \t\t\t\t\t\t\t\t" + joueur.getNiveau() + "\n");
        levelView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        
        profilLayout.addView(levelView,params);

        /*************************** Creation de xpView *****************************/
		
        BotaTextView xpView = new BotaTextView(this);
        xpView.setText("Expérience : \t\t" + joueur.getExperience() + "\n");
        xpView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        
        profilLayout.addView(xpView,params);
		
	}
	
	private void createAndAddStatButton(LinearLayout profilLayout){
		
		float textSize = SizeCalculator.getTextSizeFor(this, Constant.textButtonSize);
		int height = (int)SizeCalculator.getYSizeFor(this, Constant.button_height);
		int marginTop = (int)SizeCalculator.getYSizeFor(this, Constant.profilStatButtonMarginTop);
		int width  = (int)SizeCalculator.getXSizeFor(this, Constant.levelSelectionMenuButtonWidth);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, marginTop, 0, 0);
		params.gravity = Gravity.CENTER;
		
		BotaButton statButton = new BotaButton(this);
		statButton.setText("Voir les statistiques par notions");
		statButton.setMinHeight(height);
		statButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		statButton.setOnClickListener(onClickStat());
		
		profilLayout.addView(statButton, params);
		
	}
	
	private OnClickListener onClickStat() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Profil.this, StatNotionMenu.class);
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
	
	@Override
	protected void onBackKeyPressed() {
		
		Intent intent = new Intent(Profil.this, MainMenu.class);
    	startActivity(intent);
    	onDestroy();
    	finish();
	}
	
}
