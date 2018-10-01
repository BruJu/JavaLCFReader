package fr.bruju.lcfreader.services;

import java.util.List;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.rmobjets.RMInstruction;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;

/**
 * Classe extrayant une instruction depuis un ensemble de données
 * 
 * @author Bruju
 *
 */
public class LCFInstruction implements RMInstruction {
	/* =========
	 * ATTRIBUTS
	 * ========= */
	
	/** Code de l'instruction */
	private final int code;
	/** Argument de l'instruction en format texte */
	private final String argument;
	/** Liste des paramètres */
	private final int[] parametres;
	
	/**
	 * Instancie l'instruction depuis l'ensemble de données donné
	 * @param ensemble L'ensemble de données
	 */
	@SuppressWarnings("unchecked")
	public LCFInstruction(EnsembleDeDonnees ensemble) {
		code = ensemble.getDonnee("code", Integer.class);
		argument = ensemble.getDonnee("string", String.class);
		parametres = Utilitaire.transformerTableau(ensemble.getDonnee("parameters", List.class));
	}

	
	/* ===============================
	 * IMPLEMENTATION DE RMINSTRUCTION
	 * =============================== */

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