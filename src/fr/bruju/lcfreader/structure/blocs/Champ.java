package fr.bruju.lcfreader.structure.blocs;

/**
 * Une classe temporaire qui compacte index, nom et si le bloc est un champ de taille pour le passage entre fonctions
 * avant la création du bloc.
 * 
 * @author Bruju
 *
 */
class Champ {
	/** Index */
	final int index;
	/** Nom */
	final String nom;
	/** Vrai si le champ est un champ indiquant la taille d'un autre */
	final boolean sized;

	/**
	 * Construit le champ
	 */
	Champ(int index, String nom, boolean sized) {
		this.index = index;
		this.nom = nom;
		this.sized = sized;
	}
}