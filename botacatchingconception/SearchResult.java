package com.example.botacatchingconception;

import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.db.bdd.BddLiaison;
import com.example.db.bdd.BddNiveau;
import com.example.db.bdd.BddQube;
import com.example.db.object.Liaison;
import com.example.db.object.Niveau;
import com.example.db.object.Qube;
import com.example.util.BotaButton;
import com.example.util.Draw;
import com.example.util.SizeCalculator;

public class SearchResult extends RootActivity {

	private RelativeLayout RLayout;
	private DisplayMetrics metrics;
	
	private String text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		text = getIntent().getStringExtra("Param");

		RLayout = new RelativeLayout(this);
		loadReturnButton();
		loadResult();
		
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
	        	SearchResult.this.onBackPressed();
	      	}
    	});	  	
    }
	 
	private void loadResult()
	{
	  	float buttonSize = SizeCalculator.getTextSizeFor(this, 0.025f);
    	float textSize = SizeCalculator.getTextSizeFor(this, 0.03f);
    	float subtextSize = SizeCalculator.getTextSizeFor(this, 0.020f);
    	float titleSize = SizeCalculator.getTextSizeFor(this, 0.07f);
		/**** TITLE ****/ 
		TextView title = new TextView(this);
    	title.setText("Résultats de la recherche".toUpperCase(Locale.getDefault()));
    	title.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
    	title.setTextColor(Color.parseColor("#3B170B"));
    	
    	RelativeLayout.LayoutParams RLPTitle = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	RLPTitle.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    	RLPTitle.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
    	RLPTitle.setMargins(0, 50, 0, 0);
    	title.setLayoutParams(RLPTitle);
    	
    	RLayout.addView(title);
    	
    	/**** HORIZONTAL SCROLLVIEW ****/
    	HorizontalScrollView HSView = new HorizontalScrollView(this);

    	HSView.setX(50);
    	HSView.setY((int)(metrics.heightPixels/2.5));
    	
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
		LLayout.setPadding(0, 0, 100, 0);
		LLayout.setLayoutParams(Lparams);
    	LLayout.setVerticalScrollBarEnabled(true);
    	
    	/**** LISTE DES QUBES ****/
    	BddQube bddQube = new BddQube(this);
    	bddQube.open();
    	ArrayList<Qube> listeQubes = bddQube.getQubeByKeyword(text);
    	bddQube.close();
    	
    	for(final Qube qube : listeQubes)
    	{
    		LinearLayout LLayoutQube = new LinearLayout(this);
    		LLayoutQube.setOrientation(LinearLayout.VERTICAL);
        	LinearLayout.LayoutParams LLPQube = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        	LLPQube.setMargins(25, 0, 25, 0);
        	LLPQube.width = (int) (metrics.widthPixels/3.5);
        	LLayoutQube.setLayoutParams(LLPQube);
        	Draw.setBackground(LLayoutQube, getResources().getDrawable(R.drawable.backgroundborderqube));
        	
        	/****VERTICAL SCROLLVIEW*****/
        	final ScrollView scroll = new ScrollView(this);
    		LinearLayout.LayoutParams paramLayout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
    				   										LayoutParams.MATCH_PARENT);
    		paramLayout.setMargins(0, 0, 50, 0);
    		paramLayout.height = (int) (metrics.heightPixels/2);
        	scroll.setLayoutParams(paramLayout);
        	scroll.setVerticalScrollBarEnabled(true);
        	scroll.addView(LLayoutQube);
        	
        	boolean isFicheInformative = false;
        	if(qube.getNumReponse() == -1)
        		isFicheInformative = true;
        	
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
    			Draw.setBackground(LLayoutQube, getResources().getDrawable(R.drawable.backgroundborderfi));
    		}
    		else // QUBE
    		{
    			text = "QUESTION";
    			BddLiaison bddLiaison = new BddLiaison(this);
        		bddLiaison.open();
        		Liaison liaison = bddLiaison.getZoneObsWithQubeId(qube.getIdQube());
        		bddLiaison.close();
        		
        		if(liaison != null)
        			Draw.setBackground(LLayoutQube, getResources().getDrawable(R.drawable.backgroundborderqube));
        		else
        			Draw.setBackground(LLayoutQube, getResources().getDrawable(R.drawable.backgroundborderqube_without_zo));
    		}
    		titleQube.setText(text);
    		titleQube.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    		titleQube.setPadding(0, 0, 0, 15);
    		titleQube.setTextColor(Color.parseColor("#3B170B"));
    		titleQube.setGravity(Gravity.CENTER);
    		RLTitle.addView(titleQube, lptitle);
    		
    		
    		/*************CROIX SUPPRESSION****************/
    		ImageView deleteCross = new ImageView(this);
    		deleteCross.setImageResource(R.drawable.croix_fermer_bleu);
    		//deleteCross.setPadding(400, 0, 0, 1);
    		
    		deleteCross.setOnClickListener(new OnClickListener() 
        	{	
    	        @Override
    	        public void onClick(final View v) 
    	        {
    	    		AlertDialog.Builder dial = new AlertDialog.Builder(SearchResult.this);
    	        	dial.setMessage("Voulez-vous vraiment supprimer ce QUBE?");
    	        	
    	        	
    	        	dial.setPositiveButton("Valider", new DialogInterface.OnClickListener() 
    	    		{
    	    		   public void onClick(DialogInterface dialog, int id) 
    	    		   {
							BddQube bddQube = new BddQube(SearchResult.this);
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
        		
        		LLayoutQube.addView(typeQube);
    		}
    		
    		TextView question = new TextView(this);
    		question.setText(qube.getQuestion());
    		question.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    		question.setPadding(0, 0, 0, 15);
    		question.setTextColor(Color.parseColor("#3B170B"));
    		LLayoutQube.addView(question);
    		
    		/**** PROPOSITIONS ****/
    		LinearLayout LLayoutProp = new LinearLayout(this);
    		LinearLayout.LayoutParams LLP = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    		int i = 1;
    		if(!qube.getProposition1().equals(""))
    		{
    			TextView prop1 = new TextView(this);
    			prop1.setText(i+"-"+qube.getProposition1());
    			prop1.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    			prop1.setPadding(0, 0, 20, 20);
    			prop1.setTextColor(Color.parseColor("#3B170B"));
    			LLayoutProp.addView(prop1);
    			++i;
    		}
    		if(!qube.getProposition2().equals(""))
    		{
    			TextView prop2 = new TextView(this);
    			prop2.setText(i+"-"+qube.getProposition2());
    			prop2.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    			prop2.setPadding(0, 0, 20, 20);
    			prop2.setTextColor(Color.parseColor("#3B170B"));
    			LLayoutProp.addView(prop2);
    			++i;
    		}
    		if(!qube.getProposition3().equals(""))
    		{
    			TextView prop3 = new TextView(this);
    			prop3.setText(i+"-"+qube.getProposition3());
    			prop3.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    			prop3.setPadding(0, 0, 20, 20);
    			prop3.setTextColor(Color.parseColor("#3B170B"));
    			LLayoutProp.addView(prop3);
    			++i;
    		}
    		if(!qube.getProposition4().equals(""))
    		{
    			TextView prop4 = new TextView(this);
    			prop4.setText(i+"-"+qube.getProposition4());
    			prop4.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    			prop4.setPadding(0, 0, 20, 20);
    			prop4.setTextColor(Color.parseColor("#3B170B"));
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
						intent = new Intent(SearchResult.this, FicheInformation.class);
					else // QUBE
						intent = new Intent(SearchResult.this, QubeEditLayout.class);
					
					double param[] = new double[3]; // Param passé en paramètre contenant l'id de la notion et le niveau
					BddNiveau bddNiveau = new BddNiveau(SearchResult.this);
					bddNiveau.open();
					Niveau niveau = bddNiveau.getNiveauWithId(qube.getIdNiveau());
					param[0] = niveau.getIdNotion(); // ID de la notion
					param[1] = qube.getIdNiveau(); // ID du niveau
					param[2] = qube.getIdQube(); // ID du QUBE
					intent.putExtra("Param", param);
					startActivity(intent);
					finish();
				}
    			
    		});
    		LLayoutQube.addView(modifierQube);
    		
    		LLayout.addView(scroll);
    	}
    	
    	HSView.addView(LLayout);
    	RLayout.addView(HSView);
	}
	
	protected void onFinish()
	{
		Intent intent = new Intent(SearchResult.this, ParcoursEditLayout.class);
		startActivity(intent);
		finish();
	}
}
