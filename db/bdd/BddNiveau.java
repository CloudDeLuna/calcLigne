package com.example.db.bdd;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.db.object.Niveau;

public class BddNiveau 
{
	private static final String TABLE_NIVEAU 	= "niveau";
	
	private static final String COL_ID_NIVEAU 	= "idNiveau";
	private static final int 	NUM_COL_ID 		= 0;
	
	private static final String COL_NUM_NIVEAU 	= "numNiveau";
	private static final int 	NUM_COL_NUM 	= 1;

	private static final String COL_IS_BLOCKED_NIVEAU 	= "isBlocked";
	private static final int 	NUM_COL_IS_BLOCKED 		= 2;
	
	private static final String COL_IS_PLAYABLE_NIVEAU 	= "isPlayable";
	private static final int 	NUM_COL_IS_PLAYABLE 	= 3;
	
	private static final String COL_SCORE_TO_UNLOCK 	= "scoreToUnlock";
	private static final int 	NUM_COL_SCORE_TO_UNLOCK	= 4;
	
	private static final String COL_SCORE_ACTUEL_NIVEAU	= "scoreActuel";
	private static final int 	NUM_COL_SCORE_ACTUEL	= 5;
	
	private static final String COL_IS_RANDOM			= "isRandom";
	private static final int 	NUM_COL_IS_RANDOM		= 6;

	private static final String COL_ID_NOTION_NIVEAU 	= "idNotion";
	private static final int 	NUM_COL_NOTION 			= 7;

	
	private SQLiteDatabase bdd;
	private BddBotacatching bddBotacatching;
 
	public BddNiveau(Context context)
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
	
	public long insertNiveau(Niveau niveau)
	{
		ContentValues values = new ContentValues();
		
		values.put(COL_NUM_NIVEAU, niveau.getNumNiveau());
		values.put(COL_IS_BLOCKED_NIVEAU, niveau.isBlocked());
		values.put(COL_IS_PLAYABLE_NIVEAU, niveau.isPlayable());
		values.put(COL_SCORE_TO_UNLOCK, niveau.getScoreToUnlock());
		values.put(COL_SCORE_ACTUEL_NIVEAU, niveau.getScoreActuel());
		values.put(COL_IS_RANDOM, niveau.isRandom());
		values.put(COL_ID_NOTION_NIVEAU, niveau.getIdNotion());
		return bdd.insert(TABLE_NIVEAU, null, values);
	}
 
	public int updateNiveau(int id, Niveau niveau)
	{
		ContentValues values = new ContentValues();
		
		values.put(COL_NUM_NIVEAU, niveau.getNumNiveau());
		values.put(COL_IS_BLOCKED_NIVEAU, niveau.isBlocked());
		values.put(COL_IS_PLAYABLE_NIVEAU, niveau.isPlayable());
		values.put(COL_SCORE_TO_UNLOCK, niveau.getScoreToUnlock());
		values.put(COL_SCORE_ACTUEL_NIVEAU, niveau.getScoreActuel());
		values.put(COL_IS_RANDOM, niveau.isRandom());
		values.put(COL_ID_NOTION_NIVEAU, niveau.getIdNotion());
		
		return bdd.update(TABLE_NIVEAU, values, COL_ID_NIVEAU + " = " + id, null);
	}
 
	public int removeNiveauWithID(int id)
	{
		return bdd.delete(TABLE_NIVEAU, COL_ID_NIVEAU + " = " +id, null);
	}
	
	public int removeNiveauWithNotionId(int id)
	{
		return bdd.delete(TABLE_NIVEAU, COL_ID_NOTION_NIVEAU + " = " + id, null);
	}
	
	public Niveau getNiveauWithId(int id)
	{	
		String req = "SELECT * FROM " + TABLE_NIVEAU + 
					 " WHERE " + COL_ID_NIVEAU + " == " + id + " ;";
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
		Niveau niveau = new Niveau(c.getInt(NUM_COL_ID), c.getInt(NUM_COL_NUM), 
									(c.getInt(NUM_COL_IS_BLOCKED) != 0 ? true : false), 
									(c.getInt(NUM_COL_IS_PLAYABLE) != 0 ? true : false), 
									c.getInt(NUM_COL_SCORE_TO_UNLOCK),
									c.getInt(NUM_COL_SCORE_ACTUEL),
									(c.getInt(NUM_COL_IS_RANDOM) != 0 ? true : false),
									c.getInt(NUM_COL_NOTION));
		c.close();
		
		return niveau;
	}
	
	public ArrayList<Niveau> getNiveauxWithNotionId(int notionId)
	{
		String req = "SELECT * FROM " + TABLE_NIVEAU + 
				   " WHERE " + COL_ID_NOTION_NIVEAU + " == " + notionId + " ORDER BY " + COL_NUM_NIVEAU + " ASC;";
		Cursor c = null;		
		try 
		{
			c = bdd.rawQuery(req, null);
		}
		catch(SQLiteException e) 
		{
			Log.v("error", e.getMessage());
		}

		ArrayList<Niveau> res = new ArrayList<Niveau>();

		if(c.getCount() <= 0)
			return res;
		
		c.moveToFirst();	
		do
		{
			res.add(new Niveau(c.getInt(NUM_COL_ID), c.getInt(NUM_COL_NUM), 
					(c.getInt(NUM_COL_IS_BLOCKED) != 0 ? true : false), 
					(c.getInt(NUM_COL_IS_PLAYABLE) != 0 ? true : false),
					c.getInt(NUM_COL_SCORE_TO_UNLOCK),
					c.getInt(NUM_COL_SCORE_ACTUEL), 
					(c.getInt(NUM_COL_IS_RANDOM) != 0 ? true : false), notionId));
		}while(c.moveToNext());
		
		return res;
	}
	
	public Niveau getNiveauWithNotionIdAndNumLevel(int notionId, int numLevel)
	{
		String req = "SELECT * FROM " + TABLE_NIVEAU + 
				   " WHERE " + COL_ID_NOTION_NIVEAU + " == " + notionId + 
				   " AND " + COL_NUM_NIVEAU + " == " + numLevel + ";";
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
			return null;
		
		c.moveToFirst();	
		Niveau res = new Niveau(c.getInt(NUM_COL_ID), c.getInt(NUM_COL_NUM), 
					(c.getInt(NUM_COL_IS_BLOCKED) != 0 ? true : false), 
					(c.getInt(NUM_COL_IS_PLAYABLE) != 0 ? true : false),
					c.getInt(NUM_COL_SCORE_TO_UNLOCK),
					c.getInt(NUM_COL_SCORE_ACTUEL), 
					(c.getInt(NUM_COL_IS_RANDOM) != 0 ? true : false), notionId);
		
		return res;
	}	
	
	public ArrayList<Niveau> getAllNiveaux()
	{
		String req = "SELECT * FROM " + TABLE_NIVEAU + " ORDER BY "+ COL_ID_NOTION_NIVEAU + " ASC;";
		Cursor c = null;		
		try 
		{
			c = bdd.rawQuery(req, null);
		}
		catch(SQLiteException e) 
		{
			Log.v("error", e.getMessage());
		}

		ArrayList<Niveau> res = new ArrayList<Niveau>();

		if(c.getCount() <= 0)
			return res;
		
		c.moveToFirst();	
		do
		{
			res.add(new Niveau(c.getInt(NUM_COL_ID), c.getInt(NUM_COL_NUM), 
					(c.getInt(NUM_COL_IS_BLOCKED) != 0 ? true : false), 
					(c.getInt(NUM_COL_IS_PLAYABLE) != 0 ? true : false),
					c.getInt(NUM_COL_SCORE_TO_UNLOCK),
					c.getInt(NUM_COL_SCORE_ACTUEL),
					(c.getInt(NUM_COL_IS_RANDOM) != 0 ? true : false),
					c.getInt(NUM_COL_NOTION)));
		}while(c.moveToNext());
		
		return res;
	}
}
