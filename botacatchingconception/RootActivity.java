package com.example.botacatchingconception;

import android.app.Activity;
import android.view.KeyEvent;

public abstract class RootActivity extends Activity
{
	protected abstract void onFinish();
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) 
        {
        	onFinish();
        	return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public void onBackPressed() 
    {
    	onFinish();
    	super.onBackPressed();
    }
}
