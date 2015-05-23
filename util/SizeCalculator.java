package com.util;

import android.app.Activity;
import android.util.DisplayMetrics;

public class SizeCalculator {
	
	public static float getYSizeFor(Activity context, float sizeToCalculate){
		
		DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		float size = metrics.heightPixels * sizeToCalculate;
		return size;
	}
	
	public static float getXSizeFor(Activity context, float sizeToCalculate){
		
		DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		float size = metrics.widthPixels * sizeToCalculate;
		return size;
	}
	
	public static float getTextSizeFor(Activity context, float sizeToCalculate){
		
		float textSize = getYSizeFor(context, sizeToCalculate);
		
		final float scale = context.getResources().getDisplayMetrics().density;
	    return ((textSize - 0.5f) / scale);
		
	}

}
