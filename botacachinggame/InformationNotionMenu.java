package com.botacachinggame;

import java.util.ArrayList;

import com.db.bdd.BddNiveau;
import com.db.bdd.BddQube;
import com.db.object.Niveau;
import com.db.object.Notion;
import com.db.object.Qube;

public class InformationNotionMenu extends AbstractNotionMenu{

	@Override
	protected int checkLevelsInNotion(Notion current_notion) {

		BddNiveau bddLevel = new BddNiveau(this);
		bddLevel.open();
		
		ArrayList<Niveau> niveaux = bddLevel.getNiveauxWithNotionId(current_notion.getId());
		
		bddLevel.close();
		
		BddQube bddQube = new BddQube(this);
		bddQube.open();
		
		ArrayList<Qube> fichesInfo;
		
		int res = -1;
		
		for(int i = 0; i < niveaux.size(); ++i){
			
			fichesInfo = bddQube.getFicheInfoWithLevelID(niveaux.get(i).getIdNiveau());
			
			if(fichesInfo == null)
				continue;
			
			for(int j = 0; j < fichesInfo.size(); ++j)
				if(fichesInfo.get(j).getEtat() == Qube.QUESTION_REUSSI)
					return 1;
				else if(fichesInfo.get(j).getEtat() == Qube.QUESTION_NON_TRAITE)
					res = 0;
		}
		
		bddQube.close();
		
		return res;
		
	}

	@Override
	protected Class<?> getNextActivity() {
		return InformationSheetMenu.class;
	}

	@Override
	protected Class<?> getPreviousActivity() {
		return MainMenu.class;
	}
	
}
