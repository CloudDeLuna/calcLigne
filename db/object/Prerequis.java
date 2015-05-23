package com.example.db.object;

import java.util.ArrayList;

public class Prerequis 
{
	private int idNotions;
	private ArrayList<Integer> notionsPrerequises;

	public Prerequis() 
	{
		this.notionsPrerequises = new ArrayList<Integer>();
	}
	
	public Prerequis(int id, ArrayList<Integer> notionsPrerequises) 
	{
		this.idNotions = id;
		this.notionsPrerequises = notionsPrerequises;
	}
	
	/******************** SETTERS ***********************/

	public void setIdNotions(int idNotions) 
	{
		this.idNotions = idNotions;
	}

	public void setNotionsPrerequises(ArrayList<Integer> notionsPrerequises) 
	{
		this.notionsPrerequises = notionsPrerequises;
	}

	/******************** GETTERS ***********************/

	public int getIdNotions() 
	{
		return idNotions;
	}

	public ArrayList<Integer> getNotionsPrerequises() 
	{
		return notionsPrerequises;
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
		Prerequis other = (Prerequis) obj;
		if (idNotions != other.idNotions)
			return false;
		if (notionsPrerequises == null) {
			if (other.notionsPrerequises != null)
				return false;
		} else if (!notionsPrerequises.equals(other.notionsPrerequises))
			return false;
		return true;
	}
}
