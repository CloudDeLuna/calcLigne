package com.botacachinggame;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.db.bdd.BddNiveau;
import com.db.bdd.BddNotion;
import com.db.bdd.BddQube;
import com.db.object.Niveau;
import com.db.object.Notion;
import com.db.object.Qube;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer.GridStyle;
import com.jjoe64.graphview.LegendRenderer.LegendAlign;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.util.BotaTextView;
import com.util.Constant;
import com.util.SizeCalculator;

public class StatActivity extends RootActivity{

	private int notionID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final Intent intent = this.getIntent();
		this.notionID = intent.getIntExtra(Constant.notionID,-1);
		
		loadStats();
	}
	
	private void loadStats() {

		/********************************** Creation du mainLayout ***********************************/
		
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		
		/***************************** Creation et ajout du titre ************************************/
		
		createAndAddTitle(mainLayout);
		
		/***************************** Creation et ajout du texte ************************************/
		
		createAndAddStatContent(mainLayout);
		
		/********************************** Ajout du mainLayout a l'actvity **********************************/
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
											   							 LinearLayout.LayoutParams.MATCH_PARENT);
		
		this.addContentView(mainLayout, params);
	}

	private void createAndAddTitle(LinearLayout mainLayout) {

		BddNotion bddNotion = new BddNotion(this);
		bddNotion.open();
		
		Notion notion = bddNotion.getNotionWithId(this.notionID);
		
		bddNotion.close();
		
		/***************************** Creation du titre ************************************/
		
		BotaTextView title = new BotaTextView(this);
		title.setText(/*"Statistiques:\n" + */notion.getName());
		float titleSize = SizeCalculator.getTextSizeFor(this, Constant.statsTitleSize);
		title.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
		title.setGravity(Gravity.CENTER);
		
		/********************************** Ajout du titre au mainLayout *************************************/
		
		int titleMarginBottom = (int)SizeCalculator.getYSizeFor(this, Constant.subTitleMarginBottom);
		int titleMarginTop = (int)SizeCalculator.getYSizeFor(this, Constant.subTitleMarginTop);
	
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
																		 LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		params.setMargins(0, titleMarginTop, 0, titleMarginBottom);
		mainLayout.addView(title, params);
		
	}

	private void createAndAddStatContent(LinearLayout mainLayout) {

		
		/********************** Ajout du texte ***********************/
		
		createAndAddText(mainLayout);
		
		/********************** Ajout du graph ***********************/
		
//		ScrollView scrollView = new ScrollView(this);
		createAndAddGraph(mainLayout);
	}
	
	private void createAndAddText(LinearLayout mainLayout) {
		
		
		/** Récuperation du nombre de niveaux débloqués et à débloquer **/
		
		BddNiveau bddLevel = new BddNiveau(this);
		bddLevel.open();
		
		ArrayList<Niveau> levels = bddLevel.getNiveauxWithNotionId(this.notionID);
		
		bddLevel.close();
		
		int nbLevelLocked = 0;
		int nbLevelUnlocked = 0;

		for(int i = 0; i < levels.size() ; ++i){
			
			if(levels.get(i).isPlayable())
				++nbLevelUnlocked;
			else
				++nbLevelLocked;
			
		}
		
		/***************** Parametres pour le texte *********************/
		
		float textSize = SizeCalculator.getTextSizeFor(this, Constant.statsTextSize);
		int marginLeft = (int)SizeCalculator.getXSizeFor(this, Constant.statsTextMarginLeft);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					  						   							 LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(marginLeft, 0, 0, 0);
		
        
        /**************** Nb de niveaux débloqués ************************/
        
        BotaTextView unlockView = new BotaTextView(this);
        unlockView.setText("Niveau(x) débloqué(s) : " + nbLevelUnlocked);
        unlockView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        
        mainLayout.addView(unlockView, params);
        
        /************* Nb de niveaux restant à débloquer *****************/
        
        BotaTextView lockedView = new BotaTextView(this);
        lockedView.setText("Niveau(x) bloqué(s) : " + nbLevelLocked);
        lockedView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        
        mainLayout.addView(lockedView, params);
	}

	private void createAndAddGraph(LinearLayout mainLayout) {

		BddNiveau bddLevel = new BddNiveau(this);
		bddLevel.open();
		
		ArrayList<Niveau> levels = bddLevel.getNiveauxWithNotionId(this.notionID);
		
		bddLevel.close();
		
		int nbQubeJuste = 0;
		int nbQubeRate = 0;
		int nbQubeNonTraite = 0;
		int nbFIDebloque = 0;
		int nbFIBloque = 0;

		BddQube bddQube = new BddQube(this);
		bddQube.open();
		
		ArrayList<Qube> qubes;
		Qube qube;
		
		for(int i = 0; i < levels.size() ; ++i){
			
			qubes = bddQube.getQubesWithNiveauId(levels.get(i).getIdNiveau());
			
			if(!levels.get(i).isPlayable())
				continue;
			
			for(int j = 0; j < qubes.size(); ++j){

				qube = qubes.get(j);
				
				if(qube.isFicheInfo()){
				
					if(qube.getEtat() == Qube.QUESTION_REUSSI)
						++nbFIDebloque;
					else
						++nbFIBloque;
				}
				else{

					if(qube.getEtat() == Qube.QUESTION_REUSSI)
						++nbQubeJuste;
					else if(qube.getEtat() == Qube.QUESTION_RATE)
						++nbQubeRate;
					else
						++nbQubeNonTraite;
				}
			}
		}
		
		bddQube.close();
		
        BarGraphSeries<DataPoint> barGraphDataQubeJuste = new BarGraphSeries<DataPoint>(new DataPoint[]{ new DataPoint(0, nbQubeJuste) });
        barGraphDataQubeJuste.setTitle("Question juste");
        barGraphDataQubeJuste.setDrawValuesOnTop(true);
        barGraphDataQubeJuste.setValuesOnTopColor(Color.RED);
        barGraphDataQubeJuste.setColor(Color.parseColor(Constant.green));
	   	
        BarGraphSeries<DataPoint> barGraphDataQubeRate = new BarGraphSeries<DataPoint>(new DataPoint[]{ new DataPoint(1, nbQubeRate) });
        barGraphDataQubeRate.setTitle("Question fausse");
        barGraphDataQubeRate.setDrawValuesOnTop(true);
        barGraphDataQubeRate.setValuesOnTopColor(Color.RED);
        barGraphDataQubeRate.setColor(Color.parseColor(Constant.red));
        
        BarGraphSeries<DataPoint> barGraphDataQubeNonTraite = new BarGraphSeries<DataPoint>(new DataPoint[]{ new DataPoint(2, nbQubeNonTraite) });
        barGraphDataQubeNonTraite.setTitle("Question non traité");
        barGraphDataQubeNonTraite.setDrawValuesOnTop(true);
        barGraphDataQubeNonTraite.setValuesOnTopColor(Color.RED);
        barGraphDataQubeNonTraite.setColor(Color.parseColor(Constant.black));
        
        BarGraphSeries<DataPoint> barGraphDataFIDebloque = new BarGraphSeries<DataPoint>(new DataPoint[]{ new DataPoint(3, nbFIDebloque) });
        barGraphDataFIDebloque.setTitle("Fiche informative débloqué");
        barGraphDataFIDebloque.setDrawValuesOnTop(true);
        barGraphDataFIDebloque.setValuesOnTopColor(Color.RED);
        barGraphDataFIDebloque.setColor(Color.parseColor(Constant.light_blue));
        
        BarGraphSeries<DataPoint> barGraphDataFIBloque = new BarGraphSeries<DataPoint>(new DataPoint[]{ new DataPoint(4, nbFIBloque) });
        barGraphDataFIBloque.setTitle("Fiche informative bloqué");
        barGraphDataFIBloque.setDrawValuesOnTop(true);
        barGraphDataFIBloque.setValuesOnTopColor(Color.RED);
        barGraphDataFIBloque.setColor(Color.parseColor(Constant.dark_blue));
        
        
		GraphView barGraph = new GraphView(this);
//		barGraph.setBackgroundColor(Color.parseColor(Constant.white));
//		barGraph.getGridLabelRenderer().setGridColor(Color.parseColor(Constant.white));
		barGraph.getGridLabelRenderer().setGridStyle(GridStyle.HORIZONTAL);
		barGraph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
		barGraph.getLegendRenderer().setVisible(true);
		barGraph.getLegendRenderer().setAlign(LegendAlign.TOP);
		
		barGraph.getViewport().setXAxisBoundsManual(true);
		barGraph.getViewport().setMaxX(5);
		barGraph.getViewport().setMinX(-1);
		
		barGraph.addSeries(barGraphDataQubeJuste);
		barGraph.addSeries(barGraphDataQubeRate);
		barGraph.addSeries(barGraphDataQubeNonTraite);
		barGraph.addSeries(barGraphDataFIDebloque);
		barGraph.addSeries(barGraphDataFIBloque);
		
		barGraph.getViewport().setYAxisBoundsManual(true);
		
		int maxY = (int)(barGraph.getViewport().getMaxY(true));
		
		if(maxY % 2 == 0)
			maxY += 4;
		else
			maxY += 3;
		
		barGraph.getViewport().setMaxY(maxY);
		barGraph.getViewport().setMinY(0);
		
		int marginTop = (int)SizeCalculator.getYSizeFor(this, Constant.statsGraphMarginTop);
		int marginBottom = (int)SizeCalculator.getYSizeFor(this, Constant.statsGraphMarginBottom);
		int marginLeft = (int)SizeCalculator.getXSizeFor(this, Constant.statsGraphMarginLeft);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
																   		 LinearLayout.LayoutParams.WRAP_CONTENT);

		params.setMargins(marginLeft, marginTop, 0, marginBottom);
		
		mainLayout.addView(barGraph, params);
		
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

		Intent intent = new Intent(StatActivity.this, StatNotionMenu.class);
		intent.putExtra(Constant.notionID, this.notionID);
    	startActivity(intent);
    	onDestroy();
    	finish();
		
	}

}
