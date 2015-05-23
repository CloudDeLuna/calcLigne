package com.example.botacatchingconception;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.db.bdd.BddQube;
import com.example.db.object.Qube;
import com.example.util.BotaButton;
import com.example.util.Draw;
import com.example.util.SizeCalculator;

public class FicheInformation extends RootActivity 
{
	private RelativeLayout RLayout;
	private DisplayMetrics metrics;
	
	private int idNotion;
	private int idLevel;
	private int idQube;
	
	private EditText editContenu;
	private EditText editKeyword;
	
	private boolean isModified;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        isModified = false;
        RLayout = new RelativeLayout(this);
        
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        double param[] = getIntent().getDoubleArrayExtra("Param");
        String task = "";
        if(param.length == 2)
        	task = "new";
        else
        {
        	task = "modify";
        	this.idQube = (int) param[2];
        }
        
        this.idNotion = (int)param[0];
		this.idLevel = (int)param[1];
		loadReturnButton();
        loadView(task);
        initListener();
        setContentView(RLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        return super.onOptionsItemSelected(item);
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
	        	if(isModified)
	        	{
	        		AlertDialog.Builder dial = new AlertDialog.Builder(FicheInformation.this);
		        	dial.setMessage("Vous perdrez toutes vos modifications, êtes-vous sur de vouloir continuer?");
		    		
		        	dial.setPositiveButton("Valider", new DialogInterface.OnClickListener() 
		    		{
		    		   public void onClick(DialogInterface dialog, int id) 
		    		   {
				        	dialog.dismiss();
				        	FicheInformation.this.onBackPressed();
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
	        	else
	        		FicheInformation.this.onBackPressed();
	      	}
    	});	  	
    }
    
    private void loadView(final String task)
    {
    	float buttonSize = SizeCalculator.getTextSizeFor(this, 0.025f);
    	float textSize = SizeCalculator.getTextSizeFor(this, 0.03f);
    	float subtextSize = SizeCalculator.getTextSizeFor(this, 0.020f);
    	float titleSize = SizeCalculator.getTextSizeFor(this, 0.07f);
    	
    	/**** TITLE ****/ 
		TextView title = new TextView(this);
		String text = "";
		if(task.equals("new"))
			text = "Créer une fiche informative";
		else
			text = "Modification de la fiche informative";
		
    	title.setText(text);
    	title.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
    	title.setTextColor(Color.parseColor("#3B170B"));
    	
    	RelativeLayout.LayoutParams RLPTitle = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	RLPTitle.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    	RLPTitle.setMargins(metrics.widthPixels/5, 50, 0, 0);
    	title.setLayoutParams(RLPTitle);
    	
    	RLayout.addView(title);
    	
    	/****** LAYOUT GLOBAL ******/
    	LinearLayout LLayoutGlobal = new LinearLayout(this);
    	LLayoutGlobal.setOrientation(LinearLayout.HORIZONTAL);
    	RelativeLayout.LayoutParams RLPGlobal = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	LLayoutGlobal.setX(metrics.widthPixels/4);
    	LLayoutGlobal.setY(metrics.heightPixels/6);

    	LLayoutGlobal.setLayoutParams(RLPGlobal);
    	
    	/****** VIEW FI *****/
    	ScrollView scrollView = new ScrollView(this);
    	LinearLayout.LayoutParams paramLayout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
    	paramLayout.height = (int) (metrics.heightPixels/6*4.70);
    	scrollView.setLayoutParams(paramLayout);
    	scrollView.setVerticalScrollBarEnabled(false);
    	
    	LinearLayout LLayoutFI = new LinearLayout(this);
    	LLayoutFI.setOrientation(LinearLayout.VERTICAL);
    	RelativeLayout.LayoutParams RLPFI = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	
    	LLayoutFI.setLayoutParams(RLPFI);
    	
    	/****** FICHE *******/
    	TextView contenu = new TextView(this);
    	contenu.setText("Contenu de la fiche");
    	contenu.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	contenu.setTextColor(Color.parseColor("#3B170B"));
    	LLayoutFI.addView(contenu);
    	
    	/**** EDIT TEXT FICHE ****/
    	editContenu = new EditText(this);
    	Draw.setBackground(editContenu, getResources().getDrawable(R.drawable.backgroundwithborderfi));
    	
    	RelativeLayout.LayoutParams RLPEdit = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	RLPEdit.width = (int)(metrics.widthPixels/2);
    	editContenu.setY(10);
    	editContenu.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	editContenu.setLayoutParams(RLPEdit);
    	editContenu.setMinLines(15);
    	editContenu.setGravity(Gravity.TOP);
    	
    	/**** KEYWORDS ****/
    	LinearLayout LLayoutKeyWord = new LinearLayout(this);
    	LinearLayout.LayoutParams LLPProp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	LLPProp.setMargins(0, 20, 0, 0);
    	LLayoutKeyWord.setLayoutParams(LLPProp);
    	
    	TextView keyword = new TextView(this);
    	keyword.setText("Mots clefs : ");
    	keyword.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	keyword.setTextColor(Color.parseColor("#3B170B"));
    	LLayoutKeyWord.addView(keyword);
    	
    	/**** EDIT TEXT KEYWORDS ****/
    	editKeyword = new EditText(this);
    	Draw.setBackground(editKeyword, getResources().getDrawable(R.drawable.backgroundwithborderfi));
    	
    	editKeyword.setMinWidth((int)(metrics.widthPixels/2));
    	editKeyword.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	LLayoutKeyWord.addView(editKeyword);
    	
    	if(task.equals("modify"))
    	{
    		BddQube bddQube = new BddQube(this);
    		bddQube.open();
    		Qube fiche = bddQube.getQubeWithId(idQube);
    		editContenu.setText(fiche.getQuestion());
    		
    		if(fiche.getMotsClefs() != null && fiche.getMotsClefs().size() > 0)
    		{
    			ArrayList<String> arrayKeyword = fiche.getMotsClefs();
    			String res = "";
    			for(String kw : arrayKeyword)
    			{
    				if(!kw.equals(""))
    					res += kw+";";
    			}
    			
    			editKeyword.setText(res);
    		}
    		
    		bddQube.close();
    	}
    	LLayoutFI.addView(editContenu);
    	
    	LLayoutFI.addView(LLayoutKeyWord);
    	
    	/**** INFO KEYWORDS ****/
    	TextView infoKeyword = new TextView(this);
    	infoKeyword.setText("Séparez chaque mot clef par un point virgule.");
    	infoKeyword.setTextSize(TypedValue.COMPLEX_UNIT_SP, subtextSize);
    	infoKeyword.setTypeface(null, Typeface.BOLD_ITALIC);
    	
    	LLayoutFI.addView(infoKeyword);


    	LinearLayout.LayoutParams LLPButton = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	LLPButton.width = (int)(metrics.widthPixels/10);
    	int marginLeft = (int)(metrics.widthPixels/6);
    	int marginTop = (int)(metrics.heightPixels/20);
    	LLPButton.setMargins(marginLeft, marginTop, 0, 0);
    	
    	BotaButton validerButton = new BotaButton(this);
    	validerButton.setLayoutParams(LLPButton);
    	validerButton.setText("Valider");
    	validerButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, buttonSize);
    	Draw.setBackground(validerButton, getResources().getDrawable(R.drawable.backgroundbuttonaddnotion));
    	
    	validerButton.setOnClickListener(new OnClickListener() 
    	{	
			@Override
			public void onClick(View arg0) 
			{
				if(!editContenu.getText().toString().equals(""))
				{
					Draw.setBackground(editContenu, getResources().getDrawable(R.drawable.backgroundwithborder));
					
					BddQube bddQube = new BddQube(FicheInformation.this);
					bddQube.open();
					
					Qube fiche;
					if(task.equals("new"))
						fiche = new Qube();
					else
						fiche = bddQube.getQubeWithId(idQube);
					
					Editable res = editKeyword.getText();

					ArrayList<String> arrayRes = new ArrayList<String>();
					for(String kw : res.toString().split(";"))
					{
						arrayRes.add(kw);
					}
					
					fiche.setMotsClefs(arrayRes);
					
					fiche.setQuestion(editContenu.getText().toString());
					fiche.setProposition1("");
					fiche.setProposition2("");
					fiche.setProposition3("");
					fiche.setProposition4("");
					fiche.setNumReponse(-1);
					int numNextQube = bddQube.getNextNumQube(idLevel);
					fiche.setNumQube(numNextQube);
					fiche.setIdNiveau(idLevel);
					fiche.setScore(0);
					fiche.setEtat(Qube.QUESTION_NON_TRAITE);
					
					if(task.equals("new"))
						bddQube.insertQube(fiche);
					else
						bddQube.updateQube(idQube, fiche);
					
					bddQube.close();
					
					Intent intent = new Intent(FicheInformation.this, NiveauLayout.class);
					double param[] = new double[2]; // Param passé en paramètre contenant l'id de la notion et le niveau
					param[0] = idNotion; // ID de la notion
					param[1] = idLevel; // ID du niveau
					intent.putExtra("Param", param);
					startActivity(intent);
				}
				else
				{
					Draw.setBackground(editContenu, getResources().getDrawable(R.drawable.backgroundwithborderred));
					Toast toast = Toast.makeText(FicheInformation.this, "Veuillez insérer un contenu", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 200);
					toast.show();
				}
				
			}
		});
    	//LLayoutButton.addView(validerButton);
    	LLayoutFI.addView(validerButton);
    	scrollView.addView(LLayoutFI);
    	LLayoutGlobal.addView(scrollView);
    	
    	RLayout.addView(LLayoutGlobal);

    }
    
    private void initListener(){
    	editContenu.addTextChangedListener(new TextWatcher()
    	{
    		@Override
    		public void afterTextChanged(Editable s) 
    		{		
    		}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) 
			{	
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) 
			{	
				isModified = true;
			}
    	});
    	editKeyword.addTextChangedListener(new TextWatcher()
    	{
    		@Override
    		public void afterTextChanged(Editable s) 
    		{		
    		}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) 
			{	
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) 
			{	
				isModified = true;
			}
    	});
    }
    
	protected void onFinish()
	{
		Intent intent = new Intent(FicheInformation.this, NiveauLayout.class);
		double param[] = new double[2]; // Param passé en paramètre contenant l'id de la notion et le niveau
		param[0] = idNotion; // ID de la notion
		param[1] = idLevel; // ID du niveau
		intent.putExtra("Param", param);
		startActivity(intent);
		finish();
	}
}
