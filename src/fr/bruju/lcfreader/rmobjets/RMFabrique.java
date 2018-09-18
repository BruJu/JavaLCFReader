package fr.bruju.lcfreader.rmobjets;

import java.util.List;

/**
 * Une RM Fabrique est un objet pouvant générer des objets standards RM à partir des numéros d'identifiants donnés
 * 
 * @author Bruju
 *
 */
public interface RMFabrique {
	
	public RMMap map(int idCarte);
	
	public RMEvenement evenement(int idCarte, int idEvenement);
	
	public default RMPage page(int idCarte, int idEvenement, int idPage) {
		return evenement(idCarte, idEvenement).pages().get(idPage - 1);
	}
	
	public RMEvenementCommun evenementCommun(int idEvenementCommun);
	
	public List<RMMap> maps();
	
	public List<RMEvenementCommun> evenementsCommuns();
}
