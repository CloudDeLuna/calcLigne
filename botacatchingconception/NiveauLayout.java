package com.example.botacatchingconception;

import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.db.bdd.BddLiaison;
import com.example.db.bdd.BddNiveau;
import com.example.db.bdd.BddNotion;
import com.example.db.bdd.BddQube;
import com.example.db.object.Liaison;
import com.example.db.object.Niveau;
import com.example.db.object.Notion;
import com.example.db.object.Qube;
import com.example.util.BotaButton;
import com.example.util.Draw;
import com.example.util.SizeCalculator;

public class NiveauLayout extends RootActivity 
{	
	private Notion n;
	private Niveau level;
	private RelativeLayout RLayout;
	private DisplayMetrics metrics;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		double param[] = getIntent().getDoubleArrayExtra("Param");
		int idNotion = (int)param[0];
		int idLevel = (int)param[1];
		
		BddNotion bddNotion = new BddNotion(this);
		bddNotion.open();
		this.n = bddNotion.getNotionWithId(idNotion);
		bddNotion.close();
		
		BddNiveau bddNiveau = new BddNiveau(this);
		bddNiveau.open();
		this.level = bddNiveau.getNiveauWithId(idLevel);
		bddNiveau.close();
		
		RLayout = new RelativeLayout(this);
		loadReturnButton();
		loadButton();
		loadDeleteLevelButton();
		loadLevels();
		loadOptions();

		setContentView(RLayout);
	}
	
    private void loadReturnButton()
    {
    	BotaButton returnButton = new BotaButton(this);
    	float textSize = SizeCalculator.getTextSizeFor(this, 0.025f);
    	returnButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
     	returnButton.setText("Retour");
    	
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	params.setMargins(50, 50, 0, 0);

    	returnButton.setLayoutParams(params);
    	Draw.setBackground(returnButton, getResources().getDrawable(R.drawable.backgroundbuttonexit));
    	RLayout.addView(returnButton);
    	
    	returnButton.setOnClickListener(new OnClickListener() 
    	{	
	        @Override
	        public void onClick(View v) 
	        {
	        	NiveauLayout.this.onBackPressed();
	      	}
    	});	  	
    }

    private void loadDeleteLevelButton(){
    	BotaButton deleteLevel = new BotaButton(this);
    	deleteLevel.setText("Supprimer niveau");
    	float textSize = SizeCalculator.getTextSizeFor(this, 0.025f);
    	deleteLevel.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
     	Draw.setBackground(deleteLevel, getResources().getDrawable(R.drawable.backgroundbuttonaddnotion));
    	RelativeLayout.LayoutParams paramButton = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	paramButton.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    	paramButton.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    	paramButton.setMargins(0, 30, 30, 0);
    	deleteLevel.setLayoutParams(paramButton);
    	
    	RLayout.addView(deleteLevel);
    	
    	deleteLevel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dial = new AlertDialog.Builder(NiveauLayout.this);
	        	dial.setMessage("La suppression du niveau entrainera celle des QUBES présent dans celui ci, voulez vous continuer?");
	        	
	        	
	        	dial.setPositiveButton("Valider", new DialogInterface.OnClickListener() 
	    		{
	    		   public void onClick(DialogInterface dialog, int id) 
	    		   {
	    			    BddNiveau bddNiveau = new BddNiveau(NiveauLayout.this);
		   				bddNiveau.open();
		   				bddNiveau.removeNiveauWithID(level.getIdNiveau());
		   				bddNiveau.close();
		   				
		   				BddQube bddQube = new BddQube(NiveauLayout.this);
		   				bddQube.open();
		   				bddQube.removeQubesWithIDNiveau(level.getIdNiveau());
		   				bddQube.close();
						dialog.dismiss();
						NiveauLayout.this.onBackPressed();
	    		   }
	    		});
	    		
	        	dial.setNegativeButton("Annuler", new DialogInterface.OnClickListener() 
	    		{
	               public void onClick(DialogInterface dialog, int id) 
	               {
	            	   dialog.dismiss();
	               }
	            });
	        	
	        	dial.show();
	      	}
		});
    }
    
    private void loadOptions(){
    	 BddQube bddQube = new BddQube(this);
    	 bddQube.open();
    	 int max = bddQube.getQubesWithNiveauId(this.level.getIdNiveau()).size() - bddQube.getFicheInfoWithLevelID(this.level.getIdNiveau()).size();
    	 bddQube.close();
    	 
    	 LinearLayout LLScore = new LinearLayout(this);
    	 LLScore.setOrientation(LinearLayout.VERTICAL);
    	 
    	 final SeekBar scoreBar = new SeekBar(this);
    	 scoreBar.setMax(max);
         scoreBar.setProgress(level.getScoreToUnlock());
         scoreBar.setVisibility(View.VISIBLE);
         LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
         scoreBar.setLayoutParams(param);
         
         final TextView scoreTxt = new TextView(this);
         scoreTxt.setPadding(13, 0, 0, 0);
         scoreTxt.setTextColor(Color.parseColor("#3B170B"));
    	 scoreTxt.setText("Score nécessaire:" + level.getScoreToUnlock() + "/" + scoreBar.getMax());
    	 float textSize = SizeCalculator.getTextSizeFor(this, 0.020f);
    	 scoreTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	 
         RelativeLayout.LayoutParams paramBar = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
         paramBar.addRule(RelativeLayout.ALIGN_PARENT_TOP);
         paramBar.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
         paramBar.setMargins(0, metrics.heightPixels/8, 10, 0);
         paramBar.width = metrics.widthPixels/6;
         LLScore.setLayoutParams(paramBar);
     	 
         scoreBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        	 int score = level.getScoreToUnlock();
        	 
             public void onStopTrackingTouch(SeekBar arg0) {
            	 level.setScoreToUnlock(score);
            	 BddNiveau bddNiveau = new BddNiveau(NiveauLayout.this);
            	 bddNiveau.open();
            	 bddNiveau.updateNiveau(level.getIdNiveau(), level);
            	 bddNiveau.close();
             }

             public void onStartTrackingTouch(SeekBar arg0) {
             }

             public void onProgressChanged(SeekBar arg0, int value, boolean arg2) {
            	 score = value;
            	 scoreTxt.setText("Score nécessaire: " + value + "/" + scoreBar.getMax());
             }
         });
         LLScore.addView(scoreTxt);
         LLScore.addView(scoreBar);
        
         RLayout.addView(LLScore);
         
         TextView isRandomText = new TextView(this);
         isRandomText.setText("Ordre des questions dans le jeu");
         isRandomText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
         LLScore.addView(isRandomText);
         
         final ToggleButton isRandomButton = new ToggleButton(this);
         isRandomButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
         isRandomButton.setTextOn("Au hasard");
         isRandomButton.setTextOff("Au hasard");
         
         isRandomButton.setOnCheckedChangeListener(new OnCheckedChangeListener() 
         {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				NiveauLayout.this.level.setRandom(isChecked);
				BddNiveau bddNiveau = new BddNiveau(NiveauLayout.this);
				bddNiveau.open();
				bddNiveau.updateNiveau(level.getIdNiveau(), NiveauLayout.this.level);
				bddNiveau.close();
			}
         });
         
         isRandomButton.setChecked(level.isRandom());
         LLScore.addView(isRandomButton);    
    }
    
	private void loadButton()
	{
		float buttonSize = SizeCalculator.getTextSizeFor(this, 0.025f);
		BotaButton newQube = new BotaButton(this);
    	newQube.setTextSize(TypedValue.COMPLEX_UNIT_SP, buttonSize);
     	newQube.setText("Créer une Question");
     	Draw.setBackground(newQube, getResources().getDrawable(R.drawable.backgroundbuttonaddnotion));
    	RelativeLayout.LayoutParams paramQube = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	paramQube.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    	paramQube.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
    	paramQube.setMargins((int) (metrics.widthPixels/3.5), (int)(metrics.heightPixels/4), 0, 0);
    	newQube.setLayoutParams(paramQube);
    	
    	RLayout.addView(newQube);
    	
    	newQube.setOnClickListener(new OnClickListener() 
    	{	
	        @Override
	        public void onClick(View v) 
	        {
	        	Intent intent = new Intent(NiveauLayout.this, QubeEditLayout.class);
	        	double param[] = new double[2]; // Param passé en paramètre contenant l'id de la notion et le niveau
				param[0] = n.getId(); // ID de la notion
				param[1] = level.getIdNiveau(); // ID du niveau
				intent.putExtra("Param", param);
	        	startActivity(intent);
				finish();
	      	}
      });
    	
    	BotaButton newFI = new BotaButton(this);
    	newFI.setText("Créer une Fiche Informative");
    	newFI.setTextSize(TypedValue.COMPLEX_UNIT_SP, buttonSize);
    	Draw.setBackground(newFI, getResources().getDrawable(R.drawable.backgroundbuttonaddnotion));
    	
    	RelativeLayout.LayoutParams paramFI = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	paramFI.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    	paramFI.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    	paramFI.setMargins(0, metrics.heightPixels/4, (int) (metrics.widthPixels/3.5), 0);
    	newFI.setLayoutParams(paramFI);
    	
    	newFI.setOnClickListener(new OnClickListener() {
    		
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(NiveauLayout.this, FicheInformation.class);
				double param[] = new double[2]; // Param passé en paramètre contenant l'id de la notion et le niveau
				param[0] = n.getId(); // ID de la notion
				param[1] = level.getIdNiveau(); // ID du niveau
				intent.putExtra("Param", param);
				startActivity(intent);
				finish();
			}
		});
    	
    	RLayout.addView(newFI);    	  	
    }
	 
	private void loadLevels()
	{
    	float titleSize = SizeCalculator.getTextSizeFor(this, 0.07f);
    	float textSize = SizeCalculator.getTextSizeFor(this, 0.03f);
    	float subtextSize = SizeCalculator.getTextSizeFor(this, 0.020f);
    	float buttonSize = SizeCalculator.getTextSizeFor(this, 0.025f);
    	
		/**** TITLE ****/ 
		TextView title = new TextView(this);
    	title.setText("Niveau "+level.getNumNiveau()+" de la notion "+n.getName().toUpperCase(Locale.getDefault()));
    	title.setTextColor(Color.parseColor("#3B170B"));
    	title.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
    	
    	RelativeLayout.LayoutParams RLPTitle = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	RLPTitle.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    	RLPTitle.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
    	RLPTitle.setMargins(0, 50, 0, 0);
    	title.setLayoutParams(RLPTitle);
    	
    	RLayout.addView(title);
    	
    	/**** HORIZONTAL SCROLLVIEW ****/
    	HorizontalScrollView HSView = new HorizontalScrollView(this);

    	HSView.setX(50);
    	HSView.setY((int)(metrics.heightPixels/2.3));
    	
    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

    	HSView.setHorizontalScrollBarEnabled(false);
    	HSView.setVerticalScrollBarEnabled(false);
    	HSView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
    	HSView.setLayoutParams(params);
    	
    	/**** LINEAR LAYOUT ****/
    	final LinearLayout LLayout = new LinearLayout(this);
    	LLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		LinearLayout.LayoutParams Lparams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				   LayoutParams.WRAP_CONTENT);
		//LLayout.setPadding(0, 0, metrics.widthPixels/20, 0);
		LLayout.setLayoutParams(Lparams);
    	LLayout.setVerticalScrollBarEnabled(true);
    	
    	/**** LISTE DES QUBES ****/
    	BddQube bddQube = new BddQube(this);
    	bddQube.open();
    	ArrayList<Qube> listeQubes = bddQube.getQubesWithNiveauId(level.getIdNiveau());
    	bddQube.close();
    	
    	for(final Qube qube : listeQubes)
    	{	
    		LinearLayout LLayoutQube = new LinearLayout(this);
    		LLayoutQube.setOrientation(LinearLayout.VERTICAL);
        	LinearLayout.LayoutParams LLPQube = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        	LLPQube.setMargins(metrics.widthPixels/25, 0, metrics.widthPixels/25, 0);
        	LLPQube.width = (int) (metrics.widthPixels/3.5);
        	LLayoutQube.setLayoutParams(LLPQube);
        	
        	/****VERTICAL SCROLLVIEW*****/
        	final ScrollView scroll = new ScrollView(this);
    		LinearLayout.LayoutParams paramLayout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
    				   										LayoutParams.MATCH_PARENT);
    		paramLayout.setMargins(0, 0, 50, 0);
    		paramLayout.height = (int) (metrics.heightPixels/2);
        	scroll.setLayoutParams(paramLayout);
        	scroll.setVerticalScrollBarEnabled(true);
        	scroll.addView(LLayoutQube);
        	//scroll.setPadding(0, 0, metrics.widthPixels/25, 0);

        	boolean isFicheInformative = false;
        	
        	if(qube.getNumReponse() == -1)
        	{
        		isFicheInformative = true;
        		Draw.setBackground(LLayoutQube, getResources().getDrawable(R.drawable.backgroundborderfi));
        	}
        	else
        	{
        		BddLiaison bddLiaison = new BddLiaison(this);
        		bddLiaison.open();
        		Liaison liaison = bddLiaison.getZoneObsWithQubeId(qube.getIdQube());
        		bddLiaison.close();
        		
        		if(liaison != null)
        			Draw.setBackground(LLayoutQube, getResources().getDrawable(R.drawable.backgroundborderqube));
        		else
        			Draw.setBackground(LLayoutQube, getResources().getDrawable(R.drawable.backgroundborderqube_without_zo));
        	}
        	
        	/************ TITLE QUBE / FICHE *************/
        	RelativeLayout RLTitle = new RelativeLayout(this);
        	RelativeLayout.LayoutParams lpcroix = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lpcroix.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lpcroix.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            
            RelativeLayout.LayoutParams lptitle = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lptitle.addRule(RelativeLayout.CENTER_HORIZONTAL);
            
    		TextView titleQube = new TextView(this);
    		String text = "";
    		if(isFicheInformative) // FICHE
    		{
    			text = "FICHE INFORMATIVE";
    		}
    		else // QUBE
    		{
    			text = "QUESTION";
    		}
    		titleQube.setText(text);
    		titleQube.setPadding(0, 0, 0, 15);
    		titleQube.setGravity(Gravity.CENTER);
    		titleQube.setTextColor(Color.parseColor("#3B170B"));
    		titleQube.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    		RLTitle.addView(titleQube, lptitle);
    		
    		
    		/*************CROIX SUPPRESSION****************/
    		ImageView deleteCross = new ImageView(this);
    		deleteCross.setImageResource(R.drawable.croix_fermer_bleu);
    		
    		deleteCross.setOnClickListener(new OnClickListener() 
        	{	
    	        @Override
    	        public void onClick(final View v) 
    	        {
    	    		AlertDialog.Builder dial = new AlertDialog.Builder(NiveauLayout.this);
    	        	dial.setMessage("Voulez-vous vraiment supprimer ce QUBE?");
    	        	
    	        	
    	        	dial.setPositiveButton("Valider", new DialogInterface.OnClickListener() 
    	    		{
    	    		   public void onClick(DialogInterface dialog, int id) 
    	    		   {
							BddQube bddQube = new BddQube(NiveauLayout.this);
							bddQube.open();
							bddQube.removeQubeWithID(qube.getIdQube());
							
							LLayout.removeView((View) v.getParent().getParent());
							dialog.dismiss();
    	    		   }
    	    		});
    	    		
    	        	dial.setNegativeButton("Annuler", new DialogInterface.OnClickListener() 
    	    		{
    	               public void onClick(DialogInterface dialog, int id) 
    	               {
    	            	   dialog.dismiss();
    	               }
    	            });
    	        	
    	        	dial.show();
    	      	}
        	});
    		RLTitle.addView(deleteCross, lpcroix);
    		
    		LLayoutQube.addView(RLTitle);
    		
    		if(!isFicheInformative)
    		{
    			TextView typeQube = new TextView(this);
    			typeQube.setTextSize(TypedValue.COMPLEX_UNIT_SP, subtextSize);
        		if(qube.isQcmBasic())
        			typeQube.setText("QCM");
        		else
        			typeQube.setText("Autre");
        		
        		typeQube.setTextColor(Color.parseColor("#3B170B"));
        		LLayoutQube.addView(typeQube);
    		}
    		
    		TextView question = new TextView(this);
    		question.setText(qube.getQuestion());
    		question.setPadding(0, 0, 0, 15);
    		question.setTextColor(Color.parseColor("#3B170B"));
    		question.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    		LLayoutQube.addView(question);
    		
    		/**** PROPOSITIONS ****/
    		LinearLayout LLayoutProp = new LinearLayout(this);
    		LinearLayout.LayoutParams LLP = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    		int i = 1;
    		if(!qube.getProposition1().equals(""))
    		{
    			TextView prop1 = new TextView(this);
    			prop1.setText(i+"-"+qube.getProposition1());
    			prop1.setPadding(0, 0, 20, 20);
    			prop1.setTextColor(Color.parseColor("#3B170B"));
    			prop1.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    			LLayoutProp.addView(prop1);
    			++i;
    		}
    		if(!qube.getProposition2().equals(""))
    		{
    			TextView prop2 = new TextView(this);
    			prop2.setText(i+"-"+qube.getProposition2());
    			prop2.setPadding(0, 0, 20, 20);
    			prop2.setTextColor(Color.parseColor("#3B170B"));
    			prop2.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    			LLayoutProp.addView(prop2);
    			++i;
    		}
    		if(!qube.getProposition3().equals(""))
    		{
    			TextView prop3 = new TextView(this);
    			prop3.setText(i+"-"+qube.getProposition3());
    			prop3.setPadding(0, 0, 20, 20);
    			prop3.setTextColor(Color.parseColor("#3B170B"));
    			prop3.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    			LLayoutProp.addView(prop3);
    			++i;
    		}
    		if(!qube.getProposition4().equals(""))
    		{
    			TextView prop4 = new TextView(this);
    			prop4.setText(i+"-"+qube.getProposition4());
    			prop4.setPadding(0, 0, 20, 20);
    			prop4.setTextColor(Color.parseColor("#3B170B"));
    			prop4.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    			LLayoutProp.addView(prop4);
    			++i;
    		}
    		LLayoutProp.setLayoutParams(LLP);
    		LLayoutQube.addView(LLayoutProp);
    		
    		BotaButton modifierQube = new BotaButton(this);
    		modifierQube.setText("Modifier");
    		modifierQube.setTextSize(TypedValue.COMPLEX_UNIT_SP, buttonSize);
    		Draw.setBackground(modifierQube, getResources().getDrawable(R.drawable.backgroundbuttonaddnotion));
    		modifierQube.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent intent;
					if(qube.getNumReponse() == -1) // FICHE
						intent = new Intent(NiveauLayout.this, FicheInformation.class);
					else // QUBE
						intent = new Intent(NiveauLayout.this, QubeEditLayout.class);
					
					double param[] = new double[3]; // Param passé en paramètre contenant l'id de la notion et le niveau
					param[0] = n.getId(); // ID de la notion
					param[1] = level.getIdNiveau(); // ID du niveau
					param[2] = qube.getIdQube(); // ID du QUBE
					intent.putExtra("Param", param);
					startActivity(intent);
					finish();
				}
    			
    		});
    		LLayoutQube.addView(modifierQube);
    		
    		LLayoutQube.setOnLongClickListener(new OnLongClickListener() 
    		{	
				@Override
				public boolean onLongClick(View v) {
					v.performClick();
					ClipData data = ClipData.newPlainText("", "");
					DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
					v.startDrag(data, shadowBuilder, v, 0);
					v.setVisibility(View.INVISIBLE);
					return false;
				}
    		});
    		DragAndDropListener listener = new DragAndDropListener(this, LLayout);
    		listener.sizeViewMoved = (int) (metrics.widthPixels/3.5);
    		listener.idNiveau = level.getIdNiveau();
    		DragAndDropListener.dragAndDropEnable = true;

            LLayout.setOnDragListener(listener);          
            RLayout.setOnDragListener(listener); 
    		
            LLayout.addView(scroll);
    	}
    	
    	HSView.addView(LLayout);
    	RLayout.addView(HSView);
	}
	
	protected void onFinish()
	{
		Intent intent = new Intent(NiveauLayout.this, ParcoursEditLayout.class);
		startActivity(intent);
		finish();
	}
}