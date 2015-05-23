package com.example.botacatchingconception;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.db.bdd.BddNiveau;
import com.example.db.bdd.BddNotion;
import com.example.db.bdd.BddParcours;
import com.example.db.bdd.BddPrerequis;
import com.example.db.bdd.BddQube;
import com.example.db.object.Niveau;
import com.example.db.object.Notion;
import com.example.db.object.Parcours;
import com.example.db.object.Prerequis;
import com.example.db.object.Qube;
import com.example.util.BotaButton;
import com.example.util.Draw;
import com.example.util.KeyboardListener;
import com.example.util.SizeCalculator;

/** Cette classe permet est liée à l'ajout d'un parcours et à son édition **/
public class ParcoursEditLayout extends RootActivity 
{
	private RelativeLayout RLayout;
	private HorizontalScrollView HSView;
	private LinearLayout LLayout;
	
	private int 			notionSelected;
	private final int 		idParcours = 1;
	private boolean 		isCreatingNewNotion = false;
	private DisplayMetrics 	metrics;
	
	private AutoCompleteTextView autoCompleteTextView;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        RLayout = new RelativeLayout(this);
        autoCompleteTextView = new AutoCompleteTextView(this);
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        loadReturnButton();
        loadSearchBar();
        loadParcours();
        loadButton();
        loadListeNotion();
        
        KeyboardListener.hideKeyboardListener(RLayout, this);
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

    private void loadSearchBar()
    {
    	LinearLayout LLayoutSearch = new LinearLayout(this);
    	LLayoutSearch.setOrientation(LinearLayout.HORIZONTAL);
    	
    	RelativeLayout.LayoutParams RLPSearchBar = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	RLPSearchBar.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    	
    	ImageView magnifyingGlass = new ImageView(this);
    	magnifyingGlass.setImageResource(R.drawable.magnifying_glass);
    	
    	magnifyingGlass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String text = autoCompleteTextView.getText().toString();
				BddQube bddQube = new BddQube(ParcoursEditLayout.this);
				bddQube.open();
				ArrayList<Qube> tabQube = bddQube.getQubeByKeyword(text);
				bddQube.close();
				
				if(tabQube == null || tabQube.size() == 0)
				{
					Toast toast = Toast.makeText(ParcoursEditLayout.this, "Aucun résultat pour votre recherche", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 200);
					toast.show();
				}
				else if(tabQube.size() == 1)
				{
					Log.v("un resultat", "un res");
					Intent intent = new Intent(ParcoursEditLayout.this, QubeEditLayout.class);
					double param[] = new double[3]; // Param passé en paramètre contenant l'id de la notion et le niveau
					BddNiveau bddNiveau = new BddNiveau(ParcoursEditLayout.this);
					bddNiveau.open();
					Niveau niveau = bddNiveau.getNiveauWithId(tabQube.get(0).getIdNiveau());
					
					param[0] = niveau.getIdNotion(); // ID de la notion
					param[1] = tabQube.get(0).getIdNiveau(); // ID du niveau
					param[2] = tabQube.get(0).getIdQube(); // ID du QUBE
					
					bddNiveau.close();
					intent.putExtra("Param", param);
					startActivity(intent);
					finish();
				}
				else
				{
					Log.v("plusieurs resultat", "plusieurs res");
					Intent intent = new Intent(ParcoursEditLayout.this, SearchResult.class);
					intent.putExtra("Param", text);
					startActivity(intent);
					finish();
				}
				
				
			}
		});
    	
    	BddQube bddQube = new BddQube(this);
    	bddQube.open();
    	ArrayList<String> allKeywords = bddQube.getAllKeywords();
    	final String[] motClefs = new String[allKeywords.size()];
    	
    	for(int i = 0; i < allKeywords.size(); ++i)
    			motClefs[i] = allKeywords.get(i);
    	
    	autoCompleteTextView.setMinWidth(metrics.widthPixels/4);
    	autoCompleteTextView.setHint(R.string.search_bar);
    	autoCompleteTextView.setSelectAllOnFocus(true);
    	
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, motClefs);
    	autoCompleteTextView.setAdapter(adapter);
    	
    	LLayoutSearch.addView(autoCompleteTextView);
    	LLayoutSearch.addView(magnifyingGlass);
    	bddQube.close();
    	
    	RLayout.addView(LLayoutSearch, RLPSearchBar);
    }
     
    private void loadParcours()
    {
    	/**** TITLE ****/
    	TextView title = new TextView(this);
    	title.setText("Parcours pédagogique");
    	float titleSize = SizeCalculator.getTextSizeFor(this, 0.07f);
    	title.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
    	title.setTextColor(Color.parseColor("#3B170B"));
    	
    	RelativeLayout.LayoutParams RLPTitle = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	RLPTitle.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    	RLPTitle.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
    	RLPTitle.setMargins(0, 50, 0, 0);
    	title.setLayoutParams(RLPTitle);
    	
    	RLayout.addView(title);
    	
    	/**** NAME ****/
    	LinearLayout LLayoutName = new LinearLayout(this);
    	RelativeLayout.LayoutParams RLPName = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	RLPName.setMargins(100, (int)(metrics.heightPixels/4.5), 0, 0);
    	LLayoutName.setLayoutParams(RLPName);
    	
    	float textSize = SizeCalculator.getTextSizeFor(this, 0.03f);
    	TextView name = new TextView(this);
    	name.setText("Nom du parcours : ");
    	name.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	name.setTextColor(Color.parseColor("#3B170B"));
    	
    	LLayoutName.addView(name);
    	
    	/**** EDIT TEXT NAME ****/
    	BddParcours bddParcours = new BddParcours(this);
    	bddParcours.open();
    	Parcours p = bddParcours.getParcoursWithId(1);
    	bddParcours.close();
    	
    	EditText editName = new EditText(this);
    	Draw.setBackground(editName, getResources().getDrawable(R.drawable.backgroundwithborder));
    	this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // PERMET DE PERDRE LE FOCUS SUR L'EDIT TEXT AU LANCEMENT DE L'ACTIVITY
    	
    	editName.setText(p.getName());
    	
    	editName.addTextChangedListener(new TextWatcher()
    	{
    		public void afterTextChanged(Editable s) 
    		{		
    			BddParcours bddParcours = new BddParcours(ParcoursEditLayout.this);
    	    	bddParcours.open();
    	    	Parcours p = bddParcours.getParcoursWithId(ParcoursEditLayout.this.idParcours);
    			p.setName(s.toString());
                bddParcours.updateParcours(ParcoursEditLayout.this.idParcours, p);
                bddParcours.close();
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
			}
    	});
    	
    	RelativeLayout.LayoutParams RLPEditName = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	RLPEditName.width = metrics.widthPixels / 5;

    	editName.setLayoutParams(RLPEditName);
    	
    	LLayoutName.addView(editName);
    	RLayout.addView(LLayoutName);
    }
    
    private void loadButton()
    {
    	LinearLayout layout = new LinearLayout(this);
    	layout.setOrientation(LinearLayout.HORIZONTAL);
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    	params.setMargins(0, metrics.widthPixels/10, metrics.widthPixels/10, 0);
    	layout.setLayoutParams(params);
    	
    	BotaButton newNotionButton = new BotaButton(this);
    	newNotionButton.setText("Ajouter une notion");
    	float textSize = SizeCalculator.getTextSizeFor(this, 0.030f);
    	newNotionButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	Draw.setBackground(newNotionButton, getResources().getDrawable(R.drawable.backgroundbuttonaddnotion));
    	
    	layout.addView(newNotionButton);
    	 
    	newNotionButton.setOnClickListener(new OnClickListener() 
    	{	
	        @Override
	        public void onClick(View v) 
	        {
	        	if(!isCreatingNewNotion)//Empeche de créer plusieurs nouvelles notions en même temps
	        		loadNewNotion();
	      	}
    	});
    	
    	RLayout.addView(layout);
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
	        	ParcoursEditLayout.this.onBackPressed();
	      	}
    	});	  	
    }
    
    private void loadListeNotion()
    {
    	/**** HORIZONTAL SCROLL VIEW ****/
    	this.HSView = new HorizontalScrollView(this);

    	HSView.setX(50);
    	HSView.setY(metrics.heightPixels/3);
    	
    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

    	HSView.setHorizontalScrollBarEnabled(false);
    	HSView.setVerticalScrollBarEnabled(false);
    	HSView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
    	HSView.setLayoutParams(params);
    	
    	/**** LINEAR LAYOUT ****/
    	this.LLayout = new LinearLayout(this);
    	LLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		LinearLayout.LayoutParams Lparams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				   LayoutParams.WRAP_CONTENT);
		
		LLayout.setLayoutParams(Lparams);
    	LLayout.setVerticalScrollBarEnabled(true);

		BddNotion bddNotion = new BddNotion(this);
		bddNotion.open();
		ArrayList<Notion> allNotions = bddNotion.getAllNotions();
		for(final Notion n : allNotions)
	    	LLayout.addView(createNotionView(n));
		
		bddNotion.close();

    	HSView.addView(LLayout);
    	RLayout.addView(HSView);
    }
    
	private ScrollView createNotionView(final Notion n)
    {
    	/**** LINEAR LAYOUT VERTICAL ****/
		final LinearLayout LLayoutNotion = new LinearLayout(this);
    	LLayoutNotion.setOrientation(LinearLayout.VERTICAL);
    	LinearLayout.LayoutParams LLPNotion = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	LLayoutNotion.setPadding(5, 5, 5, 5);
    	LLayoutNotion.setLayoutParams(LLPNotion);
    	
    	/****VERTICAL SCROLLVIEW*****/
    	final ScrollView scroll = new ScrollView(this);
		LinearLayout.LayoutParams paramLayout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				   										LayoutParams.MATCH_PARENT);
		paramLayout.setMargins(0, 0, 50, 0);
		paramLayout.height = (int) (metrics.heightPixels/3 * 1.90);
    	scroll.setLayoutParams(paramLayout);
    	scroll.setVerticalScrollBarEnabled(true);
    	scroll.addView(LLayoutNotion);
    	
    	/**** EDIT TEXT NOTION ****/
    	EditText editNotion = new EditText(this);
    	editNotion.setId(n.getId());
    	Draw.setBackground(editNotion, getResources().getDrawable(R.drawable.backgroundbordernamenotion));
    	float textSize = SizeCalculator.getTextSizeFor(this, 0.03f);
    	editNotion.setText(n.getName());
    	editNotion.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

    	editNotion.setOnTouchListener(new OnTouchListener() 
    	{			
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				notionSelected = v.getId();
				return false;
			}
		});
    	
    	editNotion.addTextChangedListener(new TextWatcher()
    	{
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
				BddNotion bddNotion = new BddNotion(ParcoursEditLayout.this);
    			bddNotion.open();
    			EditText editText = (EditText)findViewById(notionSelected);
    			
				if(bddNotion.getNotionWithName(arg0.toString()) != null)
				{
					Draw.setBackground(editText, getResources().getDrawable(R.drawable.backgroundwithborderred));
					Toast toast = Toast.makeText(ParcoursEditLayout.this, "Le nom existe déjà", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 200);
					toast.show();
				}
    			else
    			{
    				Draw.setBackground(editText, getResources().getDrawable(R.drawable.backgroundwithborder));
    				Notion n = bddNotion.getNotionWithId(notionSelected);
    				n.setName(arg0.toString());
	    			bddNotion.updateNotion(n.getId(), n);
    			}
    			bddNotion.close();
			}
    	});
    	
    	RelativeLayout.LayoutParams RLPEditNotion = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	RLPEditNotion.width = 300;
    	editNotion.setLayoutParams(RLPEditNotion);
    
    	RelativeLayout relativeLayout = new RelativeLayout(this);
    	relativeLayout.addView(editNotion);
    	
    	/*************CROIX SUPPRESSION****************/
		ImageView deleteCross = new ImageView(this);
		deleteCross.setImageResource(R.drawable.croix_fermer_bleu);

		deleteCross.setOnClickListener(new OnClickListener() 
    	{	
	        @Override
	        public void onClick(final View v) 
	        {	        	
	    		AlertDialog.Builder dial = new AlertDialog.Builder(ParcoursEditLayout.this);
	        	dial.setMessage("Voulez-vous vraiment supprimer cette notion ?");
	    		
	        	dial.setPositiveButton("Valider", new DialogInterface.OnClickListener() 
	    		{
	    		   public void onClick(DialogInterface dialog, int id) 
	    		   {
			        	BddNotion bddNotion = new BddNotion(ParcoursEditLayout.this);
			        	BddNiveau bddNiveau = new BddNiveau(ParcoursEditLayout.this);
			        	BddPrerequis bddPrerequis = new BddPrerequis(ParcoursEditLayout.this);
			        	bddNotion.open();
			        	bddNiveau.open();
			        	bddPrerequis.open();
			        	
			        	bddPrerequis.removeAllPrerequisOf(n.getId());
			        	bddPrerequis.removeAllPrerequisWithIdAsPere(n.getId());
			        	bddNiveau.removeNiveauWithNotionId(n.getId());
			        	bddNotion.removeNotionWithID(n.getId());	        	
			        	
			        	bddNotion.close();
			    		bddNiveau.close();
			    		bddPrerequis.close();
			 
			        	LLayout.removeView((View) v.getParent().getParent().getParent());
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
		RelativeLayout.LayoutParams lpcroix = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lpcroix.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lpcroix.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		relativeLayout.addView(deleteCross, lpcroix);
    	LLayoutNotion.addView(relativeLayout);
    	
    	/**** CHARGEMENT DES NIVEAUX ****/
    	BddNiveau bddNiveau = new BddNiveau(this);
    	bddNiveau.open();
    	ArrayList<Niveau> allNiveaux = bddNiveau.getNiveauxWithNotionId(n.getId());
    	
    	if(allNiveaux.size() > 0)
    	{
    		for(final Niveau level : allNiveaux)
	    	{
    			BotaButton b = new BotaButton(this);
	    		b.setText("Niveau : "+level.getNumNiveau());
	    		b.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
	    		Draw.setBackground(b, getResources().getDrawable(R.drawable.backgroundbuttonlevel));
	    		LLayoutNotion.addView(b);
	    		
	    		b.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(ParcoursEditLayout.this, NiveauLayout.class);
						double param[] = new double[2]; // Param passé en paramètre contenant l'id de la notion et le niveau
						param[0] = n.getId(); // ID de la notion
						param[1] = level.getIdNiveau(); // ID du niveau
						intent.putExtra("Param", param);
						startActivity(intent);
						finish();
					}		
	    		});
	    	}
    	}
    	
    	BotaButton b = new BotaButton(this);
    	b.setText("Ajouter un niveau");
    	b.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	Draw.setBackground(b, getResources().getDrawable(R.drawable.backgroundbuttonprerequis));
    	b.setOnClickListener(new OnClickListener() 
    	{	
	        @Override
	        public void onClick(View v) 
	        {
	        	BddNiveau bddNiveau = new BddNiveau(ParcoursEditLayout.this);
	        	bddNiveau.open();
	        	
	        	int nbLevel = bddNiveau.getNiveauxWithNotionId(n.getId()).size();
	    		final long lvlId = bddNiveau.insertNiveau(new Niveau(0, nbLevel + 1, false, false, 0, 0, false, n.getId()));
	    		
	    		BotaButton b = new BotaButton(ParcoursEditLayout.this);
	    		b.setText("Niveau : " + (nbLevel + 1));
	    		float bSize = SizeCalculator.getTextSizeFor(ParcoursEditLayout.this, 0.03f);
	        	b.setTextSize(TypedValue.COMPLEX_UNIT_SP, bSize);
	    		Draw.setBackground(b, getResources().getDrawable(R.drawable.backgroundbuttonlevel));
	    		b.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(ParcoursEditLayout.this, NiveauLayout.class);
						double param[] = new double[2]; // Param passé en paramètre contenant l'id de la notion et le niveau
						param[0] = n.getId(); // ID de la notion
						param[1] = lvlId; // ID du niveau
						intent.putExtra("Param", param);
						startActivity(intent);
					}		
	    		});
	    		
	    		LLayoutNotion.addView(b, nbLevel + 1);
	    		bddNiveau.close();
	      	}
    	});
    	
    	LLayoutNotion.addView(b);
    	
    	BotaButton prerequis = new BotaButton(this);
    	prerequis.setText("Changer prérequis");
    	prerequis.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	Draw.setBackground(prerequis, getResources().getDrawable(R.drawable.backgroundbuttonprerequis));
    	LLayoutNotion.addView(prerequis);
    	prerequis.setOnClickListener(new OnClickListener() 
    	{	
	        @Override
	        public void onClick(View v) 
	        {
	        	BddNotion bddNot = new BddNotion(ParcoursEditLayout.this);
	        	bddNot.open();
	        	int index = LLayout.indexOfChild((View) v.getParent().getParent());
	        	LLayout.removeViewAt(index);
	        	loadPrerequis(bddNot.getNotionWithId(n.getId()), index, scroll);
	        	bddNot.close();
	      	}
    	});
    	bddNiveau.close();
    	return scroll;
    }
    
    private void loadNewNotion()
    {
    	isCreatingNewNotion = true;
    	/**** LINEAR LAYOUT VERTICAL ****/
    	LinearLayout LLayoutNotion = new LinearLayout(this);
    	LLayoutNotion.setOrientation(LinearLayout.VERTICAL);
    	
    	LinearLayout.LayoutParams LLPNotion = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	LLPNotion.setMargins(0, 0, 70, 0);
    	LLayoutNotion.setLayoutParams(LLPNotion);
    	
    	/****VERTICAL SCROLLVIEW*****/
       	final ScrollView scroll = new ScrollView(this);
		LinearLayout.LayoutParams paramLayout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				   										LayoutParams.MATCH_PARENT);
		paramLayout.setMargins(0, 0, 50, 0);
		paramLayout.height = metrics.heightPixels/3 * 2;
    	scroll.setLayoutParams(paramLayout);
    	scroll.setVerticalScrollBarEnabled(true);
    	scroll.addView(LLayoutNotion);
    	
    	/**** EDIT TEXT NOTION ****/
    	final EditText editNotion = new EditText(this);
    	Draw.setBackground(editNotion, getResources().getDrawable(R.drawable.backgroundwithborder));
    	editNotion.setText("Nouvelle Notion"); // Mettre variable static
    	editNotion.setSelectAllOnFocus(true);
    	Draw.setBackground(editNotion, getResources().getDrawable(R.drawable.backgroundbordernamenotion));
    	RelativeLayout.LayoutParams RLPEditNotion = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	RLPEditNotion.width = 250;

    	editNotion.setLayoutParams(RLPEditNotion);
    	
    	/**** TEXT VIEW PREREQUIS ****/
    	TextView prerequis = new TextView(this);
    	prerequis.setText("Prérequis : ");
    	prerequis.setTextSize(15);
    	prerequis.setTextColor(Color.parseColor("#3B170B"));
    	
    	LinearLayout.LayoutParams LLPPrerequis = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	LLPPrerequis.setMargins(0, 30, 0, 20);
    	prerequis.setLayoutParams(LLPPrerequis);
    	
    	LLayoutNotion.addView(editNotion);
    	LLayoutNotion.addView(prerequis);

    	/**** CHARGEMENT CHECKBOX ****/
    	CheckBox checkBox;
    	BddNotion bddNotion = new BddNotion(this);
    	bddNotion.open();
    	
    	final ArrayList<CheckBox> checkBoxs = new ArrayList<CheckBox>();
    	
    	for(Notion notion : bddNotion.getAllNotions())
    	{
    		checkBox = new CheckBox(this);
    		checkBox.setText(notion.getName());
    		checkBox.setTextSize(15);
    		checkBox.setTextColor(Color.parseColor("#3B170B"));
    		checkBoxs.add(checkBox);
    		LLayoutNotion.addView(checkBox);
    	}
    	
    	BotaButton validationButton = new BotaButton(this);
    	validationButton.setText("Valider");
    	validationButton.setTextSize(15);
    	Draw.setBackground(validationButton, getResources().getDrawable(R.drawable.backgroundbuttonprerequis));
    	validationButton.setOnClickListener(new OnClickListener() 
    	{	
	        @Override
	        public void onClick(View v)
	        {
	        	/***INSERE LA NOUVELLE NOTION ET LES PREREQUIS DANS LA BD***/
	        	BddNotion bddNotion = new BddNotion(ParcoursEditLayout.this);
	        	bddNotion.open();     
	        	
	    		if(bddNotion.getNotionWithName(editNotion.getText().toString()) == null)
	        	{
	        		BddPrerequis bddPrerequis = new BddPrerequis(ParcoursEditLayout.this);
		        	bddPrerequis.open();
	        	     		
	        		Draw.setBackground(editNotion, getResources().getDrawable(R.drawable.backgroundwithborder));
	        		bddNotion.insertNotion(new Notion(0,editNotion.getText().toString(), ParcoursEditLayout.this.idParcours));
	        		Notion n = bddNotion.getNotionWithName(editNotion.getText().toString());  
	        		Notion nPrerequis;
	        		
	        		for(CheckBox box : checkBoxs)
	        		{
						nPrerequis = bddNotion.getNotionWithName(box.getText().toString());
						
						if(box.isChecked())
							bddPrerequis.insertPrerequis(n.getId(), nPrerequis.getId());
						else
							bddPrerequis.removePrerequis(n.getId(), nPrerequis.getId());    		    		  
	        		}
	        		
	        		bddNotion.close();
	        		bddPrerequis.close();
	        		
	        		LLayout.removeView(scroll);
	        		ScrollView newLayoutNotion = createNotionView(n);
	    	    	LLayout.addView(newLayoutNotion);
	    	    	isCreatingNewNotion = false;//Permet de pouvoir créer une autre notion
	        	}
	        	else
	        	{
					Draw.setBackground(editNotion, getResources().getDrawable(R.drawable.backgroundwithborderred));
					Toast toast = Toast.makeText(ParcoursEditLayout.this, "Le nom existe déjà", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 200);
					toast.show();
	        	}
	      	}
    	});
    	
    	BotaButton annulationButton = new BotaButton(this);
    	annulationButton.setText("Annuler");
    	annulationButton.setTextSize(15);
    	Draw.setBackground(annulationButton, getResources().getDrawable(R.drawable.backgroundbuttonprerequis));
    	
    	annulationButton.setOnClickListener(new OnClickListener() 
    	{	
	        @Override
	        public void onClick(View v) 
	        {
        		LLayout.removeView(scroll);
    	    	isCreatingNewNotion = false;//Permet de pouvoir créer une autre notion
	      	}
    	});
    	
    	bddNotion.close();
    	LLayoutNotion.addView(validationButton);
    	LLayoutNotion.addView(annulationButton);
    	LLayout.addView(scroll);
    }//Fin loadNewNotion()
        
	private void loadPrerequis(final Notion not, final int index, final View view)
    {
    	/**** LINEAR LAYOUT VERTICAL ****/
    	final LinearLayout LLayoutNotion = new LinearLayout(this);
    	LLayoutNotion.setOrientation(LinearLayout.VERTICAL);
    	
    	LinearLayout.LayoutParams LLPNotion = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	LLPNotion.setMargins(0, 0, 70, 0);
    	LLayoutNotion.setLayoutParams(LLPNotion);
    	
    	/****VERTICAL SCROLLVIEW*****/
       	final ScrollView scroll = new ScrollView(this);
		LinearLayout.LayoutParams paramLayout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				   										LayoutParams.MATCH_PARENT);
		paramLayout.setMargins(0, 0, 50, 0);
		paramLayout.height = getResources().getDisplayMetrics().heightPixels/3 * 2;
    	scroll.setLayoutParams(paramLayout);
    	scroll.setVerticalScrollBarEnabled(true);
    	scroll.addView(LLayoutNotion);
    	
    	/**** TEXT VIEW PREREQUIS ****/
    	TextView prerequis = new TextView(this);
    	prerequis.setText("Prérequis " + not.getName() + " :");
    	float prerequisSize = SizeCalculator.getTextSizeFor(ParcoursEditLayout.this, 0.03f);
    	prerequis.setTextSize(TypedValue.COMPLEX_UNIT_SP, prerequisSize);
    	prerequis.setTextColor(Color.parseColor("#3B170B"));
    	
    	LinearLayout.LayoutParams LLPPrerequis = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	LLPPrerequis.setMargins(0, 30, 0, 20);
    	prerequis.setLayoutParams(LLPPrerequis);
    	
    	LLayoutNotion.addView(prerequis);

    	/**** CHARGEMENT CHECKBOX ****/
    	CheckBox checkBox;
    	BddNotion bddNotion = new BddNotion(this);
    	bddNotion.open();
    	BddPrerequis bddPrerequis = new BddPrerequis(ParcoursEditLayout.this);
    	bddPrerequis.open();
    	
    	Prerequis pre = bddPrerequis.getPrerequisWithId(not.getId());
    	ArrayList<Integer> notionsPrerequises = new ArrayList<Integer>();

    	if(pre != null)
    		notionsPrerequises = pre.getNotionsPrerequises();

    	final ArrayList<CheckBox> checkBoxs = new ArrayList<CheckBox>();
    	
    	for(Notion notion : bddNotion.getAllNotions())
    	{
    		if(notion.getId() == not.getId())
    			continue;
    		
    		checkBox = new CheckBox(this);
    		checkBox.setText(notion.getName());
    		checkBox.setTextColor(Color.parseColor("#3B170B"));
    		float checkboxSize = SizeCalculator.getTextSizeFor(ParcoursEditLayout.this, 0.03f);
    		checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, checkboxSize);

    		if(notionsPrerequises.contains(notion.getId()))
    			checkBox.setChecked(true);
    		
    		checkBoxs.add(checkBox);
    		LLayoutNotion.addView(checkBox);
    	}

    	BotaButton validationButton = new BotaButton(this);
    	validationButton.setText("Valider");
    	validationButton.setTextSize(15);
    	Draw.setBackground(validationButton, getResources().getDrawable(R.drawable.backgroundbuttonprerequis));
    	
    	validationButton.setOnClickListener(new OnClickListener() 
    	{	
	        @Override
	        public void onClick(View v) 
	        {
	        	/*****MET A JOUR LES PREREQUIS DANS LA BD*******/
	        	BddNotion bddNotion = new BddNotion(ParcoursEditLayout.this);
	        	BddPrerequis bddPrerequis = new BddPrerequis(ParcoursEditLayout.this);
	        	bddNotion.open();
	        	bddPrerequis.open();
	        	boolean isValidable = true;
	        	boolean doNotHavePrerequis = true;
	        	Notion n = bddNotion.getNotionWithName(not.getName());
        	
	        	for(CheckBox box : checkBoxs)
        		{    				
        			if(box.isChecked())
    		    	{
        				doNotHavePrerequis = false;
						Notion nPrerequis = bddNotion.getNotionWithName(box.getText().toString());
						if(nPrerequis != null)
						{
							if(bddPrerequis.isPrerequisExists(nPrerequis.getId(), n.getId()))
							{
								Draw.setBackground(box, getResources().getDrawable(R.drawable.backgroundwithborderred));
								isValidable = false;
							}
							else
								bddPrerequis.insertPrerequis(n.getId(), nPrerequis.getId());
						}
    		    	}
    		    	else
    		    	{
    		    		Notion nPrerequis = bddNotion.getNotionWithName(box.getText().toString());
						Draw.setBackground(box, null);
    		    		
						if(nPrerequis != null)
    		    			bddPrerequis.removePrerequis(n.getId(), nPrerequis.getId());    		    		  
    		    	}	        			
        		}
        		
        		bddNotion.close();
        		bddPrerequis.close();
        		
        		if(isValidable)
        		{
        			if(doNotHavePrerequis)
        			{
        				//TODO Mettre au début ?
        			}
        			
        			LLayout.removeView(scroll);
	        		LLayout.addView(view, index);
	        	}
        		else
        		{
					Toast toast = Toast.makeText(ParcoursEditLayout.this, "Ces notions ont déjà pour prérequis la notion "  
																		+ n.getName(), Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 200);
					toast.show();
        		}
	      	}
    	});
    	
    	BotaButton annulationButton = new BotaButton(this);
    	annulationButton.setText("Annuler");
    	annulationButton.setTextSize(15);
    	Draw.setBackground(annulationButton, getResources().getDrawable(R.drawable.backgroundbuttonprerequis));
    	annulationButton.setOnClickListener(new OnClickListener() 
    	{	
	        @Override
	        public void onClick(View v) 
	        {
	        	LLayout.removeView(scroll);
	        	LLayout.addView(view, index);
	      	}
    	});
    	    	
    	bddPrerequis.close();
    	bddNotion.close();
    	
    	LLayoutNotion.addView(validationButton);
    	LLayoutNotion.addView(annulationButton);
    	LLayout.addView(scroll, index);
    }//Fin loadPrerequis()
    
	protected void onFinish()
	{
		Intent intent = new Intent(ParcoursEditLayout.this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
