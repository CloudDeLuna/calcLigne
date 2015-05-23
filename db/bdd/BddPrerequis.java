package com.example.db.bdd;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.db.object.Prerequis;

public class BddPrerequis 
{ 
	private static final String TABLE_PREREQUIS			= "prerequis";
	
	private static final String COL_ID_NOTION			= "idNotion";
	private static final int 	NUM_COL_ID 				= 0;
	
	private static final String COL_ID_NOTION_PERE		= "idNotionPere";
	private static final int 	NUM_COL_ID_PREREQUIS 	= 1;
 
	private SQLiteDatabase bdd;
	private BddBotacatching bddBotacatching;
 
	public BddPrerequis(Context context)
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
	
	public long insertPrerequis(int idNotion, int idNotionPere)
	{	
		if (isPrerequisExists(idNotion, idNotionPere))
			return -1;
		
		ContentValues values = new ContentValues();
		
		values.put(COL_ID_NOTION, idNotion);
		values.put(COL_ID_NOTION_PERE, idNotionPere);
		return bdd.insert(TABLE_PREREQUIS, null, values);
	}
	
	public int removeAllPrerequisWithIdAsPere(int idNotionPere)
	{
		return bdd.delete(TABLE_PREREQUIS, COL_ID_NOTION_PERE + " = " + idNotionPere, null);
	}
	
	public int removeAllPrerequisOf(int idNotion)
	{
		return bdd.delete(TABLE_PREREQUIS, COL_ID_NOTION + " = " + idNotion, null);
	}
 
	public int removePrerequis(int idNotion, int idNotionPere)
	{
		return bdd.delete(TABLE_PREREQUIS, COL_ID_NOTION + " = " + idNotion + " AND "  
											+ COL_ID_NOTION_PERE + " = " + idNotionPere, null);
	}
	
	public boolean isPrerequisExists(int idNotion, int idNotionPere)
	{
		String req = "SELECT * FROM " + TABLE_PREREQUIS + 
				" WHERE " + COL_ID_NOTION + " == " + idNotion + 
				" AND " + COL_ID_NOTION_PERE + " == " + idNotionPere + " ;";
		
		Cursor c = null;		
		try 
		{
			c = bdd.rawQuery(req, null);
		}
		catch(SQLiteException e) 
		{
			Log.v("error", e.getMessage());
		}
		
		if (c.getCount() > 0)
			return true;//Le tuple est deja dans la BD
		else
			return false;
	}
	
	public Prerequis getPrerequisWithId(int id)
	{	
		String req = "SELECT * FROM " + TABLE_PREREQUIS + 
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
 
		ArrayList<Integer> notionsPrerequises = new ArrayList<Integer>();
		c.moveToFirst();	
		do
		{
			notionsPrerequises.add(c.getInt(NUM_COL_ID_PREREQUIS));
		}while(c.moveToNext());
		
		c.close();
		Prerequis prerequis = new Prerequis(id, notionsPrerequises);
		return prerequis;
	}
	
	
	public ArrayList<Integer> getNotionsIdWithPrerequis(int notionId)
	{	
		String req = "SELECT * FROM " + TABLE_PREREQUIS + 
					 " WHERE " + COL_ID_NOTION_PERE + " == " + notionId + " ;";
		Cursor c = null;		
		try 
		{
			c = bdd.rawQuery(req, null);
		}
		catch(SQLiteException e) 
		{
			Log.v("error", e.getMessage());
		}
		
		ArrayList<Integer> notions = new ArrayList<Integer>();
		if (c.getCount() <= 0)
			return notions;
 
		
		c.moveToFirst();	
		do
		{
			notions.add(c.getInt(NUM_COL_ID));
		}while(c.moveToNext());
		
		c.close();
	
		return notions;
	}
}