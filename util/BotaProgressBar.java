package com.util;

import com.botacachinggame.R;
import com.db.object.Niveau;

import android.content.Context;
import android.widget.ProgressBar;

public class BotaProgressBar extends ProgressBar{

	public BotaProgressBar(Context context, int defStyleAttr, int scoreMax, Niveau currentLevel) {
		super(context, null, defStyleAttr);

		this.setProgressDrawable(context.getResources().getDrawable(this.getColorForScore(currentLevel, scoreMax)));
		this.setMax(scoreMax);
		this.setProgress(currentLevel.getScoreActuel());
		
	}
	
	private int getColorForScore(Niveau level, int scoreMax) {
		
		int color;
		
		if(level.getScoreActuel() < (int)(scoreMax * 0.3) )
			color = R.drawable.progressbarred;
		else if(level.getScoreActuel() < level.getScoreToUnlock() )
			color = R.drawable.progressbarorange;
		else if(level.getScoreActuel() < scoreMax)
			color = R.drawable.progressbargreen;
		else
			color = R.drawable.progressbarblue;
		
		return color;
	}


}
