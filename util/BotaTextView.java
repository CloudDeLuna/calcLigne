package com.util;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

public class BotaTextView extends TextView{

	public BotaTextView(Context context) {
		super(context);
		this.setTextColor(Color.parseColor(Constant.light_brown));
	}

}
