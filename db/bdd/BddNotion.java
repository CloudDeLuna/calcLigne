package com.example.db.bdd;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.db.object.Notion;

public class BddNotion 
{ 
	private static final String TABLE_NOTION 	= "notion";
	private static final String COL_ID_NOTION 	= "idNotion";
	private static final int 	NUM_COL_ID 		= 0;
	
	private static final String COL_NOM_NOTION	= "nomNotion";
	private static final int 	NUM_COL_NOM 	= 1;
	
	private static final String COL_ID_PARCOURS_NOTION 	= "idParcours";
	private static final int 	NUM_COL_PARCOURS = 2;
 
	private SQLiteDatabase bdd;
	private BddBotacatching bddBotacatching;
 
	public BddNotion(Context context)
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
	
	public long insertNotion(Notion notion)
	{
		ContentValues values = new ContentValues();
		
		values.put(COL_NOM_NOTION, notion.getName());
		values.put(COL_ID_PARCOURS_NOTION, notion.getIdParcours());
		return bdd.insert(TABLE_NOTION, null, values);
	}
 
	public int updateNotion(int id, Notion notion)
	{
		ContentValues values = new ContentValues();
		
		values.put(COL_NOM_NOTION, notion.getName());
		values.put(COL_ID_PARCOURS_NOTION, notion.getIdParcours());
		
		return bdd.update(TABLE_NOTION, values, COL_ID_NOTION + " = " +id, null);
	}
 
	public int removeNotionWithID(int id)
	{
		return bdd.delete(TABLE_NOTION, COL_ID_NOTION + " = " +id, null);
	}
	
	public Notion getNotionWithId(int id)
	{	
		String req = "SELECT * FROM " + TABLE_NOTION + 
					 " WHERE " + COL_ID_NOTION + " == " + id + " ;";
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
		Notion notion = new Notion(id, c.getString(NUM_COL_NOM),
									   c.getInt(NUM_COL_PARCOURS));
		c.close();
		
		return notion;
	}
	
	public Notion getNotionWithName(String name)
	{	
		String req = "SELECT * FROM " + TABLE_NOTION + 
					 " WHERE " + COL_NOM_NOTION + " = '" + name + "' ;";
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
		Notion notion = new Notion(c.getInt(NUM_COL_ID), name, c.getInt(NUM_COL_PARCOURS));
		c.close();
		
		return notion;
	}
	
	public ArrayList<Notion> getNotionsWithParcoursId(int parcoursId)
	{
		String req = "SELECT * FROM " + TABLE_NOTION + 
				   " WHERE " + COL_ID_PARCOURS_NOTION + " == " + parcoursId + " ;";
		Cursor c = null;		
		try 
		{
			c = bdd.rawQuery(req, null);
		}
		catch(SQLiteException e) 
		{
			Log.v("error", e.getMessage());
		}

		ArrayList<Notion> res = new ArrayList<Notion>();

		if(c.getCount() <= 0)
			return res;
		
		c.moveToFirst();	
		do
		{
			res.add(new Notion(c.getInt(NUM_COL_ID), c.getString(NUM_COL_NOM),
													 parcoursId));
		}while(c.moveToNext());
		
		return res;
	}
	
	public ArrayList<Notion> getAllNotions()
	{
		String req = "SELECT * FROM " + TABLE_NOTION + ";";
		Cursor c = null;		
		try 
		{
			c = bdd.rawQuery(req, null);
		}
		catch(SQLiteException e) 
		{
			Log.v("error", e.getMessage());
		}

		ArrayList<Notion> res = new ArrayList<Notion>();

		if(c.getCount() <= 0)
			return res;
		
		c.moveToFirst();	
		do
		{
			res.add(new Notion(c.getInt(NUM_COL_ID), c.getString(NUM_COL_NOM),
													 c.getInt(NUM_COL_PARCOURS)));
		}while(c.moveToNext());
		
		return res;
	}
}