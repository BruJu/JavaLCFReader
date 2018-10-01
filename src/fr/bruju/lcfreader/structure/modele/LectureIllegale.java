package fr.bruju.lcfreader.structure.modele;

import fr.bruju.lcfreader.Utilitaire;

/**
 * Exception jetée lorsqu'une lecture illégale est faite
 * 
 * @author Bruju
 *
 */
public class LectureIllegale extends RuntimeException {
	/** Serial id */
	private static final long serialVersionUID = 760742337368045553L;

	/**
	 * Construit une exception de lecture illégale
	 * @param fichier Nom du fichier
	 * @param debut Position du premier octet
	 * @param position Position du curseur
	 */
	public LectureIllegale(String fichier, int debut, int position) {
		super("Lecture illégale dans "
				+ fichier
				+ ", segment commençant à "
				+ Utilitaire.toHex(debut)
				+ " avec un curseur à "
				+ Utilitaire.toHex(position));
	}
}