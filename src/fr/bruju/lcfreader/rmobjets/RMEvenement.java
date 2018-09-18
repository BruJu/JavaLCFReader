package fr.bruju.lcfreader.rmobjets;

import java.util.List;

/**
 * Représente un évènement dans RPG Maker
 * 
 * @author Bruju
 *
 */
public interface RMEvenement {
	/**
	 * Donne l'identifiant de l'évènemen 
	 * @return L'identifiant de l'évènement
	 */
	public int id();
	
	/**
	 * Donne le nom de l'évènement
	 * @return Le nom de l'évènement
	 */
	public String nom();
	
	/**
	 * Donne la position en X de l'évènement
	 * @return La position en X de l'évènement
	 */
	public int x();
	
	/**
	 * Donne la position en Y de l'évènement
	 * @return La position en Y de l'évènement
	 */
	public int y();
	
	/**
	 * Donne la liste des pages de l'évènement
	 * @return La liste des pages
	 */
	public List<RMPage> pages();
}
