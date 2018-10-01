package fr.bruju.lcfreader.services;

import java.util.List;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.rmobjets.RMInstruction;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;

public class LCFInstruction implements RMInstruction {
	public static LCFInstruction instancier(EnsembleDeDonnees ensemble) {
		LCFInstruction instruction = new LCFInstruction(ensemble);
		
		if (instruction.code() == 0) {
			instruction = null;
		}
		
		return instruction;
	}
	
	
	
	
	private final int code;
	private final String argument;
	private final int[] parametres;
	

	@SuppressWarnings("unchecked")
	private LCFInstruction(EnsembleDeDonnees ensemble) {
		code = ensemble.getDonnee("code", Integer.class);
		argument = ensemble.getDonnee("string", String.class);
		parametres = Utilitaire.transformerTableau(ensemble.getDonnee("parameters", List.class));
	}
	

	@Override
	public int code() {
		return code;
	}

	@Override
	public String argument() {
		return argument;
	}
	
	@Override
	public int[] parametres() {
		return parametres;
	}
}