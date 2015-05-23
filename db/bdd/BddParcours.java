package com.example.db.bdd;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.db.object.Parcours;

public class BddParcours 
{
	private static final String TABLE_PARCOURS 	= "parcours";
	
	private static final String COL_ID_PARCOURS = "idParcours";
	private static final int 	NUM_COL_ID 		= 0;
	
	private static final String COL_NOM_PARCOURS = "nomParcours";
	private static final int 	NUM_COL_NOM 	= 1;
	 
	private SQLiteDatabase bdd;
	private BddBotacatching bddBotacatching;
 
	public BddParcours(Context context)
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
	
	public long insertParcours(Parcours parcours)
	{
		ContentValues values = new ContentValues();
		
		values.put(COL_NOM_PARCOURS, parcours.getName());
		return bdd.insert(TABLE_PARCOURS, null, values);
	}
 
	public int updateParcours(int id, Parcours parcours)
	{
		ContentValues values = new ContentValues();
		
		values.put(COL_NOM_PARCOURS, parcours.getName());
		
		return bdd.update(TABLE_PARCOURS, values, COL_ID_PARCOURS + " = " +id, null);
	}
 
	public int removeParcoursWithID(int id)
	{
		return bdd.delete(TABLE_PARCOURS, COL_ID_PARCOURS + " = " +id, null);
	}
	
	public Parcours getParcoursWithId(int id)
	{	
		String req = "SELECT * FROM " + TABLE_PARCOURS + 
				 " WHERE " + COL_ID_PARCOURS + " == " + id + " ;";
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
		Parcours parcours = new Parcours(id, c.getString(NUM_COL_NOM));
		c.close();

		return parcours;
	}
 
	public ArrayList<Parcours> getAllParcours()
	{
		String req = "SELECT * FROM " + TABLE_PARCOURS + ";";
		Cursor c = null;		
		try 
		{
			c = bdd.rawQuery(req, null);
		}
		catch(SQLiteException e) 
		{
			Log.v("error", e.getMessage());
		}

		ArrayList<Parcours> res = new ArrayList<Parcours>();

		if(c.getCount() <= 0)
			return res;
		
		c.moveToFirst();	
		do
		{
			res.add(new Parcours(c.getInt(NUM_COL_ID), c.getString(NUM_COL_NOM)));
		}while(c.moveToNext());
		
		return res;
	}
}
