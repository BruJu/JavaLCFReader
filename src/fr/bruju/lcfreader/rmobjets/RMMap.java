package fr.bruju.lcfreader.rmobjets;

import java.util.List;
import java.util.Map;

/**
 * Représente une carte RPG Maker
 * 
 * @author Bruju
 *
 */
public interface RMMap {
	/**
	 * Donne le numéro de la carte
	 * @return Le numéro de la carte
	 */
	public int id();
	
	/**
	 * Donne le nom de la carte. L'interprétation de ce terme est laissé à l'implémentation.
	 * @return Un nom pourla carte
	 */
	public String nom();
	
	/**
	 * Donne la liste des évènements composant la carte
	 * @return La liste des évènements
	 */
	public Map<Integer, RMEvenement> evenements();
}
