package com.example.db.bdd;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.db.object.Qube;

public class BddQube 
{
	private static final String TABLE_QUBE 				= "qube";

	private static final String COL_ID_QUBE 			= "idQube";
	private static final int 	NUM_COL_ID				= 0;
	
	private static final String COL_NUM_QUBE 			= "numQUBE";
	private static final int 	NUM_COL_NUM				= 1;
	
	private static final String COL_QUESTION 			= "question";
	private static final int 	NUM_COL_QUESTION		= 2;
	
	private static final String COL_PROP1   			= "prop1";
	private static final int 	NUM_COL_PROP_1			= 3;
	
	private static final String COL_PROP2   			= "prop2";
	private static final int 	NUM_COL_PROP_2			= 4;
	
	private static final String COL_PROP3   			= "prop3";
	private static final int 	NUM_COL_PROP_3			= 5;
	
	private static final String COL_PROP4   			= "prop4";
	private static final int 	NUM_COL_PROP_4			= 6;
	
	private static final String COL_NUM_REPONSE			= "numReponse";
	private static final int 	NUM_COL_NUM_REPONSE		= 7;
	
	private static final String COL_IS_QCM_BASIC   		= "isQCMBasic";
	private static final int 	NUM_COL_IS_QCM_BASIC	= 8;
	
	private static final String COL_IS_BLOCKED_QUBE 	= "isBlocked";
	private static final int 	NUM_COL_IS_BLOCKED		= 9;
	
	private static final String COL_IS_PLAYABLE_QUBE 	= "isPlayable";
	private static final int 	NUM_COL_IS_PLAYABLE		= 10;
	
	private static final String COL_ID_NIVEAU_QUBE 		= "idNiveau";
	private static final int 	NUM_COL_ID_NIVEAU		= 11;
	
	private static final String COL_SCORE_QUBE 			= "score";
	private static final int 	NUM_COL_SCORE			= 12;
	
	private static final String COL_ETAT_QUBE			= "etat";
	private static final int 	NUM_COL_ETAT			= 13;

	private static final String COL_MOTS_CLEFS			= "motsClefs";
	private static final int 	NUM_MOTS_CLEFS			= 14;

	private SQLiteDatabase bdd;
	private BddBotacatching bddBotacatching;
 
	public BddQube(Context context)
	{
		bddBotacatching = new BddBotacatching(context, BddBotacatching.NOM_BDD, null, BddBotacatching.VERSION_BDD);
	}
 
	public void open()
	{
		bdd = bddBotacatching.getWritableDatabase();
	}
 
	public void close()
	{
		bdd.close();
	}
 
	public SQLiteDatabase getBDD()
	{
		return bdd;
	}
	
	public long insertQube(Qube qube)
	{
		ContentValues values = new ContentValues();
		
		values.put(COL_NUM_QUBE, 		qube.getNumQube());
		values.put(COL_QUESTION, 		qube.getQuestion());
		values.put(COL_PROP1, 			qube.getProposition1());
		values.put(COL_PROP2, 			qube.getProposition2());
		values.put(COL_PROP3, 			qube.getProposition3());
		values.put(COL_PROP4, 			qube.getProposition4());
		values.put(COL_NUM_REPONSE,		qube.getNumReponse());
		values.put(COL_IS_QCM_BASIC, 	qube.isQcmBasic());
		values.put(COL_IS_BLOCKED_QUBE, qube.isBlocked());
		values.put(COL_IS_PLAYABLE_QUBE,qube.isPlayable());
		values.put(COL_ID_NIVEAU_QUBE, 	qube.getIdNiveau());
		values.put(COL_SCORE_QUBE, 		qube.getScore());
		values.put(COL_ETAT_QUBE, 		qube.getEtat());
		values.put(COL_MOTS_CLEFS, 		qube.getMotClefsForInsert());
		
		return bdd.insert(TABLE_QUBE, null, values);
	}
 
	public int updateQube(int id, Qube qube)
	{
		ContentValues values = new ContentValues();
		
		values.put(COL_NUM_QUBE, 		qube.getNumQube());
		values.put(COL_QUESTION, 		qube.getQuestion());
		values.put(COL_PROP1, 			qube.getProposition1());
		values.put(COL_PROP2, 			qube.getProposition2());
		values.put(COL_PROP3, 			qube.getProposition3());
		values.put(COL_PROP4, 			qube.getProposition4());
		values.put(COL_NUM_REPONSE,		qube.getNumReponse());
		values.put(COL_IS_QCM_BASIC, 	qube.isQcmBasic());
		values.put(COL_IS_BLOCKED_QUBE, qube.isBlocked());
		values.put(COL_IS_PLAYABLE_QUBE,qube.isPlayable());
		values.put(COL_ID_NIVEAU_QUBE, 	qube.getIdNiveau());
		values.put(COL_SCORE_QUBE, 		qube.getScore());
		values.put(COL_ETAT_QUBE, 		qube.getEtat());
		values.put(COL_MOTS_CLEFS, 		qube.getMotClefsForInsert());
		
		return bdd.update(TABLE_QUBE, values, COL_ID_QUBE + " = " + id, null);
	}
	
	public void updateOrderQube(int idNiveau, int oldNum, int newNum)
	{
		Log.v("old", String.valueOf(oldNum));
		Log.v("new", String.valueOf(newNum));
		
		/************* UPDATE DU QUBE DEPLACE *************/
		String req = "SELECT * FROM " + TABLE_QUBE + " WHERE " + COL_NUM_QUBE + " = " + oldNum + " AND " + COL_ID_NIVEAU_QUBE + " = " + idNiveau;
		Cursor c = null;		
		try 
		{
			c = bdd.rawQuery(req, null);
		}
		catch(SQLiteException e) 
		{
			Log.v("error", e.getMessage());
		}
		
		if(c.getCount() <= 0)
			return;
		
		c.moveToFirst();
		Qube qubeMoved;
		do
		{
			ArrayList<String> motsClefs = new ArrayList<String>();
			for(String mot : c.getString(NUM_MOTS_CLEFS).split(";"))
				motsClefs.add(mot);
			
			qubeMoved = new Qube(c.getInt(NUM_COL_ID), 
							c.getInt(NUM_COL_NUM),
							 c.getString(NUM_COL_QUESTION), 
							 c.getString(NUM_COL_PROP_1),
							 c.getString(NUM_COL_PROP_2), 
							 c.getString(NUM_COL_PROP_3),
							 c.getString(NUM_COL_PROP_4),
							 c.getInt(NUM_COL_NUM_REPONSE),
							 (c.getInt(NUM_COL_IS_QCM_BASIC) != 0 ? true : false),
							 (c.getInt(NUM_COL_IS_BLOCKED) != 0 ? true : false),
							 (c.getInt(NUM_COL_IS_PLAYABLE) != 0 ? true : false),
							 c.getInt(NUM_COL_ID_NIVEAU), 
							 c.getInt(NUM_COL_SCORE),
							 c.getInt(NUM_COL_ETAT),
							 motsClefs);
			
			qubeMoved.setNumQube(newNum);
			
			
		}while(c.moveToNext());
		
		
		/******************* UPDATE DES AUTRES QUBES *********************/
		if(oldNum < newNum)
			req = "SELECT * FROM " + TABLE_QUBE + " WHERE " + COL_NUM_QUBE + " > " + oldNum + " AND " + COL_NUM_QUBE + " <= " + newNum + " AND " + COL_ID_NIVEAU_QUBE + " = " + idNiveau;
		else
			req = "SELECT * FROM " + TABLE_QUBE + " WHERE " + COL_NUM_QUBE + " < " + oldNum + " AND " + COL_NUM_QUBE + " >= " + newNum + " AND " + COL_ID_NIVEAU_QUBE + " = " + idNiveau;

		c = null;		
		try 
		{
			c = bdd.rawQuery(req, null);
		}
		catch(SQLiteException e) 
		{
			Log.v("error", e.getMessage());
		}
	
		if(c.getCount() <= 0)
			return;
		
		c.moveToFirst();	
		do
		{
			ArrayList<String> motsClefs = new ArrayList<String>();
			for(String mot : c.getString(NUM_MOTS_CLEFS).split(";"))
				motsClefs.add(mot);
			
			Qube qube = new Qube(c.getInt(NUM_COL_ID), 
							c.getInt(NUM_COL_NUM),
							 c.getString(NUM_COL_QUESTION), 
							 c.getString(NUM_COL_PROP_1),
							 c.getString(NUM_COL_PROP_2), 
							 c.getString(NUM_COL_PROP_3),
							 c.getString(NUM_COL_PROP_4),
							 c.getInt(NUM_COL_NUM_REPONSE),
							 (c.getInt(NUM_COL_IS_QCM_BASIC) != 0 ? true : false),
							 (c.getInt(NUM_COL_IS_BLOCKED) != 0 ? true : false),
							 (c.getInt(NUM_COL_IS_PLAYABLE) != 0 ? true : false),
							 c.getInt(NUM_COL_ID_NIVEAU), 
							 c.getInt(NUM_COL_SCORE),
							 c.getInt(NUM_COL_ETAT),
							 motsClefs);
			
			Log.v("QUBE ", String.valueOf(qube.getNumQube()));
			
			if(oldNum < newNum)
				qube.setNumQube(qube.getNumQube() - 1);
			else
				qube.setNumQube(qube.getNumQube() + 1);
			
			updateQube(qube.getIdQube(), qube);
			
		}while(c.moveToNext());
		
		updateQube(qubeMoved.getIdQube(), qubeMoved);
	}
 
	public int removeQubeWithID(int id)
	{
		return bdd.delete(TABLE_QUBE, COL_ID_QUBE + " = " +id, null);
	}
	
	public int removeQubesWithIDNiveau(int idNiveau)
	{
		return bdd.delete(TABLE_QUBE, COL_ID_NIVEAU_QUBE + " = "+idNiveau, null);
	}
	
	public Qube getQubeWithId(int id)
	{	
		String req = "SELECT * FROM " + TABLE_QUBE + 
					 " WHERE " + COL_ID_QUBE + " == " + id + " ;";
		Cursor c = null;
		try 
		{
			c = bdd.rawQuery(req, null);
		}
		catch(SQLiteException e) 
		{
			Log.v("error", e.getMessage());
		}
		
		if (c.getCount() <= 0)
			return null;
 
		c.moveToFirst();
		
		ArrayList<String> motsClefs = new ArrayList<String>();
		for(String mot : c.getString(NUM_MOTS_CLEFS).split(";"))
			motsClefs.add(mot);
		
		Qube qube = new Qube(id, c.getInt(NUM_COL_NUM),
								 c.getString(NUM_COL_QUESTION),
								 c.getString(NUM_COL_PROP_1),
								 c.getString(NUM_COL_PROP_2), 
								 c.getString(NUM_COL_PROP_3),
								 c.getString(NUM_COL_PROP_4),
								 c.getInt(NUM_COL_NUM_REPONSE),
								 (c.getInt(NUM_COL_IS_QCM_BASIC) != 0 ? true : false),
								 (c.getInt(NUM_COL_IS_BLOCKED) != 0 ? true : false),
								 (c.getInt(NUM_COL_IS_PLAYABLE) != 0 ? true : false),
								 c.getInt(NUM_COL_ID_NIVEAU), 
								 c.getInt(NUM_COL_SCORE),
								 c.getInt(NUM_COL_ETAT),
								 motsClefs);
		c.close();
		
		return qube;
	}
	
	public ArrayList<Qube> getQubesWithNiveauId(int idNiveau)
	{	
		String req = "SELECT * FROM " + TABLE_QUBE + 
					 " WHERE " + COL_ID_NIVEAU_QUBE + " == " + idNiveau + " ORDER BY " + COL_NUM_QUBE + " ASC ";
		Cursor c = null;		
		try 
		{
			c = bdd.rawQuery(req, null);
		}
		catch(SQLiteException e) 
		{
			Log.v("error", e.getMessage());
		}
	
		ArrayList<Qube> res = new ArrayList<Qube>();
	
		if(c.getCount() <= 0)
			return res;
		
		c.moveToFirst();	
		do
		{
			ArrayList<String> motsClefs = new ArrayList<String>();
			for(String mot : c.getString(NUM_MOTS_CLEFS).split(";"))
				motsClefs.add(mot);
			
			res.add(new Qube(c.getInt(NUM_COL_ID), 
							c.getInt(NUM_COL_NUM),
							 c.getString(NUM_COL_QUESTION), 
							 c.getString(NUM_COL_PROP_1),
							 c.getString(NUM_COL_PROP_2), 
							 c.getString(NUM_COL_PROP_3),
							 c.getString(NUM_COL_PROP_4),
							 c.getInt(NUM_COL_NUM_REPONSE),
							 (c.getInt(NUM_COL_IS_QCM_BASIC) != 0 ? true : false),
							 (c.getInt(NUM_COL_IS_BLOCKED) != 0 ? true : false),
							 (c.getInt(NUM_COL_IS_PLAYABLE) != 0 ? true : false),
							 idNiveau, 
							 c.getInt(NUM_COL_SCORE),
							 c.getInt(NUM_COL_ETAT),
							 motsClefs));
		}while(c.moveToNext());
		
		return res;
	}
	
	/*
	public ArrayList<Qube> getQubesWithThemeId(int idTheme)
	{	
		String req = "SELECT * FROM " + TABLE_QUBE + 
				 " WHERE " + COL_ID_THEME_QUBE + " == " + idTheme + " ;";
		Cursor c = null;		
		try 
		{
			c = bdd.rawQuery(req, null);
		}
		catch(SQLiteException e) 
		{
			Log.v("error", e.getMessage());
		}
	
		ArrayList<Qube> res = new ArrayList<Qube>();
	
		if(c.getCount() <= 0)
			return res;
		
		c.moveToFirst();	
		do
		{
			res.add(new Qube(c.getInt(NUM_COL_ID), 
							c.getInt(NUM_COL_NUM),
							 c.getString(NUM_COL_QUESTION), 
							 c.getString(NUM_COL_PROP_1),
							 c.getString(NUM_COL_PROP_2), 
							 c.getString(NUM_COL_PROP_3),
							 c.getString(NUM_COL_PROP_4),
							 c.getInt(NUM_COL_NUM_REPONSE), 
							 (c.getInt(NUM_COL_IS_QCM_BASIC) != 0 ? true : false),
							 (c.getInt(NUM_COL_IS_BLOCKED) != 0 ? true : false),
							 (c.getInt(NUM_COL_IS_PLAYABLE) != 0 ? true : false),
							 c.getInt(NUM_COL_ID_NIVEAU), 
							 idTheme,
							 c.getInt(NUM_COL_SCORE),
							 c.getInt(NUM_COL_ETAT)));
		}while(c.moveToNext());
		
		return res;
	}*/
	
	public ArrayList<Qube> getAllQubes()
	{
		String req = "SELECT * FROM " + TABLE_QUBE + ";";
		Cursor c = null;		
		try 
		{
			c = bdd.rawQuery(req, null);
		}
		catch(SQLiteException e) 
		{
			Log.v("error", e.getMessage());
		}

		ArrayList<Qube> res = new ArrayList<Qube>();

		if(c.getCount() <= 0)
			return res;
		
		c.moveToFirst();	
		do
		{
			ArrayList<String> motsClefs = new ArrayList<String>();
			for(String mot : c.getString(NUM_MOTS_CLEFS).split(";"))
				motsClefs.add(mot);
			
			res.add(new Qube(c.getInt(NUM_COL_ID), 
							c.getInt(NUM_COL_NUM),
							 c.getString(NUM_COL_QUESTION), 
							 c.getString(NUM_COL_PROP_1),
							 c.getString(NUM_COL_PROP_2), 
							 c.getString(NUM_COL_PROP_3),
							 c.getString(NUM_COL_PROP_4),
							 c.getInt(NUM_COL_NUM_REPONSE),
							 (c.getInt(NUM_COL_IS_QCM_BASIC) != 0 ? true : false),
							 (c.getInt(NUM_COL_IS_BLOCKED) != 0 ? true : false),
							 (c.getInt(NUM_COL_IS_PLAYABLE) != 0 ? true : false),
							 c.getInt(NUM_COL_ID_NIVEAU), 
							 c.getInt(NUM_COL_SCORE),
							 c.getInt(NUM_COL_ETAT),
							 motsClefs));
		}while(c.moveToNext());
		
		return res;
	}
	
	public Qube getNextQube(Qube previousQube)
	{	
		String req = "SELECT * FROM " + TABLE_QUBE + 
					 " WHERE " + COL_ID_NIVEAU_QUBE+ " == " + previousQube.getIdNiveau() + 
					 " AND " +  COL_NUM_QUBE+ " == " + (previousQube.getNumQube()+1) + " ;";
		Cursor c = null;
		try 
		{
			c = bdd.rawQuery(req, null);
		}
		catch(SQLiteException e) 
		{
			Log.v("error", e.getMessage());
		}
		
		if (c.getCount() <= 0)
			return null;
 
		c.moveToFirst();
		
		ArrayList<String> motsClefs = new ArrayList<String>();
		for(String mot : c.getString(NUM_MOTS_CLEFS).split(";"))
			motsClefs.add(mot);
		
		Qube qube = new Qube(c.getInt(NUM_COL_ID),
							 c.getInt(NUM_COL_NUM),
							 c.getString(NUM_COL_QUESTION), 
							 c.getString(NUM_COL_PROP_1),
							 c.getString(NUM_COL_PROP_2), 
							 c.getString(NUM_COL_PROP_3),
							 c.getString(NUM_COL_PROP_4),
							 c.getInt(NUM_COL_NUM_REPONSE),
							 (c.getInt(NUM_COL_IS_QCM_BASIC) != 0 ? true : false),
							 (c.getInt(NUM_COL_IS_BLOCKED) != 0 ? true : false),
							 (c.getInt(NUM_COL_IS_PLAYABLE) != 0 ? true : false),
							 c.getInt(NUM_COL_ID_NIVEAU), 
							 c.getInt(NUM_COL_SCORE),
							 c.getInt(NUM_COL_ETAT),
							 motsClefs);
		c.close();
		
		return qube;
	}
	
	
	public int getNextNumQube(int idNiveau)
	{
		String req = "SELECT MAX(" + COL_NUM_QUBE + ") FROM QUBE WHERE " + COL_ID_NIVEAU_QUBE + " = " + idNiveau;
		
		Cursor c = null;
		try 
		{
			c = bdd.rawQuery(req, null);
		}
		catch(SQLiteException e) 
		{
			Log.v("error", e.getMessage());
		}
		
		if (c.getCount() <= 0)
			return 0;
 
		c.moveToFirst();
		
		return c.getInt(0) + 1;
		
	}
	
	
	public ArrayList<String> getAllKeywords()
	{
		ArrayList<String> res = new ArrayList<String>();
		String req = "SELECT " + COL_MOTS_CLEFS + " FROM " + TABLE_QUBE;
		
		Cursor c = null;
		try
		{
			c = bdd.rawQuery(req, null);
		}
		catch(SQLiteException e) 
		{
			Log.v("error", e.getMessage());
		}
		if(c.getCount() <= 0)
			return res;
		
		c.moveToFirst();
		do
		{
			for(String mot : c.getString(0).split(";"))
				if(!res.contains(mot))
					res.add(mot);
		
		}
		while(c.moveToNext());
		
		
		return res;
	}

	public ArrayList<Qube> getFicheInfoWithLevelID(int levelId)
	{	
		ArrayList<Qube> res = new ArrayList<Qube>();
		
		String req = "SELECT * FROM " + TABLE_QUBE + 
					 " WHERE " + COL_ID_NIVEAU_QUBE+ " == " + levelId + 
					 " AND " +  COL_NUM_REPONSE+ " == -1 ;";
		Cursor c = null;
		try 
		{
			c = bdd.rawQuery(req, null);
		}
		catch(SQLiteException e) 
		{
			Log.v("error", e.getMessage());
		}
		
		if(c.getCount() <= 0)
			return res;
		
		c.moveToFirst();
		do
		{
			ArrayList<String> motsClefs = new ArrayList<String>();
			for(String mot : c.getString(NUM_MOTS_CLEFS).split(";"))
				motsClefs.add(mot);
			
			Qube qube = new Qube(c.getInt(NUM_COL_ID),
					 c.getInt(NUM_COL_NUM),
					 c.getString(NUM_COL_QUESTION), 
					 c.getString(NUM_COL_PROP_1),
					 c.getString(NUM_COL_PROP_2), 
					 c.getString(NUM_COL_PROP_3),
					 c.getString(NUM_COL_PROP_4),
					 c.getInt(NUM_COL_NUM_REPONSE),
					 (c.getInt(NUM_COL_IS_QCM_BASIC) != 0 ? true : false),
					 (c.getInt(NUM_COL_IS_BLOCKED) != 0 ? true : false),
					 (c.getInt(NUM_COL_IS_PLAYABLE) != 0 ? true : false),
					 c.getInt(NUM_COL_ID_NIVEAU), 
					 c.getInt(NUM_COL_SCORE),
					 c.getInt(NUM_COL_ETAT),
					 motsClefs);
		
			res.add(qube);
	
		}while(c.moveToNext());
		c.close();
	
		return res;
		
	}
	
	
	public ArrayList<Qube> getQubeByKeyword(String word)
	{
		ArrayList<Qube> res = new ArrayList<Qube>();
		String req = "SELECT * FROM " + TABLE_QUBE + " WHERE " + COL_MOTS_CLEFS + " LIKE '%" + word + "%'";
		//String req = "SELECT * FROM " + TABLE_QUBE + " WHERE ';' || " + COL_MOTS_CLEFS +" || ';' LIKE '%;" + word + ";%'";
		
		Cursor c = null;
		try
		{
			c = bdd.rawQuery(req, null);
		}
		catch(SQLiteException e) 
		{
			Log.v("error", e.getMessage());
		}
		
		if (c.getCount() <= 0)
			return null;
 
		c.moveToFirst();

		do
		{
			ArrayList<String> motsClefs = new ArrayList<String>();
			for(String mot : c.getString(NUM_MOTS_CLEFS).split(";"))
				motsClefs.add(mot);
			
			res.add(new Qube(c.getInt(NUM_COL_ID), 
							c.getInt(NUM_COL_NUM),
							 c.getString(NUM_COL_QUESTION), 
							 c.getString(NUM_COL_PROP_1),
							 c.getString(NUM_COL_PROP_2), 
							 c.getString(NUM_COL_PROP_3),
							 c.getString(NUM_COL_PROP_4),
							 c.getInt(NUM_COL_NUM_REPONSE),
							 (c.getInt(NUM_COL_IS_QCM_BASIC) != 0 ? true : false),
							 (c.getInt(NUM_COL_IS_BLOCKED) != 0 ? true : false),
							 (c.getInt(NUM_COL_IS_PLAYABLE) != 0 ? true : false),
							 c.getInt(NUM_COL_ID_NIVEAU), 
							 c.getInt(NUM_COL_SCORE),
							 c.getInt(NUM_COL_ETAT),
							 motsClefs));
		
		}
		while(c.moveToNext());
		
		return res;
	}

}