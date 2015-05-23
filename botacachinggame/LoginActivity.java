package com.botacachinggame;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.db.bdd.BddJoueur;
import com.db.object.Joueur;
import com.db.object.Niveau;
import com.util.BotaButton;
import com.util.BotaTextView;
import com.util.Constant;
import com.util.Draw;
import com.util.KeyboardListener;
import com.util.SizeCalculator;


public class LoginActivity extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
        /**Cette activity n'est visible qu'au premier lancement du jeu
         * 
		 * La structure de cette activity est:
		 * LinearLayout(verticale)(variable: loginLayout)
		 * 		TextView(variable: title)
		 * 		LinearLayout(horizontal)(variable: pseudoLayout)
		 * 			TextView(variable : pseudoText)
		 * 			EditText(variable : editPseudo)
		 * 		LinearLayout(horizontal)(variable: emailLayout)
		 * 			TextView(variable : emailText)
		 * 			EditText(variable : editEmail)
		 * 		Button (variable : validerButton)
		 **/        
        
        BddJoueur bddJoueur = new BddJoueur(this);
        bddJoueur.open();
		
        Joueur joueur = bddJoueur.getJoueurWhoIsPlaying();
        
        bddJoueur.close();
        if(joueur == null ){
        	
        	loadLogin();
        }
        else{
        	Intent intent = new Intent(LoginActivity.this, MainMenu.class);
	    	startActivity(intent);
	    	onDestroy();
	    	finish();
        }
    }
    
	private void loadLogin() {
		
		/*************************** Creation du loginLayout *****************************/
        
        LinearLayout loginLayout = new LinearLayout(this);
        loginLayout.setOrientation(LinearLayout.VERTICAL);
	
        //pour que le clavier disparaisse quand on click a cote
        KeyboardListener.hideKeyboardListener(loginLayout, this);
        
        /************************* Creation du titre ****************************/

        createAndAddTitle(loginLayout);
        
        /************************* Creation du textInfo ****************************/
        
        createAndAddTextInfo(loginLayout);
		
        /************************* Creation du pseudoLayout ****************************/
       
        EditText editPseudo = createAndAddPseudoLayout(loginLayout);
        
        /************************* Creation du emailLayout ****************************/
        
        EditText editEmail = createAndAddEmailLayout(loginLayout);
        
        /************************** Creation du validerButton ********************************/
        
        createAndAddValiderButton(loginLayout, editPseudo, editEmail);
        
        /************************** Ajout du profilLayout a l'actvity ******************/
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				   							   							 LinearLayout.LayoutParams.MATCH_PARENT);
        
        this.addContentView(loginLayout, params);
		
	}

	private void createAndAddTitle(LinearLayout loginLayout) {

		/*************************** Parametre pour le titre *****************************/
        
        LinearLayout.LayoutParams  params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				   														  LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		
		int titleMarginBottom = (int)SizeCalculator.getYSizeFor(this, Constant.loginTitleMarginBottom);
		int titleMarginTop 	  = (int)SizeCalculator.getYSizeFor(this, Constant.loginTitleMarginTop);
		
		params.setMargins(0, titleMarginTop, 0, titleMarginBottom);
		
		/******************************** Creation du titre ***************************/
		
		BotaTextView title = new BotaTextView(this);
		title.setText(Constant.loginTitle);
		float titleSize = SizeCalculator.getTextSizeFor(this, Constant.loginTitleSize);
		title.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
		
		loginLayout.addView(title,params);
	}

	private void createAndAddTextInfo(LinearLayout loginLayout) {

		BotaTextView textInfo = new BotaTextView(this);
		textInfo.setText("Le pseudo et l'adresse mail ne doivent pas contenir d'espaces");
		textInfo.setGravity(Gravity.CENTER);
		float textSize = SizeCalculator.getTextSizeFor(this, Constant.loginTextSize);
		textInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

		int marginLeft = (int)SizeCalculator.getXSizeFor(this, Constant.loginTextMarginLeft);
		int marginRight = (int)SizeCalculator.getXSizeFor(this, Constant.loginTextMarginRight);
		int marginBottom = (int)SizeCalculator.getYSizeFor(this, Constant.loginTextMarginBottom);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
											   							 LinearLayout.LayoutParams.WRAP_CONTENT);
		
		params.setMargins(marginLeft,0,marginRight,marginBottom*2);
		
        loginLayout.addView(textInfo,params);
		
	}

	private EditText createAndAddPseudoLayout(LinearLayout loginLayout) {
		
		/************************* Creation du pseudoLayout ****************************/
		
        LinearLayout pseudoLayout = new LinearLayout(this);
        pseudoLayout.setOrientation(LinearLayout.HORIZONTAL);
        
        /************************ Creation du profilText *******************************/
		
		BotaTextView pseudoText = new BotaTextView(this);
        pseudoText.setText("Pseudo : ");
        float textSize = SizeCalculator.getTextSizeFor(this, Constant.loginTextSize);
        pseudoText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        int leftMargin = (int)SizeCalculator.getXSizeFor(this, Constant.loginTextMarginLeft);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
        									   							 LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(leftMargin,0,0,0);
        
        pseudoLayout.addView(pseudoText,params);
        
        
        /************************** Creation du editPseudo *****************************/
        
        EditText editPseudo = new EditText(this);
        editPseudo.setSingleLine(true);
        editPseudo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Constant.pseudoMaxCharacter)});
        editPseudo.setTextColor(Color.parseColor(Constant.light_brown));
        Draw.setBackground(editPseudo, getResources().getDrawable(R.drawable.backgroundwithborder));
        
        int height = (int)SizeCalculator.getYSizeFor(this, Constant.loginEditTextHeight);
        int width = (int)SizeCalculator.getXSizeFor(this, Constant.loginInputSize);
        
        params = new LinearLayout.LayoutParams(width, height);
        pseudoLayout.addView(editPseudo, params);
        
        /*************************** pseudoCrossView ********************************/
        
        ImageView pseudoCrossView = new ImageView(this);
        
        int crossMarginLeft = (int)SizeCalculator.getXSizeFor(this, Constant.loginCrossMarginLeft);
        int crossMarginRight = (int)SizeCalculator.getXSizeFor(this, Constant.loginCrossMarginRight);
        
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
        									   LinearLayout.LayoutParams.WRAP_CONTENT);
        
        params.setMargins(crossMarginLeft, 0, crossMarginRight, 0);
        
        pseudoLayout.addView(pseudoCrossView, params);
        

        editPseudo.setOnFocusChangeListener(onFocusChangePseudo(editPseudo,pseudoCrossView));
        
        /********************** Ajout du pseudoLayout au loginLayout ********************/
        
        int marginBottom = (int)SizeCalculator.getYSizeFor(this, Constant.loginTextMarginBottom);
        
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				   							   LinearLayout.LayoutParams.WRAP_CONTENT);
        
        
        params.setMargins(0, 0, leftMargin, marginBottom);
        
        loginLayout.addView(pseudoLayout,params);
        
        return editPseudo;
		
	}

	private EditText createAndAddEmailLayout(LinearLayout loginLayout) {

		/************************* Creation du emailLayout ****************************/
        
        LinearLayout emailLayout = new LinearLayout(this);
        emailLayout.setOrientation(LinearLayout.HORIZONTAL);
        
        /************************ Creation du emailText *******************************/
        
        BotaTextView emailText = new BotaTextView(this);
        emailText.setText("Email     : ");
        float textSize = SizeCalculator.getTextSizeFor(this, Constant.loginTextSize);
        emailText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        int leftMargin = (int)SizeCalculator.getXSizeFor(this, Constant.loginTextMarginLeft);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
        																 LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(leftMargin,0,0,0);
        
        emailLayout.addView(emailText,params);
        
        /************************** Creation du editEmail *****************************/
        
        EditText editEmail = new EditText(this);
        editEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        editEmail.setTextColor(Color.parseColor(Constant.light_brown));
        Draw.setBackground(editEmail, getResources().getDrawable(R.drawable.backgroundwithborder));
        
        int height = (int)SizeCalculator.getYSizeFor(this, Constant.loginEditTextHeight);
        int width = (int)SizeCalculator.getXSizeFor(this, Constant.loginInputSize);
        
        params = new LinearLayout.LayoutParams(width, height);
        //params.setMargins(0, 0, leftMargin, 0);
        emailLayout.addView(editEmail, params);
        
        /*************************** emailCrossView ********************************/
        
        ImageView emailCrossView = new ImageView(this);

        int crossMarginLeft = (int)SizeCalculator.getXSizeFor(this, Constant.loginCrossMarginLeft);
        int crossMarginRight = (int)SizeCalculator.getXSizeFor(this, Constant.loginCrossMarginRight);
        
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
        									   LinearLayout.LayoutParams.WRAP_CONTENT);
        
        params.setMargins(crossMarginLeft, 0, crossMarginRight, 0);
        
        emailLayout.addView(emailCrossView, params);
        
        editEmail.setOnFocusChangeListener(onFocusChangeEmail(editEmail,emailCrossView));
        
        /********************** Ajout du emailLayout au loginLayout ********************/
        
        int marginBottom = (int)SizeCalculator.getYSizeFor(this, Constant.loginTextMarginBottom);
        
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				   							   LinearLayout.LayoutParams.WRAP_CONTENT);
        
        params.setMargins(0, 0, leftMargin, marginBottom);
        
        loginLayout.addView(emailLayout, params);
        
        return editEmail;
		
	}
	
	private void createAndAddValiderButton(LinearLayout loginLayout, EditText editPseudo, EditText editEmail) {

		BotaButton validerButton = new BotaButton(this);
        validerButton.setText("Valider");
        float textSize = SizeCalculator.getTextSizeFor(this, Constant.textButtonSize);
        validerButton.setTextSize(textSize);
        
        validerButton.setOnClickListener(onClickValider(editPseudo, editEmail));
        
        int width = (int)SizeCalculator.getXSizeFor(this, Constant.mainMenuButton_width);
		int height = (int)SizeCalculator.getYSizeFor(this, Constant.mainMenuButton_height);
        int validerMarginTop = (int)SizeCalculator.getYSizeFor(this, Constant.loginValiderButtonMarginTop);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
        
        params.gravity = Gravity.CENTER;
        params.setMargins(0, validerMarginTop, 0, 0);
        
        loginLayout.addView(validerButton, params);
		
	}
	
	private OnFocusChangeListener onFocusChangeEmail(final EditText editEmail, final ImageView emailCrossView) {

		return new OnFocusChangeListener() {          

	        public void onFocusChange(View v, boolean hasFocus) {
	        	if(!hasFocus){
	        		boolean ok = checkEmail(editEmail.getText().toString());
	        		
	        		if(!ok)
	        			emailCrossView.setImageResource(R.drawable.croix_erreur);
	        		else
	        			emailCrossView.setImageResource(R.drawable.ok);
	        	}
	        }
	    };
		
	}

	private OnFocusChangeListener onFocusChangePseudo(final EditText editPseudo, final ImageView pseudoView) {
		
		return new OnFocusChangeListener() {          

	        public void onFocusChange(View v, boolean hasFocus) {
	        	if(!hasFocus){
	        		boolean ok = checkPseudo(editPseudo.getText().toString());
	        		
	        		if(!ok)
	        			pseudoView.setImageResource(R.drawable.croix_erreur);
	        		else
	        			pseudoView.setImageResource(R.drawable.ok);
	        	}
	        }
	    };
	}
	
	private OnClickListener onClickValider(final EditText editPseudo, final EditText editEmail) {
		
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				editPseudo.clearFocus();
				editEmail.clearFocus();
				
				String pseudo = editPseudo.getText().toString();
				String email = editEmail.getText().toString();
				
				boolean pseudoOk = checkPseudo(pseudo);
				boolean emailOk = checkEmail(email.toString());
				
				if(emailOk && pseudoOk){

					updateDataBase(pseudo, email);
					
					Intent intent = new Intent(LoginActivity.this, MainMenu.class);
			    	startActivity(intent);
			    	onDestroy();
			    	finish();
				}
			}
		};
	}

	private boolean checkEmail(String email) {

		Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");
		Matcher matcher = pattern.matcher(email.toUpperCase());
	
		return matcher.matches();
	}

	private boolean checkPseudo(String pseudo) {

		if(pseudo.length() == 0 || pseudo.contains(" ")){
			return false;
		}
		
		BddJoueur bddJoueur = new BddJoueur(this);
		bddJoueur.open();
		
		ArrayList<Joueur> joueurs = bddJoueur.getAllJoueurs();
		
		bddJoueur.close();
		
		for(int i = 0; i < joueurs.size() ; ++i ){
			
			String currentPseudo = joueurs.get(i).getPseudo().toLowerCase();
			
			if(currentPseudo.equals(pseudo.toLowerCase())){
				return false;
			}
			
		}
		
		return true;
	}

	private void updateDataBase(String pseudo, String email) {

		Joueur joueur = new Joueur(0, pseudo, 0, 0, 0, true, true, true);
		
		BddJoueur bddJoueur = new BddJoueur(this);
		bddJoueur.open();

		bddJoueur.insertJoueur(joueur);
		
		bddJoueur.close();
	}
	
	@Override
	protected boolean hasReturnButton() {
		return true;
	}

	@Override
	protected boolean hasHomeButton() {
		return false;
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
		
		exit();
		
	}
    
    

}
