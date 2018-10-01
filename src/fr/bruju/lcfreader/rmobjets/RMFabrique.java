package fr.bruju.lcfreader.rmobjets;

import java.util.Map;

/**
 * Une RM Fabrique est un objet pouvant générer des objets standards RM à partir des numéros d'identifiants donnés
 * 
 * @author Bruju
 *
 */
public interface RMFabrique {
	/**
	 * Permet d'accéder à la carte demandée
	 * @param idCarte Le numéro de la carte
	 * @return La carte
	 */
	public default RMMap map(int idCarte) {
		return maps().get(idCarte);
	}
	
	/**
	 * Permet d'accéder à l'évènement demandé
	 * @param idCarte Le numéro de la carte
	 * @param idEvenement Le numéro de l'évènement
	 * @return L'évènement
	 */
	public default RMEvenement evenement(int idCarte, int idEvenement) {
		return map(idCarte).evenements().get(idEvenement);
	}
	
	/**
	 * Permet d'accéder à la page demandée
	 * @param idCarte Le numéro de la carte
	 * @param idEvenement Le numéro de l'évènement
	 * @param idPage Le numéro de la page
	 * @return La page
	 */
	public default RMPage page(int idCarte, int idEvenement, int idPage) {
		return evenement(idCarte, idEvenement).pages().get(idPage - 1);
	}
	
	/**
	 * Permet d'accéder à un évènement commun
	 * @param idEvenementCommun Le numéro de l'évènement commun
	 * @return L'évènement commun
	 */
	public default RMEvenementCommun evenementCommun(int idEvenementCommun) {
		return evenementsCommuns().get(idEvenementCommun);
	}
	
	/**
	 * Permet d'accéder à toutes les cartes
	 * @return La table d'association id - cartes
	 */
	public Map<Integer, ? extends RMMap> maps();
	
	/**
	 * Permet d'acccéder à tous les évènements communs
	 * @return La table d'assocciation id - évènement communs
	 */
	public Map<Integer, ? extends RMEvenementCommun> evenementsCommuns();
}
