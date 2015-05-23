package com.botacachinggame;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.db.object.Niveau;
import com.util.BotaTextView;
import com.util.Constant;
import com.util.SizeCalculator;

public class MiseAJourBD extends RootActivity {

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if(RootActivity.majBD)
        	loadMAJBD();
        else
        	launchNotionMenu();
        
    }

	private void loadMAJBD() {

		RootActivity.majBD = false;
		
		LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
		
        /**************************** Creation infoView ***************************/
        
        createAndAddInfoView(layout);
        
        /****************** Creation de la bar de progression *********************/
        
        ProgressBar progressBar = createAndAddProgressBar(layout);
		
        /******************** Ajout du layout a l'activity ************************/
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				 							   							 LinearLayout.LayoutParams.MATCH_PARENT);
		
		this.addContentView(layout, params);		
		
		/******************** Lancement du "telechargement" ************************/
		
		launchDownload(progressBar);
	}

	private void createAndAddInfoView(LinearLayout layout) {

		BotaTextView textInfo = new BotaTextView(this);
		textInfo.setText("Mise à jour de la base de données en cours ...\n\n" +
						 "(Connectez-vous à un wifi ou activez les données mobiles)");
		float textSize = SizeCalculator.getTextSizeFor(this, Constant.majBDTextSize);
		textInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		textInfo.setGravity(Gravity.CENTER);
		
		int marginLeft = (int)SizeCalculator.getXSizeFor(this, Constant.majBDMarginLeft);
		int marginRight = (int)SizeCalculator.getXSizeFor(this, Constant.majBDMarginRight);
		int marginTop = (int)SizeCalculator.getYSizeFor(this, Constant.majBDInfoMaringTop);
		int marginBottom = (int)SizeCalculator.getYSizeFor(this, Constant.majBDInfoMaringBottom);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
											   							 LinearLayout.LayoutParams.WRAP_CONTENT);
		
		params.setMargins(marginLeft,marginTop,marginRight,marginBottom);
		
        layout.addView(textInfo,params);
		
	}

	private ProgressBar createAndAddProgressBar(LinearLayout layout) {

		ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
																	     LinearLayout.LayoutParams.WRAP_CONTENT);

		int marginLeft = (int) SizeCalculator.getXSizeFor(this, Constant.majBDMarginLeft);
		int marginRight = (int) SizeCalculator.getXSizeFor(this, Constant.majBDMarginRight);
		
		params.setMargins(marginLeft, 0, marginRight, 0);
		
		layout.addView(progressBar, params);
		
		return progressBar;
	}

	private void launchDownload(final ProgressBar progressBar) {

		new Thread(new Runnable() {

			public void run() {
				
				int progress = 0;
				
				while (progressBar.getProgress() < progressBar.getMax()) {

					// sleep 1 second (simulating a time consuming task...)
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					progress += 10;
					
					final int current_progress = progress;
					// Update the progress bar
					progressBar.post(new Runnable() {
						public void run() {
							progressBar.setProgress(current_progress);
						}
					});
				}

				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				MiseAJourBD.this.launchNotionMenu();

				
			}
		}).start();
		
	}

	private void launchNotionMenu() {

		Intent intent = new Intent(MiseAJourBD.this, LevelSelectionMenu.class);
		startActivity(intent);
    	onDestroy();
    	finish();
		
	}

	@Override
	protected boolean hasReturnButton() {
		return false;
	}

	@Override
	protected boolean hasHomeButton() {
		return false;
	}	
	
	@Override
	protected boolean hasExitButton() {
		return false;
	}
	
	@Override
	protected Niveau getCurrentLevel() {
		return null;
	}
	
	@Override
	protected void onBackKeyPressed() {
		//on ignore
		return;
	}

}