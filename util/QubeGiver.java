package com.util;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;

import com.db.bdd.BddLiaison;
import com.db.bdd.BddNiveau;
import com.db.bdd.BddQube;
import com.db.object.Liaison;
import com.db.object.Niveau;
import com.db.object.Qube;

public class QubeGiver {

	private static ArrayList<Qube> qubeList;
	
	public static Qube getNextQube(){
		
		if(qubeList.size() == 0)
			return null;
		
		return qubeList.remove(0);
	}
	
	public static void setQubeList(Context context, int levelID){
		
		QubeGiver.qubeList = new ArrayList<Qube>();
		
		BddNiveau bddNiveau = new BddNiveau(context);
		bddNiveau.open();
		
		Niveau level = bddNiveau.getNiveauWithId(levelID);		
	
		bddNiveau.close();
		
		BddQube bddQube = new BddQube(context);
		bddQube.open();
		
		//TODO
		/*** Echanger les deux lignes pour prendre en compte les ZO ***/
		//les qubes sans zones d'observation ne seront pas pris en compte
		//sauf si ce sont des Fiches Informatives
		
		ArrayList<Qube> qubes = bddQube.getQubesWithNiveauId(levelID);
		//ArrayList<Qube> qubes = getQubesWithZO(context, level);
		
		/**************************************************************/
		
		bddQube.close();
		
		ArrayList<Qube> qubesNonTraitee = new ArrayList<Qube>();
		ArrayList<Qube> qubesRatee = new ArrayList<Qube>();
		ArrayList<Qube> qubesJuste = new ArrayList<Qube>();
		ArrayList<Qube> ficheInfoNonTraitee = new ArrayList<Qube>();
		ArrayList<Qube> ficheInfoTraitee = new ArrayList<Qube>();
		
		Qube qube;
		
		for(int i = 0; i < qubes.size(); ++i){
			
			qube = qubes.get(i);
			
			if(qube.isFicheInfo() && qube.getEtat() == Qube.QUESTION_REUSSI)
				ficheInfoTraitee.add(qube);
			else if(qube.isFicheInfo()){
				ficheInfoNonTraitee.add(qube);
			}
			else{
				if(qube.getEtat() == Qube.QUESTION_NON_TRAITE)
					qubesNonTraitee.add(qube);
				else if(qube.getEtat() == Qube.QUESTION_RATE)
					qubesRatee.add(qube);
				else
					qubesJuste.add(qube);
			}
		}
		
		if(qubesNonTraitee.size() >= level.getScoreToUnlock()){
			
			QubeGiver.addToTheListWithRandom(qubesNonTraitee, level.getScoreToUnlock());
		}
		else if(qubesNonTraitee.size() > 0){

			QubeGiver.addToTheListWithRandom(qubesNonTraitee, qubesNonTraitee.size());
			
			int leftToFill = level.getScoreToUnlock() - qubesNonTraitee.size();
			
			if(qubesRatee.size() >= leftToFill){
				
				QubeGiver.addToTheListWithRandom(qubesRatee, leftToFill);
			}
			else if(qubesRatee.size() > 0){
				
				QubeGiver.addToTheListWithRandom(qubesRatee, qubesRatee.size());
				
				leftToFill = leftToFill - qubesRatee.size();
				QubeGiver.addToTheListWithRandom(qubesJuste, leftToFill);
			}
			else{
				QubeGiver.addToTheListWithRandom(qubesJuste, leftToFill);
			}
		}
		else{
			
			int leftToFill = level.getScoreToUnlock();
			
			if(qubesRatee.size() >= leftToFill){
				
				QubeGiver.addToTheListWithRandom(qubesRatee, leftToFill);
			}
			else if(qubesRatee.size() > 0){
				
				QubeGiver.addToTheListWithRandom(qubesRatee, qubesRatee.size());
				
				leftToFill = leftToFill - qubesRatee.size();
				
				QubeGiver.addToTheListWithRandom(qubesJuste, leftToFill);
			}
			else{
				QubeGiver.addToTheListWithRandom(qubesJuste, leftToFill);
			}
		}
		
		if(ficheInfoNonTraitee.size() > 0)
			QubeGiver.addToTheListWithRandom(ficheInfoNonTraitee, 1);
//		else if(ficheInfoTraitee.size() > 0)
//			QubeGiver.addToTheListWithRandom(ficheInfoTraitee, 1);
	}
	
	public static ArrayList<Niveau> getLevelsWithZO(Context context, ArrayList<Niveau> currentLevels) {
		
		ArrayList<Niveau> res = new ArrayList<Niveau>();
		
		BddQube bddQube = new BddQube(context);
		bddQube.open();
		
		BddLiaison bddLiaison = new BddLiaison(context);
		bddLiaison.open();
		
		Niveau level;
		ArrayList<Qube> qubes;
		Liaison liaisons;
		
		int cpt;
		
		for(int i = 0; i < currentLevels.size(); ++i){
			
			level = currentLevels.get(i);
			qubes = bddQube.getQubesWithNiveauId(level.getIdNiveau());
			
			cpt = 0;
			
			for(int j = 0; j < qubes.size(); ++j){
				
				liaisons = bddLiaison.getZoneObsWithQubeId(qubes.get(j).getIdQube());
				
				if(liaisons != null)
					++cpt;
			}
			
			if(cpt >= currentLevels.get(i).getScoreToUnlock())
				res.add(currentLevels.get(i));
			
		}
		
		bddLiaison.close();
		bddQube.close();
		
		return res;
		
	}

	public static ArrayList<Qube> getQubesWithZO(Context context, Niveau level) {
		
		ArrayList<Qube> res = new ArrayList<Qube>();
		
		BddQube bddQube = new BddQube(context);
		bddQube.open();
		
		BddLiaison bddLiaison = new BddLiaison(context);
		bddLiaison.open();
		
		ArrayList<Qube> qubes;
		Liaison liaisons;
		
		qubes = bddQube.getQubesWithNiveauId(level.getIdNiveau());
		
		for(int j = 0; j < qubes.size(); ++j){
			
			liaisons = bddLiaison.getZoneObsWithQubeId(qubes.get(j).getIdQube());
			
			//ajout du qube si il a une ZO ou si c'est une FI
			if(liaisons != null || qubes.get(j).isFicheInfo())
				res.add(qubes.get(j));
			
		}
			
		bddLiaison.close();
		bddQube.close();
		
		return res;
		
	}
	
	private static void addToTheListWithRandom(ArrayList<Qube> qubes, int nbToAdd) {
		
		int alreadyAdded = 0;
		Random rand = new Random();
		int num;
		
		while(alreadyAdded < nbToAdd){
			
			num = Math.abs(rand.nextInt()) % qubes.size();
			
			if(qubes.get(num) == null)
				continue;
			
			++alreadyAdded;
			
			QubeGiver.qubeList.add(qubes.get(num));
			qubes.set(num, null);
		}
	}
}
