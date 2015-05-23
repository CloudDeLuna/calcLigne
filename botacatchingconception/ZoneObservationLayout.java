package com.example.botacatchingconception;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.db.bdd.BddBotacatching;
import com.example.db.bdd.BddLiaison;
import com.example.db.bdd.BddNiveau;
import com.example.db.bdd.BddNotion;
import com.example.db.bdd.BddQube;
import com.example.db.bdd.BddZoneObservation;
import com.example.db.object.Niveau;
import com.example.db.object.Notion;
import com.example.db.object.Qube;
import com.example.db.object.ZoneObservation;
import com.example.util.BotaButton;
import com.example.util.Draw;
import com.example.util.ExpandableListAdapter;
import com.example.util.GPSTracker;
import com.example.util.KeyboardListener;
import com.example.util.SizeCalculator;

public class ZoneObservationLayout extends RootActivity
{
	private RelativeLayout RLayout;
	private DisplayMetrics 	metrics;	
	private Uri			mImageUri;
	private File 		zoneObs;
	private double 		latitude;
	private double 		longitude;
	private double 		ratio;
	private GPSTracker 	gps;
	private String 		imageTmpName;
	private ImageView 	annotation;	
	private AutoCompleteTextView 					 autoCompleteTextView;
	private ScrollView 								 scroll;
	private ArrayList<ExpandableListAdapter> 		 adapterList;
	private ArrayList<ArrayList<ArrayList<Integer>>> idQubesList;
	private boolean 	canExitWithoutPrompt = true;
	
	public static ImageView imageZoneObs;
	public static BotaButton 	validationButton;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        clearTmpFile();
        
        gps = new GPSTracker(this);
        RLayout = new RelativeLayout(this);
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
		DragAndDropListener.dragAndDropEnable = false;
	   	canExitWithoutPrompt = true;

        autoCompleteTextView = new AutoCompleteTextView(this);
        
    	/**** TITLE ****/
    	TextView title = new TextView(this); 
    	title.setText("Gestion des zones d'observations");
    	float titleSize = SizeCalculator.getTextSizeFor(this, 0.07f);
    	title.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
    	title.setTextColor(Color.parseColor("#3B170B"));
    	RelativeLayout.LayoutParams RLPTitle = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	RLPTitle.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    	RLPTitle.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
    	title.setLayoutParams(RLPTitle);
    	RLPTitle.setMargins(0, 43, 0, 0);
    	RLayout.addView(title);
    	        
    	this.loadReturnButton();
    	this.loadSearchBar();
    	this.loadQuestions("");
    	
    	KeyboardListener.hideKeyboardListener(RLayout, this);
    	this.loadLayoutPicture();
    	
        setContentView(RLayout);
        
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{		
		if (resultCode == RESULT_OK) 
		{
			grabImage(imageZoneObs);
			canExitWithoutPrompt = false;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
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
    	params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
    	params.setMargins(50, 50, 0, 0);

    	returnButton.setLayoutParams(params);
    	Draw.setBackground(returnButton, getResources().getDrawable(R.drawable.backgroundbuttonexit));
    	RLayout.addView(returnButton);
    	
    	returnButton.setOnClickListener(new OnClickListener() 
    	{	
	        @Override
	        public void onClick(View v) 
	        {
	        	ZoneObservationLayout.this.onKeyDown(KeyEvent.KEYCODE_BACK, null);
	      	}
    	});	  	
    }
    
    private void loadSearchBar()
    {
    	LinearLayout LLayoutSearch = new LinearLayout(this);
    	LLayoutSearch.setOrientation(LinearLayout.HORIZONTAL);
    	
    	RelativeLayout.LayoutParams RLPSearchBar = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	RLPSearchBar.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
    	RLPSearchBar.setMargins(50, metrics.heightPixels/6, 0, 0);
    	
    	ImageView magnifyingGlass = new ImageView(this);
    	magnifyingGlass.setImageResource(R.drawable.magnifying_glass);
    	
    	magnifyingGlass.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View v) 
			{
				String text = autoCompleteTextView.getText().toString();
				BddQube bddQube = new BddQube(ZoneObservationLayout.this);
				bddQube.open();
				ArrayList<Qube> tabQube = bddQube.getQubeByKeyword(text);
				bddQube.close();
				
				if(tabQube == null || tabQube.size() == 0)
				{
					Toast toast = Toast.makeText(ZoneObservationLayout.this, "Aucun résultat pour votre recherche", Toast.LENGTH_SHORT);
					toast.show();
				}
				else
				{
					RLayout.removeView(scroll);
					ZoneObservationLayout.this.loadQuestions(text);
				}			
			}
		});
    	
    	BddQube bddQube = new BddQube(this);
    	bddQube.open();
    	ArrayList<String> allKeywords = bddQube.getAllKeywords();
    	bddQube.close();

    	final String[] motClefs = new String[allKeywords.size()];

    	for(int i = 0; i < allKeywords.size(); ++i)
    		motClefs[i] = allKeywords.get(i);
    	
    	autoCompleteTextView.setMinWidth(metrics.widthPixels/4);
    	autoCompleteTextView.setHint(R.string.search_bar);
    	autoCompleteTextView.setSelectAllOnFocus(true);
    	
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, motClefs);
    	autoCompleteTextView.setAdapter(adapter);
    	//autoCompleteTextView.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PHONETIC);
    	LLayoutSearch.addView(autoCompleteTextView);
    	LLayoutSearch.addView(magnifyingGlass);
    	
    	RLayout.addView(LLayoutSearch, RLPSearchBar);
    }
    
   /** Si keyword est vide affiche toutes les questions du parcours
    ** sinon affiche les questions qui ont pour mot clef keyword 
    ** afin de pouvoir leurs associers une zone d'observation
    **/ 
    private void loadQuestions(String keyword)
    {
        adapterList = new ArrayList<ExpandableListAdapter>();
        idQubesList = new ArrayList<ArrayList<ArrayList<Integer>>>();
        
    	/***********Init metrics for listAdapter****************/
		ExpandableListAdapter.setMetrics(metrics);

    	/**** LINEAR LAYOUT VERTICAL ****/
		LinearLayout LLayoutQubes = new LinearLayout(this);
    	LLayoutQubes.setOrientation(LinearLayout.VERTICAL);
    	LinearLayout.LayoutParams LLPQubes = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	LLayoutQubes.setLayoutParams(LLPQubes);
    	
    	/****VERTICAL SCROLLVIEW*****/
    	scroll = new ScrollView(this);
    	LinearLayout.LayoutParams scrollParam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	scrollParam.width  = metrics.widthPixels/5;
    	scrollParam.height  = metrics.heightPixels /3 * 2;
    	scroll.setVerticalScrollBarEnabled(true);
    	scroll.setX(10);
    	scroll.setY(metrics.heightPixels / 4);
    	scroll.setLayoutParams(scrollParam);
    	scroll.addView(LLayoutQubes);

    	
    	BddNotion bddNotion = new BddNotion(this);
    	BddNiveau bddNiveau = new BddNiveau(this);
    	BddQube bddQube = new BddQube(this);
    	bddNotion.open();
    	bddNiveau.open();
    	bddQube.open();
    	
    	/********************CHARGEMENT QUBES**********************/
    	if(keyword.equals(""))
    	{
	    	for(Notion notion : bddNotion.getAllNotions())
	    	{
	    		ArrayList<ArrayList<Integer>> idQubes = new ArrayList<ArrayList<Integer>>();
	        	/**** TEXT VIEW NOTION ****/
	        	TextView notionName = new TextView(this);
	        	notionName.setText(notion.getName() + " :");
	        	notionName.setTextColor(Color.parseColor("#3B170B"));
	        	notionName.setTextSize(15);
	        	float textSize = SizeCalculator.getTextSizeFor(this, 0.03f);
	        	notionName.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
	
	        	/****EXPANDABLE LIST*****/         	 
	        	final ExpandableListView expandableList = new ExpandableListView(this);
		    	final ArrayList<String> listDataHeader = new ArrayList<String>();
		    	HashMap<String, ArrayList<String>> listDataChild = new HashMap<String, ArrayList<String>>();
		    	
		    	ArrayList<Niveau> levels = bddNiveau.getNiveauxWithNotionId(notion.getId());
		    	
		    	if(levels != null)
		    	{
		    		int i = 0;
	
		    		for(Niveau niveau : levels)
			    	{
			    		idQubes.add(new ArrayList<Integer>());
			    		
			    		String header = "Niveau : " + niveau.getIdNotion() + "." + niveau.getNumNiveau();
			    		ArrayList<String> tmp = new ArrayList<String>();
			    		
			    		for(Qube qube : bddQube.getQubesWithNiveauId(niveau.getIdNiveau()))
			    		{
			    			if(qube.getNumReponse() != -1)
			    			{
			    				tmp.add(qube.getQuestion());
			    				idQubes.get(i).add(qube.getIdQube());
			    			}
			    		}
	
			    		if(!tmp.isEmpty())
			    		{
			    			listDataHeader.add(header);
			    			listDataChild.put(header, tmp);
				    		++i;
			    		}
			    	}
		    	}
		    	
		    	if(! listDataHeader.isEmpty())
		    	{
		    		final ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, scroll, listDataHeader, listDataChild);
			    	expandableList.setAdapter(listAdapter);
			    	
			    	final LinearLayout.LayoutParams expListLayout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		        	final int size = listDataHeader.size() * metrics.heightPixels/20;
		        	
		        	expListLayout.height = size;
			    	
			    	expandableList.setOnGroupExpandListener(new OnGroupExpandListener() 
			    	{
			    	    @Override
			    	    public void onGroupExpand(int groupPosition) 
			    	    {
			    	    	for(int i = 0; i < listDataHeader.size(); ++i)
			    	    		if(i != groupPosition) 
			    	    			expandableList.collapseGroup(i);
			    	    	
			    	    	expListLayout.height = size;
			    	    	expListLayout.height += listAdapter.getGroupHeight(groupPosition);
			    	    }
			    	});
	
			    	expandableList.setOnGroupCollapseListener(new OnGroupCollapseListener() 
			    	{
						@Override
						public void onGroupCollapse(int groupPosition) 
						{
							expListLayout.height = size;
			    	    }
					});
			    	
			    	expandableList.setLayoutParams(expListLayout);
			    	
			    	LLayoutQubes.addView(notionName);
			    	LLayoutQubes.addView(expandableList);
			    	
			    	adapterList.add(listAdapter);
			    	idQubesList.add(idQubes);
		    	}
	    	} 	
    	}
    	else
    	{
        	ArrayList<Qube> listeQubes = bddQube.getQubeByKeyword(keyword);
    		ArrayList<ArrayList<Integer>> idQubes = new ArrayList<ArrayList<Integer>>();

        	/****EXPANDABLE LIST*****/         	 
        	final ExpandableListView expandableList = new ExpandableListView(this);
	    	final ArrayList<String> listDataHeader = new ArrayList<String>();
	    	HashMap<String, ArrayList<String>> listDataChild = new HashMap<String, ArrayList<String>>();
    		idQubes.add(new ArrayList<Integer>());
    		
    		String header = "Résultat de la recherche : ";
    		ArrayList<String> tmp = new ArrayList<String>();
    		
    		for(Qube qube : listeQubes)
    		{
    			if(qube.getNumReponse() != -1)
    			{
    				tmp.add(qube.getQuestion());
    				idQubes.get(0).add(qube.getIdQube());
    			}
    		}

    		if(!tmp.isEmpty())
    		{
    			listDataHeader.add(header);
    			listDataChild.put(header, tmp);
    		}
    		
    		if(! listDataHeader.isEmpty())
	    	{
	    		final ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, scroll, listDataHeader, listDataChild);
		    	expandableList.setAdapter(listAdapter);
		    	
		    	final LinearLayout.LayoutParams expListLayout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	        	final int size = listDataHeader.size() * metrics.heightPixels/10;
	        	
	        	expListLayout.height = size;
	        	expListLayout.height += listAdapter.getChildrenCount(0) * metrics.heightPixels / 7;
	        	expandableList.expandGroup(0);
	        	
		    	expandableList.setOnGroupExpandListener(new OnGroupExpandListener() 
		    	{
		    	    @Override
		    	    public void onGroupExpand(int groupPosition) 
		    	    {
		    	    	for(int i = 0; i < listDataHeader.size(); ++i)
		    	    		if(i != groupPosition) 
		    	    			expandableList.collapseGroup(i);
		    	    	
		    	    	expListLayout.height = size;
		    	    	expListLayout.height += listAdapter.getGroupHeight(groupPosition);
		    	    }
		    	});

		    	expandableList.setOnGroupCollapseListener(new OnGroupCollapseListener() 
		    	{
					@Override
					public void onGroupCollapse(int groupPosition) 
					{
						expListLayout.height = size;
		    	    }
				});
		    	
		    	expandableList.setLayoutParams(expListLayout);
		    	
		    	LLayoutQubes.addView(expandableList);
		    	
		    	adapterList.add(listAdapter);
		    	idQubesList.add(idQubes);
	    	}
    	}
    	
    	RLayout.addView(scroll);
     
    	bddNotion.close();
    	bddNiveau.close();
    	bddQube.close();
    }
    
	private void loadLayoutPicture() 
	{
		/*******APERCU IMAGE********/
    	final LinearLayout.LayoutParams imgParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	imgParam.weight = 1;    
    	
    	final LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    	layoutParam.height = metrics.heightPixels/3 * 2;
    	layoutParam.width  = metrics.widthPixels/5*3;
    	layoutParam.gravity = Gravity.CENTER_HORIZONTAL;
    	
    	LinearLayout layout = new LinearLayout(this);
    	layout.setOrientation(LinearLayout.HORIZONTAL);
    	
    	imageZoneObs = new ImageView(this);
    	imageZoneObs.setAdjustViewBounds(true);
    	imageZoneObs.setMaxHeight(metrics.heightPixels/3 * 2);
    	imageZoneObs.setMaxWidth(metrics.widthPixels/5*3);
    	imageZoneObs.setLayoutParams(imgParam);
    	
        layout.setX(metrics.widthPixels/5);
        layout.setY(metrics.heightPixels / 4);
    	layout.setLayoutParams(layoutParam);
    	layout.addView(imageZoneObs);
    	RLayout.addView(layout);
    	
    	/**** LINEAR LAYOUT VERTICAL ****/
		final LinearLayout LLayoutAnnotation = new LinearLayout(this);
		LLayoutAnnotation.setOrientation(LinearLayout.VERTICAL);
    	LinearLayout.LayoutParams LLPAnnot = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	LLayoutAnnotation.setLayoutParams(LLPAnnot);
    	
    	/****VERTICAL SCROLLVIEW*****/
    	final ScrollView scroll = new ScrollView(this);
		LinearLayout.LayoutParams paramLayout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				   										LayoutParams.MATCH_PARENT);
		paramLayout.setMargins(0, 0, 50, 0);
		paramLayout.height = metrics.heightPixels/3 * 2;
		paramLayout.width = metrics.widthPixels/5;
    	scroll.setLayoutParams(paramLayout);
    	scroll.setVerticalScrollBarEnabled(true);
    	scroll.setX(metrics.widthPixels /5 * 4);
    	scroll.setY(metrics.heightPixels / 4);
    	scroll.addView(LLayoutAnnotation);
    	
    	float buttonSize = SizeCalculator.getTextSizeFor(this, 0.025f);
    	float textSize = SizeCalculator.getTextSizeFor(this, 0.025f);
    	
    	BotaButton pictureButton = new BotaButton(this);
    	pictureButton.setText("Prendre une photo");
    	pictureButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, buttonSize);
    	Draw.setBackground(pictureButton, getResources().getDrawable(R.drawable.backgroundbuttonaddnotion));
    	
    	pictureButton.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View v) 
			{
				try
		        {
		        	zoneObs = ZoneObservationLayout.this.createTemporaryFile("picture", ".jpg");
		        	imageTmpName = zoneObs.getAbsolutePath();

		        	zoneObs.delete();
		        }
		        catch(Exception e)
		        {
		            Toast.makeText(ZoneObservationLayout.this, "Impossible de créer l'image", Toast.LENGTH_SHORT).show();
		            return;
		        }
		        
				/**On remet l'annotation à sa place si elle n'y est pas**/
				if(validationButton.isEnabled() == true)
				{
					RLayout.removeView(annotation);
					annotation.setX(0);
					annotation.setY(0);
					annotation.setVisibility(View.VISIBLE);
					LLayoutAnnotation.addView(annotation, LLayoutAnnotation.getChildCount() - 1);
					validationButton.setEnabled(false);
					DragAndDropListener.firstTime = true;
				}
  
				DragAndDropListener.dragAndDropEnable = true;
		        mImageUri = Uri.fromFile(zoneObs);
		        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
		        startActivityForResult(intent, 0);		
			}
		});    	
    	LLayoutAnnotation.addView(pictureButton);
    	
    	/**** NAME ****/
    	TextView name = new TextView(this);
    	name.setText("Nom de la zone d'observation :");
    	name.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	name.setTextColor(Color.parseColor("#3B170B"));
    	LLayoutAnnotation.addView(name);
    	
    	/**** EDIT TEXT NAME ****/
    	final EditText editName = new EditText(this);
    	editName.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	Draw.setBackground(editName, getResources().getDrawable(R.drawable.backgroundwithborder));
    	this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
    	//PERMET DE PERDRE LE FOCUS SUR L'EDIT TEXT AU LANCEMENT DE L'ACTIVITY
    	LLayoutAnnotation.addView(editName);

    	
    	TextView annotationText = new TextView(this);
    	annotationText.setText("Annotation à placer :");
    	annotationText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	annotationText.setTextColor(Color.parseColor("#3B170B"));
    	annotationText.setGravity(Gravity.CENTER_HORIZONTAL);
    	LLayoutAnnotation.addView(annotationText);
    	
    	BitmapFactory.Options dimensions = new BitmapFactory.Options(); 
    	dimensions.inJustDecodeBounds = true;

    	@SuppressWarnings("unused")
		Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.annotation, dimensions);
    	final int height = dimensions.outHeight;
    	final int width =  dimensions.outWidth;
    	
    	annotation = new ImageView(this);
		annotation.setImageResource(R.drawable.annotation);

		/********DRAG & DROP***********/
		annotation.setOnTouchListener(new OnTouchListener() 
		{	
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					v.performClick();
					ClipData data = ClipData.newPlainText("", "");
					DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
					v.startDrag(data, shadowBuilder, v, 0);
					v.setVisibility(View.INVISIBLE);
					return false;
				}
				return false;
			}
		});
		DragAndDropListener listener = new DragAndDropListener(this, LLayoutAnnotation);
        RLayout.setOnDragListener(listener);          
        imageZoneObs.setOnDragListener(listener); 
        
        DragAndDropListener.firstTime = true;
        
		LLayoutAnnotation.addView(annotation);
    	
    	validationButton = new BotaButton(this);
    	validationButton.setEnabled(false);
    	validationButton.setText("Valider");
    	validationButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, buttonSize);
    	Draw.setBackground(validationButton, getResources().getDrawable(R.drawable.backgroundbuttonaddnotion));
    	validationButton.setOnClickListener(new OnClickListener() 
    	{
    		@Override
			public void onClick(View v) 
			{
				boolean localisationReady = ZoneObservationLayout.this.getLocalisation();
				
				if(localisationReady)
				{	
					if(editName.getText().length() != 0)
			    	{
						BddZoneObservation bddZone = new BddZoneObservation(ZoneObservationLayout.this);
				    	bddZone.open();
				    	ZoneObservation zone = bddZone.getZoneObservationWithName(editName.getText() + ".jpg");
				    	if(zone != null)
				    	{
				    		Draw.setBackground(editName, getResources().getDrawable(R.drawable.backgroundwithborderred));
				        	Toast.makeText(ZoneObservationLayout.this, "Ce nom est déjà pris", Toast.LENGTH_LONG).show();
				    	}
				    	else
				    	{
				    		Draw.setBackground(editName, getResources().getDrawable(R.drawable.backgroundwithborder));
				        	ArrayList<Integer> idQubesSelected = getIdQubesSelected();
				        	
				        	if(idQubesSelected.isEmpty())
				        	{
				        		Toast.makeText(ZoneObservationLayout.this, "Aucune questions séléctionnée", Toast.LENGTH_LONG).show();
				        	}
				        	else
				        	{
				        		/********Insertion des Zones d'observations dans la Bd ZoneObs et Liaison***********/
					        	double widthImg = imageZoneObs.getHeight() / ZoneObservationLayout.this.ratio;
								double heightImg = imageZoneObs.getHeight();
								double annotationX = (annotation.getX() + imageZoneObs.getWidth()/2 - imageZoneObs.getWidth()/10 + width/2) / widthImg;
					        	double annotationY = (annotation.getY() - metrics.heightPixels / 4 + height/2) / heightImg ;
					        	
					        	BddLiaison bddLiaison = new BddLiaison(ZoneObservationLayout.this);
					        	bddLiaison.open();
					        	
					        	String pathToImg = moveFileFromTempToImageDir(editName.getText().toString() + ".jpg");
					        	bddZone.insertZoneObservation(new ZoneObservation(0, pathToImg, longitude, latitude));
					        	zone = bddZone.getZoneObservationWithName(editName.getText().toString() + ".jpg");
					        
					        	for(int i = 0; i < idQubesSelected.size(); ++i)
					        		bddLiaison.insertLiaison(idQubesSelected.get(i), zone.getZoneId(), annotationX + ";" + annotationY);	
					    				        	
					        	bddLiaison.close();
					        	
					        	/****On remet tout à zéro*****/
								RLayout.removeView(annotation);
								annotation.setX(0);
								annotation.setY(0);
								LLayoutAnnotation.addView(annotation, LLayoutAnnotation.getChildCount() - 1);
								validationButton.setEnabled(false);
								DragAndDropListener.firstTime = true;
								DragAndDropListener.dragAndDropEnable = false;

								for(int i = 0; i < adapterList.size(); ++i)
									adapterList.get(i).resetCheckBoxs();
								
								editName.setText("");
								imageZoneObs.setImageDrawable(null);
								canExitWithoutPrompt = true;
								Toast.makeText(ZoneObservationLayout.this, "Insertion réussie", Toast.LENGTH_LONG).show();
				        	}
				    	}
				    	
				    	bddZone.close();
			    	}
					else
					{
						Draw.setBackground(editName, getResources().getDrawable(R.drawable.backgroundwithborderred));
						annotation.setVisibility(View.VISIBLE);
						Toast.makeText(ZoneObservationLayout.this, "Veuillez entrer un nom", Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					Toast.makeText(ZoneObservationLayout.this, "Recherche de GPS en cours ...\nVeuillez patienter", Toast.LENGTH_LONG).show();
				}
			}
    	});
    	
    	LLayoutAnnotation.addView(validationButton);
    	RLayout.addView(scroll);
	}
	
	private ArrayList<Integer> getIdQubesSelected() 
	{
		ArrayList<Integer> res = new ArrayList<Integer>();
    	
		for(int i = 0; i < idQubesList.size(); ++i)
    		for(int j = 0; j < idQubesList.get(i).size(); ++j)
    			for(int k = 0; k < idQubesList.get(i).get(j).size(); ++k)
    				if(adapterList.get(i).isChildSelected(j, k))
    					res.add(idQubesList.get(i).get(j).get(k));
		return res;
	}
    
	private boolean getLocalisation()
	{
		gps = new GPSTracker(this);
		
		if(gps.canGetLocalisation())
		{
			this.latitude = gps.getLatitude();
			this.longitude = gps.getLongitude();
			
			if(this.latitude == 0.0 && this.longitude == 0.0)
				return false;
			else
				return true;
		}
		else
        	GPSTracker.showGPSDisabledDial(ZoneObservationLayout.this);
		
		return false;
	}
	
	private void grabImage(ImageView imageView)
    {
        this.getContentResolver().notifyChange(mImageUri, null);
        ContentResolver cr = this.getContentResolver();
        Bitmap bitmap;
        try
        {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
			
            imageZoneObs.setImageBitmap(bitmap);
            this.ratio = bitmap.getHeight() / (double)bitmap.getWidth();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
            Log.d("", "Failed to load", e);
        }
    }
    
    private String moveFileFromTempToImageDir(String fileName) 
    {    	
        File imageDir = BddBotacatching.getFilesDir();
        imageDir = new File(imageDir.getAbsolutePath()+"/Image/");
        
        try 
        {           
            if (!imageDir.exists())
            	imageDir.mkdirs();

            InputStream in = new FileInputStream(imageTmpName);        
            OutputStream out = new FileOutputStream(imageDir.getAbsolutePath() + "/" + fileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) 
                out.write(buffer, 0, read);

            in.close();
            out.flush();
            out.close();
            in = null;
            out = null;

            //On supprime l'image du dossier Temp/
            new File(imageTmpName).delete();  
        } 
		catch (FileNotFoundException fnfe1) 
		{
			Log.e("tag", fnfe1.getMessage());
		}
        catch (Exception e) 
        {
            Log.e("tag", e.getMessage());
        }
        
        return imageDir.getAbsolutePath() + "/" + fileName;
    }
    
    private File createTemporaryFile(String part, String ext) throws Exception
    {
        File tempDir = BddBotacatching.getFilesDir();
        tempDir = new File(tempDir.getAbsolutePath()+"/Temp/");
        if(!tempDir.exists())
            tempDir.mkdirs();
        
        return File.createTempFile(part, ext, tempDir);
    }
    
    private void clearTmpFile()
    {
        File tempDir = BddBotacatching.getFilesDir();
        tempDir = new File(tempDir.getAbsolutePath()+"/Temp/");
        if(tempDir.exists())
        {
            for(String fileName : tempDir.list())
            {
            	File file = new File(tempDir.getAbsolutePath() + "/" + fileName) ;
            	file.delete();
            }
            
            tempDir.delete();
        }	
    }
    
    protected void onFinish() 
    {
    	ArrayList<Integer> res = getIdQubesSelected();
    	
    	if(!canExitWithoutPrompt || !res.isEmpty())
    	{
    		AlertDialog.Builder dial = new AlertDialog.Builder(ZoneObservationLayout.this);
	    	dial.setMessage("Voulez vous vraiment retourner au menu principal ?");
			
	    	dial.setPositiveButton("Oui", new DialogInterface.OnClickListener() 
			{
			   public void onClick(DialogInterface dialog, int id) 
			   {
			    	clearTmpFile();
			    	gps.stopLoctaionUpdate();
					DragAndDropListener.dragAndDropEnable = false;
					Intent intent = new Intent(ZoneObservationLayout.this, MainActivity.class);
					startActivity(intent);
			    	finish();
			   }
			});
			
	    	dial.setNegativeButton("Non", new DialogInterface.OnClickListener() 
			{
	           public void onClick(DialogInterface dialog, int id) 
	           {
	        	   dialog.dismiss();
	           }
	        }); 
			
	    	dial.show();
    	}
    	else
    	{
	    	clearTmpFile();
	    	gps.stopLoctaionUpdate();
			DragAndDropListener.dragAndDropEnable = false;			
			Intent intent = new Intent(ZoneObservationLayout.this, MainActivity.class);
			startActivity(intent);
	    	finish();	
    	}
    }
}
