package com.example.db.object;

public class Joueur 
{
	private int id;
	private String pseudo;
	private int niveau;
	private int experience;
	private int modeDeJeu;
	private boolean isSonEnable = false;
	private boolean isPlaying = false;
	private boolean isWarningVisible = true;
	
	public Joueur()
	{
		this.pseudo = "";
	}
	
	public Joueur(int id, String pseudo, int niveau, int experience, int modeDeJeu,
			boolean isSonEnable, boolean isPlaying, boolean isWarningVisible) 
	{
		this.id = id;
		this.pseudo = pseudo;
		this.niveau = niveau;
		this.experience = experience;
		this.modeDeJeu = modeDeJeu;
		this.isSonEnable = isSonEnable;
		this.isPlaying = isPlaying;
		this.isWarningVisible = isWarningVisible; 
	}

	/******************** SETTERS ***********************/
	
	public void setId(int id) 
	{
		this.id = id;
	}

	public void setPseudo(String pseudo) 
	{
		this.pseudo = pseudo;
	}

	public void setNiveau(int niveau) 
	{
		this.niveau = niveau;
	}

	public void setExperience(int experience) 
	{
		this.experience = experience;
	}

	public void setModeDeJeu(int modeDeJeu) 
	{
		this.modeDeJeu = modeDeJeu;
	}

	public void setSonEnable(boolean isSonEnable) 
	{
		this.isSonEnable = isSonEnable;
	}
	
	public void setPlaying(boolean isPlaying) 
	{
		this.isPlaying = isPlaying;
	}
	
	public void setWarningVisible(boolean isWarningVisible) 
	{
		this.isWarningVisible = isWarningVisible;
	}

	/******************** GETTERS ***********************/

	public int getId() 
	{
		return id;
	}

	public String getPseudo() 
	{
		return pseudo;
	}

	public int getNiveau() 
	{
		return niveau;
	}

	public int getExperience() 
	{
		return experience;
	}

	public int getModeDeJeu() 
	{
		return modeDeJeu;
	}

	public boolean isSonEnable() 
	{
		return isSonEnable;
	}

	public boolean isPlaying() 
	{
		return isPlaying;
	}
	
	public boolean isWarningVisible()
	{
		return isWarningVisible;
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
		Joueur other = (Joueur) obj;
		if (experience != other.experience)
			return false;
		if (id != other.id)
			return false;
		if (isPlaying != other.isPlaying)
			return false;
		if (isSonEnable != other.isSonEnable)
			return false;
		if (isWarningVisible != other.isWarningVisible)
			return false;
		if (modeDeJeu != other.modeDeJeu)
			return false;
		if (niveau != other.niveau)
			return false;
		if (pseudo == null) {
			if (other.pseudo != null)
				return false;
		} else if (!pseudo.equals(other.pseudo))
			return false;
		return true;
	}
}
