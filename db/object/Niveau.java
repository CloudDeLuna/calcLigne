package com.example.db.object;

public class Niveau 
{
	private int idNiveau;
	private int numNiveau;
	private boolean isBlocked = false;
	private boolean isPlayable = false;
	private int scoreToUnlock;
	private int scoreActuel;
	private boolean isRandom;
	private int idNotion;

	public Niveau() 
	{
	}
	
	public Niveau(int id, int numNiveau, boolean isBlocked, boolean isPlayable, int scoreToUnlock, int scoreActuel,
			boolean isRandom, int idNotion) 
	{
		this.idNiveau = id;
		this.numNiveau = numNiveau;
		this.isBlocked = isBlocked;
		this.isPlayable = isPlayable;
		this.scoreToUnlock = scoreToUnlock;
		this.scoreActuel = scoreActuel;
		this.setRandom(isRandom);
		this.idNotion = idNotion;
	}
	
	/******************** SETTERS ***********************/

	public void setIdNiveau(int idNiveau) 
	{
		this.idNiveau = idNiveau;
	}

	public void setNumNiveau(int numNiveau) 
	{
		this.numNiveau = numNiveau;
	}

	public void setBlocked(boolean isBlocked) 
	{
		this.isBlocked = isBlocked;
	}

	public void setPlayable(boolean isPlayable) 
	{
		this.isPlayable = isPlayable;
	}
	
	public void setScoreToUnlock(int scoreToUnlock) 
	{
		this.scoreToUnlock = scoreToUnlock;
	}

	public void setScoreActuel(int scoreActuel) 
	{
		this.scoreActuel = scoreActuel;
	}

	public void setRandom(boolean isRandom) 
	{
		this.isRandom = isRandom;
	}
	
	public void setIdNotion(int idNotion) 
	{
		this.idNotion = idNotion;
	}

	/******************** GETTERS ***********************/
	
	public int getIdNiveau() 
	{
		return idNiveau;
	}

	public int getNumNiveau() 
	{
		return numNiveau;
	}

	public boolean isBlocked() 
	{
		return isBlocked;
	}

	public boolean isPlayable() 
	{
		return isPlayable;
	}
	
	public int getScoreToUnlock() 
	{
		return scoreToUnlock;
	}
	
	public int getScoreActuel() 
	{
		return scoreActuel;
	}

	public boolean isRandom() 
	{
		return isRandom;
	}
	
	public int getIdNotion() 
	{
		return idNotion;
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
		Niveau other = (Niveau) obj;
		if (idNiveau != other.idNiveau)
			return false;
		if (idNotion != other.idNotion)
			return false;
		if (isBlocked != other.isBlocked)
			return false;
		if (isPlayable != other.isPlayable)
			return false;
		if (isRandom != other.isRandom)
			return false;
		if (numNiveau != other.numNiveau)
			return false;
		if (scoreActuel != other.scoreActuel)
			return false;
		if (scoreToUnlock != other.scoreToUnlock)
			return false;
		return true;
	}
}
