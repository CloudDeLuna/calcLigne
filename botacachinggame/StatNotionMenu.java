package com.botacachinggame;

import java.util.ArrayList;

import com.db.bdd.BddNiveau;
import com.db.object.Niveau;
import com.db.object.Notion;

public class StatNotionMenu extends AbstractNotionMenu{

	@Override
	protected int checkLevelsInNotion(Notion current_notion) {

		BddNiveau bddLevel = new BddNiveau(this);
		bddLevel.open();
		
		ArrayList<Niveau> niveaux = bddLevel.getNiveauxWithNotionId(current_notion.getId());
		
		bddLevel.close();

		int res = -1;
		
		for(int i = 0; i < niveaux.size(); ++i){
			
			if(niveaux.get(i).isPlayable())
				return 1;
			else
				res = 0;
		}
		
		return res;
	}

	@Override
	protected Class<?> getNextActivity() {
		return StatActivity.class;
	}

	@Override
	protected Class<?> getPreviousActivity() {
		return Profil.class;
	}

}
