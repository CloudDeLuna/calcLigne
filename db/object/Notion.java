package com.example.db.object;


public class Notion 
{
	private int idNotion;
	private String name;
	private int idParcours;
	
	public Notion () 
	{
		this.name = "";
	}
	
	public Notion (int id, String name, int idParcours)
	{
		this.idNotion = id;
		this.name = name;
		this.idParcours = idParcours;
	}
	
	/******************** SETTERS ***********************/
	
	public void setId(int id)
	{
		this.idNotion = id;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setIdParcours(int idParcours)
	{
		this.idParcours = idParcours;
	}
	
	/******************** GETTERS ***********************/
	
	public int getId()
	{
		return this.idNotion;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getIdParcours()
	{
		return this.idParcours;
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
		Notion other = (Notion) obj;
		if (idNotion != other.idNotion)
			return false;
		if (name == null) 
		{
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
