package fr.bruju.lcfreader.rmobjets;

import java.util.List;

/**
 * Représente une page d'un évènement dans RPG Maker
 * 
 * @author Bruju
 *
 */
public interface RMPage {
	/**
	 * Donne l'identifiant de la page
	 * @return L'identifiant de la page
	 */
	public int id();
	
	/**
	 * Donne la liste des instructions de la page
	 * @return La liste des instructions de la page
	 */
	public List<RMInstruction> instructions();
}
