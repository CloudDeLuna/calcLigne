//package com.example.util;
//
//import java.util.ArrayList;
//
//import com.example.botacatchingconception.ParcoursEditLayout;
//import com.example.db.bdd.BddQube;
//
//import android.content.Context;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.widget.ArrayAdapter;
//import android.widget.SimpleCursorAdapter;
//
//public class CustomAutoCompleteTextChangedListener implements TextWatcher {
//
//	Context context;
//	
//	public CustomAutoCompleteTextChangedListener(Context context){
//        this.context = context;
//    }
//	
//	@Override
//	public void beforeTextChanged(CharSequence s, int start, int count,
//			int after) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onTextChanged(CharSequence s, int start, int before, int count) {
//		
//		ParcoursEditLayout parcoursEditLayout = (ParcoursEditLayout)context;
//		BddQube bddQube = new BddQube(context);
//		bddQube.open();
//		ArrayList<String> allKeywords = bddQube.getAllKeywords();
//		String[] mots = new String[allKeywords.size()];
//		for(int i = 0; i < allKeywords.size(); ++i)
//			mots[i] = allKeywords.get(i);
//		
//		parcoursEditLayout.item = mots;
//		parcoursEditLayout.myAdapter.notifyDataSetChanged();
//		final int[] to = new int[] { parcoursEditLayout.myAutoComplete.getId() };
//		//parcoursEditLayout.myAdapter = new SimpleCursorAdapter(parcoursEditLayout, android.R.layout.simple_dropdown_item_1line, parcoursEditLayout.item);
//		parcoursEditLayout.myAdapter = new SimpleCursorAdapter(parcoursEditLayout, android.R.layout.simple_dropdown_item_1line, null, mots, to);
//		parcoursEditLayout.myAutoComplete.setAdapter(parcoursEditLayout.myAdapter);
//        bddQube.close();
//		
//	}
//
//	@Override
//	public void afterTextChanged(Editable s) {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
