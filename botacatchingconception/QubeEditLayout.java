package com.example.botacatchingconception;

import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.db.bdd.BddLiaison;
import com.example.db.bdd.BddQube;
import com.example.db.bdd.BddZoneObservation;
import com.example.db.object.Liaison;
import com.example.db.object.Qube;
import com.example.db.object.ZoneObservation;
import com.example.util.BotaButton;
import com.example.util.Draw;
import com.example.util.SizeCalculator;

public class QubeEditLayout extends RootActivity 
{
	private RelativeLayout RLayout;
	private RelativeLayout apercuImg;
	private DisplayMetrics metrics;
	
	private BotaButton returnButton;
	private BotaButton validButton;
	
	private RadioButton r_one;
	private RadioButton r_two;
	private RadioButton r_three;
	private RadioButton r_four;
	
	private boolean isModified;
	
	private EditText editQuestion;
	private EditText editProp1;
	private EditText editProp2;
	private EditText editProp3;
	private EditText editProp4;
	private EditText editKeyword;
	
	private int idNotion;
	private int idLevel;
	private int idQube;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        isModified = false;
        RLayout = new RelativeLayout(this);
        apercuImg = new RelativeLayout(this);
        
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
		initRadioButton();
        loadView(task);
        initListener();
        setContentView(RLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    
    private void loadReturnButton()
    {
     	returnButton = new BotaButton(this);
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
	        		AlertDialog.Builder dial = new AlertDialog.Builder(QubeEditLayout.this);
		        	dial.setMessage("Vous perdrez toutes vos modifications, êtes-vous sur de vouloir continuer?");
		    		
		        	dial.setPositiveButton("Valider", new DialogInterface.OnClickListener() 
		    		{
		    		   public void onClick(DialogInterface dialog, int id) 
		    		   {
				        	dialog.dismiss();
				        	QubeEditLayout.this.onBackPressed();
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
	        		QubeEditLayout.this.onBackPressed();
	        	
	      	}
    	});	  	
    }
    
    private void initRadioButton()
    {
    	r_one = new RadioButton(this);
    	r_two = new RadioButton(this);
    	r_three = new RadioButton(this);
    	r_four = new RadioButton(this);
    	
    	r_one.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				r_one.setChecked(true);
				r_two.setChecked(false);
				r_three.setChecked(false);
				r_four.setChecked(false);				
			}   		
    	});
    	
    	r_two.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				r_one.setChecked(false);
				r_two.setChecked(true);
				r_three.setChecked(false);
				r_four.setChecked(false);				
			}	
    	});
    	
    	r_three.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				r_one.setChecked(false);
				r_two.setChecked(false);
				r_three.setChecked(true);
				r_four.setChecked(false);	
			}	
    	});
    	
    	r_four.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				r_one.setChecked(false);
				r_two.setChecked(false);
				r_three.setChecked(false);
				r_four.setChecked(true);		
			} 		
    	});
    }
    
    private void loadView(final String task)
    {
    	float buttonSize = SizeCalculator.getTextSizeFor(this, 0.025f);
    	float textSize = SizeCalculator.getTextSizeFor(this, 0.023f);
    	float subtextSize = SizeCalculator.getTextSizeFor(this, 0.02f);
    	float titleSize = SizeCalculator.getTextSizeFor(this, 0.07f);
    	
    	/**** TITLE ****/ 
		TextView title = new TextView(this);
		String text;
		if(task == "new")
			text = "Creation d'une Question";
		else
			text = "Modification d'une Question";
		
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
    	RelativeLayout.LayoutParams RLPGlobal = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	RLPGlobal.setMargins(100, 150, 0, 0);
    	LLayoutGlobal.setLayoutParams(RLPGlobal);
    	
    	/****** VIEW QUBE *****/
    	ScrollView scrollView = new ScrollView(this);
    	LinearLayout LLayoutQube = new LinearLayout(this);
    	LLayoutQube.setOrientation(LinearLayout.VERTICAL);
    	RelativeLayout.LayoutParams RLPQube = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	LLayoutQube.setLayoutParams(RLPQube);
    	
    	/****** QUESTION *******/
    	LinearLayout LLayoutQuestion = new LinearLayout(this);
    	RelativeLayout.LayoutParams RLPQuestion = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	RLPQuestion.setMargins(100, 100, 0, 0);
    	LLayoutQuestion.setLayoutParams(RLPQuestion);
    	
    	TextView entrerQuestion = new TextView(this);
    	entrerQuestion.setText("Entrer la question : ");
    	entrerQuestion.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	entrerQuestion.setTextColor(Color.parseColor("#3B170B"));
    	LLayoutQuestion.addView(entrerQuestion);
    	
    	/**** EDIT TEXT QUESTION ****/
    	
    	editQuestion = new EditText(this);
    	Draw.setBackground(editQuestion, getResources().getDrawable(R.drawable.backgroundwithborder));
    	
    	RelativeLayout.LayoutParams RLPEdit = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	RLPEdit.width = 400;
    	RLPEdit.setMargins(50, 50, 50, 50);
    	editQuestion.setLayoutParams(RLPEdit);
    	editQuestion.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	
    	LLayoutQuestion.addView(editQuestion);
    	LLayoutQube.addView(LLayoutQuestion);
	
    	RLPEdit.width = 200;
    	/****** PROPOSITIONS1 *******/
    	
    	LinearLayout LLayoutProp1 = new LinearLayout(this);
    	LinearLayout.LayoutParams LLPProp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	LLPProp.setMargins(0, 20, 0, 0);
    	LLayoutProp1.setLayoutParams(LLPProp);
    	
    	
    	TextView prop1 = new TextView(this);
    	prop1.setText("Première proposition : ");
    	prop1.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	prop1.setTextColor(Color.parseColor("#3B170B"));
    	
    	LLayoutProp1.addView(prop1);
    	
    	/**** EDIT TEXT PROP1 ****/	
    	editProp1 = new EditText(this);
    	Draw.setBackground(editProp1, getResources().getDrawable(R.drawable.backgroundwithborder));

    	editProp1.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	editProp1.setLayoutParams(RLPEdit);
    	editProp1.setX(10);
    	LLayoutProp1.addView(editProp1);
    	r_one.setX(10);
    	LLayoutProp1.addView(r_one);
    	LLayoutQube.addView(LLayoutProp1);
    	
    	/****** PROPOSITIONS2 *******/
    	LinearLayout LLayoutProp2 = new LinearLayout(this);
    	LLayoutProp2.setLayoutParams(LLPProp);
    	
    	TextView prop2 = new TextView(this);
    	prop2.setText("Deuxième proposition : ");
    	prop2.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	prop2.setTextColor(Color.parseColor("#3B170B"));
    	LLayoutProp2.addView(prop2);
    	
    	/**** EDIT TEXT PROP2 ****/	
    	
    	editProp2 = new EditText(this);
    	Draw.setBackground(editProp2, getResources().getDrawable(R.drawable.backgroundwithborder));
    	editProp2.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	editProp2.setLayoutParams(RLPEdit);
    	editProp2.setX(4);
    	
    	
    	LLayoutProp2.addView(editProp2);
    	r_two.setX(3);
    	LLayoutProp2.addView(r_two);
    	
    	LLayoutQube.addView(LLayoutProp2);
    	
    	/****** PROPOSITIONS3 *******/
    	LinearLayout LLayoutProp3 = new LinearLayout(this);
    	LLayoutProp3.setLayoutParams(LLPProp);
    	
    	TextView prop3 = new TextView(this);
    	prop3.setText("Troisième proposition : ");
    	prop3.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	prop3.setTextColor(Color.parseColor("#3B170B"));
    	LLayoutProp3.addView(prop3);
    	
    	/**** EDIT TEXT PROP3 ****/	
    	editProp3 = new EditText(this);
    	Draw.setBackground(editProp3, getResources().getDrawable(R.drawable.backgroundwithborder));
    	editProp3.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	editProp3.setLayoutParams(RLPEdit);
    	editProp3.setX(5);
    	
    	LLayoutProp3.addView(editProp3);
    	r_three.setX(4);
    	LLayoutProp3.addView(r_three);
    	
    	LLayoutQube.addView(LLayoutProp3);
    	
    	/****** PROPOSITIONS4 *******/
    	LinearLayout LLayoutProp4 = new LinearLayout(this);
    	LLayoutProp4.setLayoutParams(LLPProp);
    	
    	TextView prop4 = new TextView(this);
    	prop4.setText("Quatrième proposition : ");
    	prop4.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	prop4.setTextColor(Color.parseColor("#3B170B"));
    	LLayoutProp4.addView(prop4);
    	
    	/**** EDIT TEXT PROP4 ****/	
    	editProp4 = new EditText(this);
    	Draw.setBackground(editProp4, getResources().getDrawable(R.drawable.backgroundwithborder));
    	editProp4.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	editProp4.setLayoutParams(RLPEdit);
    	
    	
    	LLayoutProp4.addView(editProp4);
    	LLayoutProp4.addView(r_four);
    	
    	LLayoutQube.addView(LLayoutProp4);
    	

    	/**** KEYWORDS ****/
    	LinearLayout LLayoutKeyWord = new LinearLayout(this);
    	LLayoutKeyWord.setLayoutParams(LLPProp);
    	
    	TextView keyword = new TextView(this);
    	keyword.setText("Mots clefs : ");
    	keyword.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	keyword.setTextColor(Color.parseColor("#3B170B"));
    	LLayoutKeyWord.addView(keyword);
    	
    	/**** EDIT TEXT KEYWORDS ****/
    	editKeyword = new EditText(this);
    	Draw.setBackground(editKeyword, getResources().getDrawable(R.drawable.backgroundwithborder));
    	editKeyword.setLayoutParams(RLPEdit);
    	editKeyword.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    	LLayoutKeyWord.addView(editKeyword);
    	
    	LLayoutQube.addView(LLayoutKeyWord);
    	
    	/**** INFO KEYWORDS ****/
    	TextView infoKeyword = new TextView(this);
    	infoKeyword.setText("Séparez chaque mot clef par un point virgule.");
    	infoKeyword.setTextSize(TypedValue.COMPLEX_UNIT_SP, subtextSize);
    	infoKeyword.setTypeface(null, Typeface.BOLD_ITALIC);
    	
    	LLayoutQube.addView(infoKeyword);

    	LinearLayout LLayoutButton = new LinearLayout(this);
    	LLayoutKeyWord.setLayoutParams(LLPProp);
    	validButton = new BotaButton(this);
    	validButton.setText("Valider");
    	validButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, buttonSize);
    	Draw.setBackground(validButton, getResources().getDrawable(R.drawable.backgroundbuttonaddnotion));

    	LinearLayout.LayoutParams LLPButton = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    	LLPButton.width = (int)(metrics.widthPixels/10);
    	int marginLeft = (int)(metrics.widthPixels/8);
    	int marginTop = (int)(metrics.heightPixels/8);
    	LLPButton.setMargins(marginLeft, marginTop, 0, 0);

    	validButton.setLayoutParams(LLPButton);
    	
    	validButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String textQuestion = editQuestion.getText().toString();
				int nbProp = 0;
				
				String textProp1 = editProp1.getText().toString();
				if(!textProp1.equals(""))
					++nbProp;
				
				String textProp2 = editProp2.getText().toString();
				if(!textProp2.equals(""))
					++nbProp;
				
				String textProp3 = editProp3.getText().toString();
				if(!textProp3.equals(""))
					++nbProp;
				
				String textProp4 = editProp4.getText().toString();
				if(!textProp4.equals(""))
					++nbProp;
				
				
				if(textQuestion.equals(""))
				{
					Draw.setBackground(editQuestion, getResources().getDrawable(R.drawable.backgroundwithborderred));
					Toast toast = Toast.makeText(QubeEditLayout.this, "Veuillez entrer une question", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 200);
					toast.show();
				}
				else if(nbProp < 2)
				{
					Draw.setBackground(editQuestion, getResources().getDrawable(R.drawable.backgroundwithborder));
					Toast toast = Toast.makeText(QubeEditLayout.this, "Veuillez entrer au moins deux propositions", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 200);
					toast.show();
				}
				else if((r_one.isChecked() && textProp1.equals("")) || (r_two.isChecked() && textProp2.equals("")) || (r_three.isChecked() && textProp3.equals("")) || (r_four.isChecked() && textProp4.equals("")))
				{
					Draw.setBackground(editQuestion, getResources().getDrawable(R.drawable.backgroundwithborder));
					Toast toast = Toast.makeText(QubeEditLayout.this, "Veuillez remplir la proposition cochée", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 200);
					toast.show();
				}
				else
				{
					BddQube bddQube = new BddQube(QubeEditLayout.this);
					bddQube.open();
					
					Draw.setBackground(editQuestion, getResources().getDrawable(R.drawable.backgroundwithborder));
					Qube qube;
					if(task.equals("new"))
						qube = new Qube();
					else
						qube = bddQube.getQubeWithId(idQube);
					
					qube.setQuestion(textQuestion);
					qube.setProposition1(textProp1);
					qube.setProposition2(textProp2);
					qube.setProposition3(textProp3);
					qube.setProposition4(textProp4);
					
					if(r_one.isChecked())
						qube.setNumReponse(1);
					else if(r_two.isChecked())
						qube.setNumReponse(2);
					else if(r_three.isChecked())
						qube.setNumReponse(3);
					else if(r_four.isChecked())
						qube.setNumReponse(4);
					
					Editable res = editKeyword.getText();

					ArrayList<String> arrayRes = new ArrayList<String>();
					for(String kw : res.toString().split(";"))
					{
						arrayRes.add(kw);
					}
					
					qube.setMotsClefs(arrayRes);
					
					if(task.equals("new"))
					{
						int numNextQube = bddQube.getNextNumQube(idLevel);
						qube.setNumQube(numNextQube);
						qube.setIdNiveau(idLevel);
						qube.setScore(0);
						qube.setEtat(Qube.QUESTION_NON_TRAITE);
					}
					
					if(task.equals("new"))
						bddQube.insertQube(qube);
					else
						bddQube.updateQube(idQube, qube);
					
					bddQube.close();
					
					Intent intent = new Intent(QubeEditLayout.this, NiveauLayout.class);
					double param[] = new double[2]; // Param passé en paramètre contenant l'id de la notion et le niveau
					param[0] = idNotion; // ID de la notion
					param[1] = idLevel; // ID du niveau
					intent.putExtra("Param", param);
					startActivity(intent);
					finish();
				}
			}
    		
    	});
    	
    	LLayoutButton.addView(validButton);
    	LLayoutQube.addView(LLayoutButton);
    	
    	scrollView.addView(LLayoutQube);
    	LLayoutGlobal.addView(scrollView);
    	
    	if(task.equals("modify"))
    	{
    		BddQube bddQube = new BddQube(this);
    		bddQube.open();
    		Qube qube = bddQube.getQubeWithId(idQube);
    		
    		editQuestion.setText(qube.getQuestion());
    		editProp1.setText(qube.getProposition1());
    		editProp2.setText(qube.getProposition2());
    		editProp3.setText(qube.getProposition3());
    		editProp4.setText(qube.getProposition4());
    		
    		int numReponse = qube.getNumReponse();
    		if(numReponse == 1)
    			r_one.setChecked(true);
    		else if(numReponse == 2)
    			r_two.setChecked(true);
    		else if(numReponse == 3)
    			r_three.setChecked(true);
    		else
    			r_four.setChecked(true);
    		
    		if(qube.getMotsClefs() != null && qube.getMotsClefs().size() > 0)
    		{
    			ArrayList<String> arrayKeyword = qube.getMotsClefs();
    			String res = "";
    			for(String kw : arrayKeyword)
    			{
    				if(!kw.equals(""))
    					res += kw+";";
    			}
    			
    			editKeyword.setText(res);
    		}
    		/********************* ZONE D'OBSERVATION ********************/
    		LinearLayout LLZO = new LinearLayout(this);
    		LLZO.setOrientation(LinearLayout.VERTICAL);
    		
    		LinearLayout.LayoutParams LLPZO = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    		LLPZO.setMargins(100, 0, 0, 0);
    		LLZO.setLayoutParams(LLPZO);
    		 
    		TextView apercu = new TextView(this);
    		apercu.setText("Aperçu zone(s) d\'observation(s)");
    		apercu.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    		apercu.setPadding(10, 0, 0, 10);
    		apercu.setTypeface(null, Typeface.BOLD_ITALIC);
    		apercu.setTextColor(Color.parseColor("#3B170B"));
    		
    		LLZO.addView(apercu);
    		
    		RelativeLayout LLImage = new RelativeLayout(this);
    		RelativeLayout.LayoutParams LLPImage = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
    		
    		
    		BddLiaison bddLiaison = new BddLiaison(this);
    		bddLiaison.open();
    		Liaison liaison = bddLiaison.getZoneObsWithQubeId(qube.getIdQube());
    		
    		if(liaison != null)
    		{
    			final ArrayList<Integer> zoList = liaison.getQubeOuZoneObsList();
    			final ArrayList<String> annotationList = liaison.getAnnotationsZoneObs();
    			if(!zoList.isEmpty())
    			{	
    				int imageSize = (int)metrics.widthPixels/6;
    	    		final ArrayList<Object> mThumbIds = new ArrayList<Object>(zoList.size());
    	    		
    				BddZoneObservation bddZoneObservation = new BddZoneObservation(this);
    				bddZoneObservation.open();

    				for(int i = 0; i < zoList.size(); ++i)	
    				{
    					BitmapFactory.Options options = new BitmapFactory.Options();
    		            options.inSampleSize = 2;
	    				ZoneObservation zo = bddZoneObservation.getZoneObservationWithId(zoList.get(i));
	    				mThumbIds.add(i, BitmapFactory.decodeFile(zo.getPathToImg(), options));
	    			}
	    			bddZoneObservation.close();
	    			
	    			final GridView gridview = (GridView) LayoutInflater.from(this).inflate(R.layout.grid_view, null);
	    			gridview.setColumnWidth(imageSize);
	    			final ImageAdapter adapter = new ImageAdapter(this, mThumbIds, imageSize);
	    			gridview.setAdapter(adapter);
	        		
	        		LLImage.addView(gridview);
	        		
	        		
	        		gridview.setOnItemClickListener(new OnItemClickListener() {
	        			
	        		       public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
	        		    	   
	        		    	   /************* LAYOUT APERCU IMAGE ****************/
	        		    	   int height = ((Bitmap) mThumbIds.get(position)).getHeight();
	        		    	   int width = ((Bitmap) mThumbIds.get(position)).getWidth();
	        		    	   
	        		    	   float ratio = (float)(metrics.widthPixels/2)/width;

	        		    	   int heightImg = (int) (height*ratio);
	        		    	   int widthImg = metrics.widthPixels/2;
	        		    	   
	        		    	   RLayout.removeView(apercuImg);
	        		    	   
	        		    	   RelativeLayout.LayoutParams paramApercuImg = new RelativeLayout.LayoutParams(widthImg, heightImg);
	        		    	   paramApercuImg.setMargins(40, 0, 0, 0);
	        		    	   paramApercuImg.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
	        		    	   apercuImg.setLayoutParams(paramApercuImg);
	        		    	   
	        		    	   final ImageView img = new ImageView(QubeEditLayout.this);
	        		    	   img.setImageBitmap((Bitmap) mThumbIds.get(position));
	        		    	   img.setLayoutParams(new LinearLayout.LayoutParams(widthImg, heightImg));
	        		    	   apercuImg.addView(img);
	        		    	   
	        		    	   /************* ANNOTATION ****************/
	        		    	   String[] annotationCoord = annotationList.get(position).split(";");
	        		    	   float annotationX = Float.parseFloat(annotationCoord[0]);
	        		    	   float annotationY = Float.parseFloat(annotationCoord[1]);
	        		    	   
	        		    	   ImageView annotation = new ImageView(QubeEditLayout.this);
	        		    	   annotation.setImageResource(R.drawable.annotation);	     
	        		    	   
	        		    	   annotation.setX(annotationX*widthImg-10);
	        		    	   annotation.setY(annotationY*heightImg-15);
	        		    	   
	        		    	   apercuImg.addView(annotation);
	        		    	   
	        		    	   /************* BOUTON FERMER ****************/
	        		    	   Button closeButton = new Button(QubeEditLayout.this);
	        		    	   closeButton.setText("Fermer");
	        		    	   closeButton.setAlpha((float) 0.5);
	        		    	   Draw.setBackground(closeButton, getResources().getDrawable(R.drawable.backgroundbuttonfermer));
	        		    	   RelativeLayout.LayoutParams paramCloseButton = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        		    	   paramCloseButton.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	        		    	   
	        		    	   closeButton.setLayoutParams(paramCloseButton);
	        		    	   apercuImg.addView(closeButton);
	        		    	   
	        		    	   closeButton.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									RLayout.removeView(apercuImg);
									changeView(true);
									
									}
	        		    	   });
	        		    	   
	        		    	   /************* CROIX SUPPRESSION ****************/
	        		    	   ImageView deleteCross = new ImageView(QubeEditLayout.this);
	        		    	   deleteCross.setImageResource(R.drawable.croix_fermer_bleu);
	        		    	   RelativeLayout.LayoutParams lpcroix = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		    		           lpcroix.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		    		    		
		    		           deleteCross.setOnClickListener(new OnClickListener() 
		    		           {	
		    		    	        @Override
		    		    	        public void onClick(final View v) 
		    		    	        {
		    		    	    		AlertDialog.Builder dial = new AlertDialog.Builder(QubeEditLayout.this);
		    		    	        	dial.setMessage("Voulez-vous vraiment supprimer cette Zone d'observation?");
		    		    	        	
		    		    	        	
		    		    	        	dial.setPositiveButton("Valider", new DialogInterface.OnClickListener() 
		    		    	    		{
		    		    	    		   public void onClick(DialogInterface dialog, int id) 
		    		    	    		   {
		    									BddLiaison bddLiaison = new BddLiaison(QubeEditLayout.this);
		    									bddLiaison.open();
		    									bddLiaison.removeLiaison(idQube, zoList.get(position));
		    									
		    									int nbLiaison = bddLiaison.getNbLiaisonWithIdZO(zoList.get(position));
		    									bddLiaison.close();
		    									
		    									if(nbLiaison == 0)
		    									{
		    										BddZoneObservation bddZoneObservation = new BddZoneObservation(QubeEditLayout.this);
			    									bddZoneObservation.open();
			    									bddZoneObservation.removeZoneObservationWithID(zoList.get(position));
			    									bddZoneObservation.close();
		    									}

		    									adapter.mThumbIds.remove(position);
		    									adapter.notifyDataSetChanged();
		    									RLayout.removeView(apercuImg);
		    									
		    									changeView(true);
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
		    		    		apercuImg.addView(deleteCross, lpcroix);
		    		    		
		    		    	    changeView(false);
		    		    	    RLayout.addView(apercuImg);
	        		         
	        		        }
	        		});
    			}
    		}
    		LLZO.addView(LLImage);
  
    		LLayoutGlobal.addView(LLZO);
    		
    		bddLiaison.close();
    		
    		bddQube.close();
    	}
    	
    	RLayout.addView(LLayoutGlobal);
    }

    private void initListener(){
    	editQuestion.addTextChangedListener(new TextWatcher()
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
    	editProp1.addTextChangedListener(new TextWatcher()
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
    	
    	editProp2.addTextChangedListener(new TextWatcher()
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
    	editProp3.addTextChangedListener(new TextWatcher()
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
    	editProp4.addTextChangedListener(new TextWatcher()
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
    private void changeView(boolean bool)
    {
	    editQuestion.setEnabled(bool);
	    validButton.setEnabled(bool);
	    editProp1.setEnabled(bool);
	    editProp2.setEnabled(bool);
	    editProp3.setEnabled(bool);
	    editProp4.setEnabled(bool);
	    editKeyword.setEnabled(bool);
	    r_one.setEnabled(bool);
	    r_two.setEnabled(bool);
	    r_three.setEnabled(bool);
	    r_four.setEnabled(bool);
    }
    
	protected void onFinish()
	{
		Intent intent = new Intent(QubeEditLayout.this, NiveauLayout.class);
		double param[] = new double[2]; // Param passé en paramètre contenant l'id de la notion et le niveau
		param[0] = idNotion; // ID de la notion
		param[1] = idLevel; // ID du niveau
		intent.putExtra("Param", param);
		startActivity(intent);
		finish();
	}
}