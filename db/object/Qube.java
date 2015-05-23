package com.example.db.object;

import java.util.ArrayList;


public class Qube
{
	public static int			QUESTION_REUSSI 	= 1;
	public static int			QUESTION_RATE 		= -1;
	public static int			QUESTION_NON_TRAITE = 0;
	
	private int 				idQube;
	private int 				numQube;
	private String 				question;
	private String 				proposition1;
	private String 				proposition2;
	private String 				proposition3;
	private String 				proposition4;
	private int 				numReponse;
	private boolean 			isQcmBasic = true;
	private boolean 			isBlocked = true;
	private boolean 			isPlayable = false;
	private int 				idNiveau;
	private int 				score;
	private int 				etat;
	private ArrayList<String> 	motsClefs;
	
	public Qube ()
	{
		this.question = "";
		this.proposition1 = "";
		this.proposition2 = "";
		this.proposition3 = "";
		this.proposition4 = "";
		
		this.etat = QUESTION_NON_TRAITE;
		this.setMotsClefs(new ArrayList<String>());
	}
	
	public Qube(int idQube, int numQube, String question, String prop1, 
			String prop2, String prop3, String prop4, int numReponse,
			boolean isQcmBasic, boolean isBlocked, boolean isPlayable, 
			int idNiveau, int score, int etat, ArrayList<String> motsClefs) 
	{
		this.idQube = idQube;
		this.numQube = numQube;
		this.question = question;
		this.proposition1 = prop1;
		this.proposition2 = prop2;
		this.proposition3 = prop3;
		this.proposition4 = prop4;
		this.numReponse = numReponse;
		this.isQcmBasic = isQcmBasic;
		this.isBlocked = isBlocked;
		this.isPlayable = isPlayable;
		this.idNiveau = idNiveau;
		this.score	= score;
		this.etat = etat;
		this.setMotsClefs(motsClefs);
	}

	/******************** SETTERS ***********************/
	
	public void setIdQube(int idQube) 
	{
		this.idQube = idQube;
	}

	public void setNumQube(int numQube) 
	{
		this.numQube = numQube;
	}

	public void setQuestion(String question) 
	{
		this.question = question;
	}
	
	public void setProposition1(String proposition1) 
	{
		this.proposition1 = proposition1;
	}

	public void setProposition2(String proposition2) 
	{
		this.proposition2 = proposition2;
	}

	public void setProposition3(String proposition3) 
	{
		this.proposition3 = proposition3;
	}
	
	public void setProposition4(String proposition4) 
	{
		this.proposition4 = proposition4;
	}
	
	public void setNumReponse(int numReponse)
	{
		this.numReponse = numReponse;
	}

	public void setQcmBasic(boolean isQcmBasic) 
	{
		this.isQcmBasic = isQcmBasic;
	}

	public void setBlocked(boolean isBlocked) 
	{
		this.isBlocked = isBlocked;
	}

	public void setPlayable(boolean isPlayable) 
	{
		this.isPlayable = isPlayable;
	}

	public void setIdNiveau(int idNiveau) 
	{
		this.idNiveau = idNiveau;
	}

	public void setScore(int score) 
	{
		this.score = score;
	}
	
	public void setEtat(int etat) 
	{
		this.etat = etat;
	}
	
	public void setMotsClefs(ArrayList<String> motsClefs) 
	{
		this.motsClefs = motsClefs;
	}
	
	public void addMotClef(String motClef)
	{
		this.motsClefs.add(motClef);
	}
	
	public void removeMotClef(String motClef)
	{
		this.motsClefs.remove(motClef);
	}

	/******************** GETTERS ***********************/
	
	public int getIdQube() 
	{
		return idQube;
	}

	public int getNumQube() 
	{
		return numQube;
	}

	public String getQuestion() 
	{
		return question;
	}

	public String getProposition1() 
	{
		return proposition1;
	}

	public String getProposition2() 
	{
		return proposition2;
	}

	public String getProposition3() 
	{
		return proposition3;
	}

	public String getProposition4() 
	{
		return proposition4;
	}
	
	public int getNumReponse() 
	{
		return numReponse;
	}

	public String getReponse() 
	{
		if(numReponse == 1)
			return proposition1;
		else if(numReponse == 2)
			return proposition2;
		else if(numReponse == 3)
			return proposition3;
		else
			return proposition4;
	}
	
	public boolean isQcmBasic() 
	{
		return isQcmBasic;
	}

	public boolean isBlocked() 
	{
		return isBlocked;
	}

	public boolean isPlayable()
	{
		return isPlayable;
	}

	public int getIdNiveau() 
	{
		return idNiveau;
	}

	public int getScore() 
	{
		return this.score;
	}
	
	public int getEtat() 
	{
		return etat;
	}
	
	public ArrayList<String> getMotsClefs() 
	{
		return motsClefs;
	}
	
	public String getMotClefsForInsert()
	{
		if(motsClefs.isEmpty())
			return "";
		
		String res = "";
		
		for(int i = 0; i < motsClefs.size() - 1; ++i )
			res += motsClefs.get(i) + ";";
	
		res += motsClefs.get(motsClefs.size()-1);
		
		return res;
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
		Qube other = (Qube) obj;
		if (etat != other.etat)
			return false;
		if (idNiveau != other.idNiveau)
			return false;
		if (idQube != other.idQube)
			return false;
		if (isBlocked != other.isBlocked)
			return false;
		if (isPlayable != other.isPlayable)
			return false;
		if (isQcmBasic != other.isQcmBasic)
			return false;
		if (motsClefs == null) {
			if (other.motsClefs != null)
				return false;
		} else if (!motsClefs.equals(other.motsClefs))
			return false;
		if (numQube != other.numQube)
			return false;
		if (numReponse != other.numReponse)
			return false;
		if (proposition1 == null) {
			if (other.proposition1 != null)
				return false;
		} else if (!proposition1.equals(other.proposition1))
			return false;
		if (proposition2 == null) {
			if (other.proposition2 != null)
				return false;
		} else if (!proposition2.equals(other.proposition2))
			return false;
		if (proposition3 == null) {
			if (other.proposition3 != null)
				return false;
		} else if (!proposition3.equals(other.proposition3))
			return false;
		if (proposition4 == null) {
			if (other.proposition4 != null)
				return false;
		} else if (!proposition4.equals(other.proposition4))
			return false;
		if (question == null) {
			if (other.question != null)
				return false;
		} else if (!question.equals(other.question))
			return false;
		if (score != other.score)
			return false;
		return true;
	}

}
