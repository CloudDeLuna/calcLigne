package com.example.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.botacatchingconception.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter
{
	private static DisplayMetrics 	metrics;
    private Activity context;
    private View father;
    private ArrayList<String> listDataHeader;
    private HashMap<String, ArrayList<String>> listDataChild;
    private ArrayList<ArrayList<Boolean>> arrayElementChecked;
    private ArrayList<ArrayList<CheckBox>> arrayElement;
    private ArrayList<Integer> arraySize;
    private boolean isFirst = true;
    private int paddingLeft = 0;
    
    public ExpandableListAdapter(Activity context, View father, ArrayList<String> listDataHeader,
            HashMap<String, ArrayList<String>> listChildData) 
    {
        this.context = context;
        this.father = father;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
        this.arrayElementChecked = new ArrayList<ArrayList<Boolean>>();
        this.arrayElement = new ArrayList<ArrayList<CheckBox>>();
        this.arraySize = new ArrayList<Integer>();
        
        for(int i = 0; i < listDataHeader.size(); ++i)
        {
        	arrayElementChecked.add(new ArrayList<Boolean>());
        	arrayElement.add(new ArrayList<CheckBox>());
        	arraySize.add(-1);
        	
        	for(int j = 0; j < listChildData.get(listDataHeader.get(i)).size(); ++j)
        	{
        		arrayElementChecked.get(i).add(false);
        		arrayElement.get(i).add(null);
        	}
        }
    }
 
    @Override
    public Object getChild(int groupPosition, int childPosititon) 
    {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosititon);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) 
    {
        return childPosition;
    }
 
    @SuppressLint("InflateParams")
	@Override
    public View getChildView(final int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) 
    {
        final String childText = (String) getChild(groupPosition, childPosition);
 
        if (convertView == null) 
        {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(
            															Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
 
        final CheckBox txtListChild = (CheckBox) convertView.findViewById(R.id.lblListItem);
        
        txtListChild.setText(childText);
        
        if(this.isFirst)
        {
        	//Pas besoin de rectifier le Padding des checkBoxs pour les versions supérieur à 17
            if(Build.VERSION.SDK_INT >= 17)
            	this.paddingLeft = txtListChild.getPaddingLeft();
            else
            	this.paddingLeft = txtListChild.getPaddingLeft() + Math.round(20.0f * metrics.density);
            
        	this.isFirst = false;
        }
        
    	txtListChild.setPadding(this.paddingLeft, txtListChild.getPaddingTop(), 
    			txtListChild.getPaddingRight(), txtListChild.getPaddingBottom());

        txtListChild.setOnCheckedChangeListener(new OnCheckedChangeListener() 
        {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				arrayElementChecked.get(groupPosition).set(childPosition, isChecked);				
			}
		});
        if(arrayElement.get(groupPosition).get(childPosition) == null)
        	arrayElement.get(groupPosition).set(childPosition, txtListChild);
        txtListChild.setChecked(arrayElementChecked.get(groupPosition).get(childPosition));
        float textSize = SizeCalculator.getTextSizeFor(this.context, 0.03f);
        txtListChild.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) 
    {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .size();
    }
 
    @Override
    public Object getGroup(int groupPosition) 
    {
        return this.listDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() 
    {
        return this.listDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) 
    {
        return groupPosition;
    }
 
    @SuppressLint("InflateParams")
	@Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) 
    {
        String headerTitle = (String) getGroup(groupPosition);
        
        if (convertView == null) 
        {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
 
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        float textSize = SizeCalculator.getTextSizeFor(this.context, 0.03f);
        lblListHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        lblListHeader.setTextColor(Color.parseColor("#3B170B"));
        
        int height = 0;
    	boolean isNotNull = true;
    	for(int i = 0; i < arrayElement.get(groupPosition).size(); ++i)
    	{
    		if(arrayElement.get(groupPosition).get(i) != null)
    			height += arrayElement.get(groupPosition).get(i).getHeight();
    		else
    			isNotNull = false;
    	}
    	
		if(arraySize.get(groupPosition) == -1 && isNotNull)
			arraySize.set(groupPosition, height);
        
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() 
    {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) 
    {
        return true;
    }
    
    public void resetCheckBoxs()
    {
    	for(int i = 0; i < arrayElement.size(); ++i)
    	{
    		for(int j = 0; j < arrayElement.get(i).size(); ++j)
    		{	
    			arrayElementChecked.get(i).set(j, false);
    			if(arrayElement.get(i).get(j) != null)
	    			arrayElement.get(i).get(j).setChecked(false);
    		}
    	}
    }
    
    public int getGroupHeight(final int groupPosition)
    {
    	int height = 0;
    	
    	Paint mPaint = new Paint(); 
    	float textSize = SizeCalculator.getTextSizeFor(this.context, 0.03f);
    	mPaint.setTextSize (textSize);//La taille du text de la checkbox dans list_item.xml 
    	float FontSpace = mPaint.getFontSpacing() * metrics.density; 
    	
    	if(arraySize.get(groupPosition) == -1)
    	{
	    	for(int i = 0; i < arrayElement.get(groupPosition).size(); ++i)
	    	{
	    		if(arrayElement.get(groupPosition).get(i) != null)
	    		{
	    			height += arrayElement.get(groupPosition).get(i).getHeight();
	    		}
	    		else
	    		{
	    			height += ((listDataChild.get(listDataHeader.get(groupPosition)).get(i).length() * FontSpace) 
	    						/ father.getWidth()*3/4) * metrics.heightPixels/45 + metrics.heightPixels/35*metrics.density;
	    		}
	    	}
	    	
    		return height;
    	}
    	
    	//Toast.makeText(context, "Group " + groupPosition + " height " + arraySize.get(groupPosition), Toast.LENGTH_SHORT).show();
    	
    	return arraySize.get(groupPosition);
    }
    
    public boolean isChildSelected(int groupPosition, int childPosition) 
    {
        return this.arrayElementChecked.get(groupPosition).get(childPosition);
    }
    
    public int getIdNotion(int groupPosition)
    {
    	String tab [] = listDataHeader.get(groupPosition).split(" : ");
    	return Integer.parseInt(tab[1].split("\\.")[0]);
    }
    
    public int getIdNiveau(int groupPosition)
    {
    	String tab [] = listDataHeader.get(groupPosition).split(" : ");
    	return Integer.parseInt(tab[1].split("\\.")[1]);
    }
    
    public static void setMetrics(DisplayMetrics metrcs)
    {
        metrics = metrcs;
    }
}