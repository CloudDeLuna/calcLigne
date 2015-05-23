package com.example.db.object;

import java.util.ArrayList;

public class Liaison 
{
	private int idQubeOuZoneObs;
	private ArrayList<Integer> qubeOuZoneObsList;
	private ArrayList<String> annotationsZoneObs;
	private boolean idIsQube = true;

	public Liaison() 
	{
		this.qubeOuZoneObsList = new ArrayList<Integer>();
	}
	
	public Liaison(int id, ArrayList<Integer> qubeOuZoneObsList, ArrayList<String> annotationsZoneObs, boolean idIsQube) 
	{
		this.idQubeOuZoneObs = id;
		this.qubeOuZoneObsList = qubeOuZoneObsList;
		this.setAnnotationsZoneObs(annotationsZoneObs);
		this.idIsQube = idIsQube;
	}
	
	/******************** SETTERS ***********************/

	public void setIdQubeOuZoneObs(int idQubeOuZoneObs) 
	{
		this.idQubeOuZoneObs = idQubeOuZoneObs;
	}

	public void setQubeOuZoneObsList(ArrayList<Integer> qubeOuZoneObsList) 
	{
		this.qubeOuZoneObsList = qubeOuZoneObsList;
	}
	
	public void setAnnotationsZoneObs(ArrayList<String> annotationsZoneObs) 
	{
		this.annotationsZoneObs = annotationsZoneObs;
	}

	public void setIdIsQube(boolean idIsQube) 
	{
		this.idIsQube = idIsQube;
	}

	/******************** GETTERS ***********************/

	public int getIdQubeOuZoneObs() 
	{
		return idQubeOuZoneObs;
	}

	public ArrayList<Integer> getQubeOuZoneObsList() 
	{
		return qubeOuZoneObsList;
	}
	
	public ArrayList<String> getAnnotationsZoneObs() 
	{
		return annotationsZoneObs;
	}
	
	public boolean getIdIsQube()
	{
		return idIsQube;
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Liaison other = (Liaison) obj;
		if (annotationsZoneObs == null) {
			if (other.annotationsZoneObs != null)
				return false;
		} else if (!annotationsZoneObs.equals(other.annotationsZoneObs))
			return false;
		if (idIsQube != other.idIsQube)
			return false;
		if (idQubeOuZoneObs != other.idQubeOuZoneObs)
			return false;
		if (qubeOuZoneObsList == null) {
			if (other.qubeOuZoneObsList != null)
				return false;
		} else if (!qubeOuZoneObsList.equals(other.qubeOuZoneObsList))
			return false;
		return true;
	}
}
