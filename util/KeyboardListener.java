package com.util;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboardListener 
{
	public static void hideKeyboardListener(View view, final Activity activity) 
	{
        if(!(view instanceof EditText)) 
        {
            view.setOnTouchListener(new View.OnTouchListener() 
            {
                public boolean onTouch(View v, MotionEvent event) 
                {
                    hideSoftKeyboard(activity);
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup) 
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) 
            {
                View innerView = ((ViewGroup) view).getChildAt(i);
                hideKeyboardListener(innerView, activity);
            }
        }
    }
 
    public static void hideSoftKeyboard(Activity activity) 
    {
        InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
