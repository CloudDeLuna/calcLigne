package com.example.db.bdd;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.db.object.Joueur;

public class BddJoueur 
{
	private static final String TABLE_JOUEUR 	= "joueur";

	private static final String COL_ID_JOUEUR		= "idJoueur";
	private static final int 	NUM_COL_ID 			= 0;

	private static final String COL_PSEUDO_JOUEUR	= "pseudoJoueur";
	private static final int 	NUM_COL_PSEUDO		= 1;

	private static final String COL_NIVEAU_JOUEUR	= "niveau";
	private static final int 	NUM_COL_NIVEAU 		= 2;

	private static final String COL_EXP_JOUEUR		= "experience";
	private static final int 	NUM_COL_EXP			= 3;

	private static final String COL_MODE_JEU		= "modeDeJeu";
	private static final int 	NUM_COL_MODE		= 4;

	private static final String COL_SON_ENABLE		= "son";
	private static final int 	NUM_COL_SON			= 5;
	
	private static final String COL_IS_PLAYING		= "isPlaying";
	private static final int 	NUM_COL_IS_PLAYING	= 6;
	
	private static final String COL_WARNING_FI		= "warningFI";
	private static final int 	NUM_COL_WARNING_FI	= 7;
	
	private SQLiteDatabase bdd;
	private BddBotacatching bddBotacatching;
 
	public BddJoueur(Context context)
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
	
	public long insertJoueur(Joueur joueur)
	{
		ContentValues values = new ContentValues();
		
		values.put(COL_PSEUDO_JOUEUR, joueur.getPseudo());
		values.put(COL_NIVEAU_JOUEUR, joueur.getNiveau());
		values.put(COL_EXP_JOUEUR, joueur.getExperience());
		values.put(COL_MODE_JEU, joueur.getModeDeJeu());
		values.put(COL_SON_ENABLE, joueur.isSonEnable());
		values.put(COL_IS_PLAYING, joueur.isPlaying());
		values.put(COL_WARNING_FI, joueur.isWarningVisible());

		return bdd.insert(TABLE_JOUEUR, null, values);
	}
 
	public int updateJoueur(int id, Joueur joueur)
	{
		ContentValues values = new ContentValues();
		
		values.put(COL_PSEUDO_JOUEUR, joueur.getPseudo());
		values.put(COL_NIVEAU_JOUEUR, joueur.getNiveau());
		values.put(COL_EXP_JOUEUR, joueur.getExperience());
		values.put(COL_MODE_JEU, joueur.getModeDeJeu());
		values.put(COL_SON_ENABLE, joueur.isSonEnable());
		values.put(COL_IS_PLAYING, joueur.isPlaying());
		values.put(COL_WARNING_FI, joueur.isWarningVisible());

		return bdd.update(TABLE_JOUEUR, values, COL_ID_JOUEUR + " = " + id, null);
	}
 
	public int removeJoueurWithID(int id)
	{
		return bdd.delete(TABLE_JOUEUR, COL_ID_JOUEUR + " = " +id, null);
	}
	
	public Joueur getJoueurWithId(int id)
	{	
		String req = "SELECT * FROM " + TABLE_JOUEUR + 
					 " WHERE " + COL_ID_JOUEUR + " == " + id + " ;";
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
		Joueur joueur = new Joueur(c.getInt(NUM_COL_ID), c.getString(NUM_COL_PSEUDO),
								 c.getInt(NUM_COL_NIVEAU), c.getInt(NUM_COL_EXP),
								 c.getInt(NUM_COL_MODE),
								 (c.getInt(NUM_COL_SON) != 0 ? true : false),
								 (c.getInt(NUM_COL_IS_PLAYING) != 0 ? true : false),
								 (c.getInt(NUM_COL_WARNING_FI) != 0 ? true : false));
		c.close();
		
		return joueur;
	}
	
	public Joueur getJoueurWhoIsPlaying()
	{	
		String req = "SELECT * FROM " + TABLE_JOUEUR + 
					 " WHERE " + COL_IS_PLAYING + " == " + 1 + " ;";
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
		Joueur joueur = new Joueur(c.getInt(NUM_COL_ID), c.getString(NUM_COL_PSEUDO),
								 c.getInt(NUM_COL_NIVEAU), c.getInt(NUM_COL_EXP),
								 c.getInt(NUM_COL_MODE),
								 (c.getInt(NUM_COL_SON) != 0 ? true : false),
								 true,
								 (c.getInt(NUM_COL_WARNING_FI) != 0 ? true : false));
		c.close();
		
		return joueur;
	}
	
	public ArrayList<Joueur> getAllJoueurs()
	{
		String req = "SELECT * FROM " + TABLE_JOUEUR + ";";
		Cursor c = null;		
		try 
		{
			c = bdd.rawQuery(req, null);
		}
		catch(SQLiteException e) 
		{
			Log.v("error", e.getMessage());
		}

		ArrayList<Joueur> res = new ArrayList<Joueur>();

		if(c.getCount() <= 0)
			return res;
		
		c.moveToFirst();	
		do
		{
			res.add(new Joueur(c.getInt(NUM_COL_ID), c.getString(NUM_COL_PSEUDO),
					 c.getInt(NUM_COL_NIVEAU), c.getInt(NUM_COL_EXP),
					 c.getInt(NUM_COL_MODE),
					 (c.getInt(NUM_COL_SON) != 0 ? true : false),
					 (c.getInt(NUM_COL_IS_PLAYING) != 0 ? true : false),
					 (c.getInt(NUM_COL_WARNING_FI) != 0 ? true : false)));
		}while(c.moveToNext());
		
		return res;
	}
}
