package fr.bruju.lcfreader.structure.redefinitions;

import fr.bruju.lcfreader.structure.StructureSerie;
import fr.bruju.lcfreader.structure.blocs.BlocSuiteValeurs;
import fr.bruju.lcfreader.structure.blocs.Champ;

public class StructureParameters extends StructureSerie {
	
	// Lit 99 x 6 short
	
	public StructureParameters() {
		super("Parameters");
		
		String[] champs = {"maxhp", "maxsp", "attack", "defense", "spirit", "agility"};
		
		for (String nomChamp : champs) {
			serie.add(new BlocSuiteValeurs(new Champ(0, nomChamp, false, "Int16"), "Int16", 99));
		}
	}

	@Override
	public void ajouterChamp(String[] donnees) {
		// Ne pas accepter les champs du fichier fields
	}
}
