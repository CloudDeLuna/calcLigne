package com.example.db.bdd;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.db.object.ZoneObservation;

public class BddZoneObservation 
{
	private static final String TABLE_ZONE_OBS 		= "zoneObservation";

	private static final String COL_ID_ZONE_OBS		= "idZoneObs";
	private static final int 	NUM_COL_ID 			= 0;

	private static final String COL_PATH_ZONE_OBS	= "path";
	private static final int 	NUM_COL_PATH 		= 1;

	private static final String COL_LONGITUDE		= "longitude";
	private static final int 	NUM_COL_LONGITUDE	= 2;

	private static final String COL_LATITUDE		= "latitude";
	private static final int 	NUM_COL_LATITUDE	= 3;
	
	private SQLiteDatabase bdd;
	private BddBotacatching bddBotacatching;
 
	public BddZoneObservation(Context context)
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
	
	public long insertZoneObservation(ZoneObservation zone)
	{
		ContentValues values = new ContentValues();
		
		values.put(COL_PATH_ZONE_OBS, zone.getPathToImg());
		values.put(COL_LONGITUDE, zone.getLongitude());
		values.put(COL_LATITUDE, zone.getLatitude());

		return bdd.insert(TABLE_ZONE_OBS, null, values);
	}
 
	public int updateZoneObservation(int id, ZoneObservation zone)
	{
		ContentValues values = new ContentValues();
		
		values.put(COL_PATH_ZONE_OBS, zone.getPathToImg());
		values.put(COL_LONGITUDE, zone.getLongitude());
		values.put(COL_LATITUDE, zone.getLatitude());
		
		return bdd.update(TABLE_ZONE_OBS, values, COL_ID_ZONE_OBS + " = " + id, null);
	}
 
	public int removeZoneObservationWithID(int id)
	{
		return bdd.delete(TABLE_ZONE_OBS, COL_ID_ZONE_OBS + " = " +id, null);
	}
	
	public ZoneObservation getZoneObservationWithName(String name)
	{
		String req = "SELECT * FROM " + TABLE_ZONE_OBS + ";";
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
		do
		{
			String[] path = c.getString(NUM_COL_PATH).split("/");
			String nom = path[path.length-1];
			
			if(nom.equals(name))
			{
				return new ZoneObservation(c.getInt(NUM_COL_ID), c.getString(NUM_COL_PATH),
						c.getDouble(NUM_COL_LONGITUDE), 
						c.getDouble(NUM_COL_LATITUDE));
			}
			
		}while(c.moveToNext());
		
		return null;
	}
	
	public ZoneObservation getZoneObservationWithId(int id)
	{	
		String req = "SELECT * FROM " + TABLE_ZONE_OBS + 
					 " WHERE " + COL_ID_ZONE_OBS + " == " + id + " ;";
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
		ZoneObservation zone = new ZoneObservation(c.getInt(NUM_COL_ID), c.getString(NUM_COL_PATH),
													c.getDouble(NUM_COL_LONGITUDE), 
													c.getDouble(NUM_COL_LATITUDE));
		c.close();
		
		return zone;
	}
	
	public ArrayList<ZoneObservation> getAllZonesObservations()
	{
		String req = "SELECT * FROM " + TABLE_ZONE_OBS + ";";
		Cursor c = null;		
		try 
		{
			c = bdd.rawQuery(req, null);
		}
		catch(SQLiteException e) 
		{
			Log.v("error", e.getMessage());
		}

		ArrayList<ZoneObservation> res = new ArrayList<ZoneObservation>();

		if(c.getCount() <= 0)
			return res;
		
		c.moveToFirst();	
		do
		{
			res.add(new ZoneObservation(c.getInt(NUM_COL_ID), c.getString(NUM_COL_PATH),
					c.getDouble(NUM_COL_LONGITUDE), 
					c.getDouble(NUM_COL_LATITUDE)));
		}while(c.moveToNext());
		
		return res;
	}
}
