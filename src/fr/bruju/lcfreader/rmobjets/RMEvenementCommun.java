package fr.bruju.lcfreader.rmobjets;

import java.util.List;

/**
 * Représente un évènement commun dans RPG Maker
 * 
 * @author Bruju
 *
 */
public interface RMEvenementCommun {

	/**
	 * Donne le numéro de l'évènement commun
	 * @return Le numéro de l'évènement commun
	 */
	public int id();
	
	/**
	 * Donne le nom de l'évènement commun
	 * @return Le nom de l'évènement commun
	 */
	public String nom();
	
	/**
	 * Donne la liste des instructions dans l'évènement commun
	 * @return La liste des instructions
	 */
	public List<RMInstruction> instructions();
}
