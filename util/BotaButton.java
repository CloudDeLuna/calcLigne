package com.util;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.botacachinggame.R;

public class BotaButton extends Button{

	public BotaButton(Context context) {
		super(context);
		
		this.setTextAppearance(context, TextView.NO_ID);
		this.setTransformationMethod(null);
		this.setBackgroundResource(R.drawable.buttun_shape);
	}
	
}
