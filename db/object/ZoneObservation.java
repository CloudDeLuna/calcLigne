package com.example.db.object;

public class ZoneObservation 
{
	private int zoneId;
	private String pathToImg;
	private double longitude;
	private double latitude;
	private String name;
	
	public ZoneObservation()
	{
		this.pathToImg = "";
		this.name = "";
	}

	public ZoneObservation(int id, String pathToImg, double longitude,
							double latitude) 
	{
		this.zoneId = id;
		this.pathToImg = pathToImg;
		this.longitude = longitude;
		this.latitude = latitude;
		String[] path = pathToImg.split("/");
		String[] nameImg = path[path.length-1].split("\\.");
		this.name = nameImg[0];
	}
	
	/******************** SETTERS ***********************/

	public void setZoneId(int zoneId) 
	{
		this.zoneId = zoneId;
	}

	public void setPathToImg(String pathToImg) 
	{
		this.pathToImg = pathToImg;
	}

	public void setLongitude(double longitude) 
	{
		this.longitude = longitude;
	}

	public void setLatitude(double latitude) 
	{
		this.latitude = latitude;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	/******************** GETTERS ***********************/
	
	public int getZoneId() 
	{
		return zoneId;
	}

	public String getPathToImg() 
	{
		return pathToImg;
	}

	public double getLongitude() 
	{
		return longitude;
	}

	public double getLatitude() 
	{
		return latitude;
	}
	
	public String getName()
	{
		return name;
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
		ZoneObservation other = (ZoneObservation) obj;
		if (Double.doubleToLongBits(latitude) != Double
				.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double
				.doubleToLongBits(other.longitude))
			return false;
		if (name == null) 
		{
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pathToImg == null) 
		{
			if (other.pathToImg != null)
				return false;
		} else if (!pathToImg.equals(other.pathToImg))
			return false;
		if (zoneId != other.zoneId)
			return false;
		return true;
	}
}
