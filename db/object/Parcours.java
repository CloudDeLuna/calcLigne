package com.example.db.object;


public class Parcours 
{
	private int idParcours;
	private String name;

	public Parcours () 
	{
		this.name = "";
	}
	
	public Parcours (int id, String name)
	{
		this.idParcours = id;
		this.name = name;
	}
	
	/******************** SETTERS ***********************/
	
	public void setId(int id)
	{
		this.idParcours = id;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	/******************** GETTERS ***********************/
	
	public int getId()
	{
		return this.idParcours;
	}
	
	public String getName()
	{
		return this.name;
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
		Parcours other = (Parcours) obj;
		if (idParcours != other.idParcours)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
