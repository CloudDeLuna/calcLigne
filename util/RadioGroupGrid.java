package com.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;

public class RadioGroupGrid extends TableLayout  implements OnClickListener {

    private RadioButton activeRadioButton;

    public RadioGroupGrid(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public RadioGroupGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onClick(View v) {
    	
        RadioButton rb = (RadioButton) v;
        
        if ( activeRadioButton != null ) {
            activeRadioButton.setChecked(false);
        }
        rb.setChecked(true);
        activeRadioButton = (RadioButton) v;
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        setChildrenOnClickListener((TableRow)child);
    }
    
    @Override
    public void addView(View child, int index,
            android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        setChildrenOnClickListener((TableRow)child);
    }


    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
       
        setChildrenOnClickListener((TableRow)child);
    }


    private void setChildrenOnClickListener(TableRow tr) {
        final int c = tr.getChildCount();
        for (int i=0; i < c; i++) {
            final View v = tr.getChildAt(i);
            if ( v instanceof RadioButton ) {
                v.setOnClickListener(this);
            }
        }
    }

    public int getCheckedRadioButtonId() {
        
    	if ( activeRadioButton != null ) {
            return activeRadioButton.getId();
        }

        return -1;
    }
    
    public RadioButton getCheckedRadioButton() {
        
    	if ( activeRadioButton != null ) {
            return activeRadioButton;
        }

        return null;
    }
}