package com.example.db.bdd;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.db.object.Liaison;

public class BddLiaison 
{ 
	private static final String TABLE_LIAISON 			= "liaisonQubeZoneObs";
	private static final String COL_ID_QUBE_LIAISON		= "idQube";
	private static final int 	NUM_COL_ID_QUBE 		= 0;
	
	private static final String COL_ID_ZONE_OBS_LIAISON	= "idZoneObservation";
	private static final int 	NUM_COL_ID_ZONE_OBS 	= 1;
	
	private static final String COL_ANNOTATION_ZO   	= "annotationZoneObs";
	private static final int 	NUM_COL_ANNOTATION		= 2;
 
	private SQLiteDatabase bdd;
	private BddBotacatching bddBotacatching;
 
	public BddLiaison(Context context)
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
	
	public long insertLiaison(int idQube, int idZoneObs, String annotationZoneObs)
	{
		String req = "SELECT * FROM " + TABLE_LIAISON + 
				" WHERE " + COL_ID_QUBE_LIAISON + " == " + idQube + 
				" AND " + COL_ID_ZONE_OBS_LIAISON + " == " + idZoneObs + " ;";
		
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
			return -1;//Le tuple est deja dans la BD
		
		ContentValues values = new ContentValues();
		
		values.put(COL_ID_QUBE_LIAISON, idQube);
		values.put(COL_ID_ZONE_OBS_LIAISON, idZoneObs);
		values.put(COL_ANNOTATION_ZO, annotationZoneObs);
		
		return bdd.insert(TABLE_LIAISON, null, values);
	}
	
	public int removeAllLiaisonOfQube(int idQube)
	{
		return bdd.delete(TABLE_LIAISON, COL_ID_QUBE_LIAISON + " = " + idQube, null);
	}
	
	public int removeAllLiaisonOfZoneObs(int idZoneObs)
	{
		return bdd.delete(TABLE_LIAISON, COL_ID_ZONE_OBS_LIAISON + " = " + idZoneObs, null);
	}
 
	public int removeLiaison(int idQube, int idZoneObs)
	{
		return bdd.delete(TABLE_LIAISON, COL_ID_QUBE_LIAISON + " = " + idQube + " AND "  
										+ COL_ID_ZONE_OBS_LIAISON + " = " + idZoneObs, null);
	}
	
	public Liaison getZoneObsWithQubeId(int id)
	{	
		String req = "SELECT * FROM " + TABLE_LIAISON + 
					 " WHERE " + COL_ID_QUBE_LIAISON + " == " + id + " ;";
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
 
		ArrayList<Integer> zoneObs = new ArrayList<Integer>();
		ArrayList<String> annotations = new ArrayList<String>();
		
		c.moveToFirst();	
		do
		{
			zoneObs.add(c.getInt(NUM_COL_ID_ZONE_OBS));
			annotations.add(c.getString(NUM_COL_ANNOTATION));
		}while(c.moveToNext());
		
		c.close();
		Liaison liaison = new Liaison(id, zoneObs, annotations, true);
		return liaison;
	}
	
	public Liaison getQubesWithZoneObsId(int id)
	{	
		String req = "SELECT * FROM " + TABLE_LIAISON + 
					 " WHERE " + COL_ID_ZONE_OBS_LIAISON + " == " + id + " ;";
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
 
		ArrayList<Integer> qubes = new ArrayList<Integer>();
		ArrayList<String> annotations = new ArrayList<String>();
		
		c.moveToFirst();	
		do
		{
			qubes.add(c.getInt(NUM_COL_ID_QUBE));
			annotations.add(c.getString(NUM_COL_ANNOTATION));
		}while(c.moveToNext());
		
		c.close();
		Liaison liaison = new Liaison(id, qubes, annotations, false);
		return liaison;
	}
	
	public int getNbLiaisonWithIdZO(int id)
	{
		String req = "SELECT COUNT(*) FROM " + TABLE_LIAISON + 
				 " WHERE " + COL_ID_ZONE_OBS_LIAISON + " == " + id + " ;";
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
		int nbLiaison = c.getInt(0);
		
		c.close();
		return nbLiaison;
	}
}