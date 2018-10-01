package fr.bruju.lcfreader.rmobjets;

import java.util.List;
import java.util.Map;

/**
 * Une RM Fabrique est un objet pouvant générer des objets standards RM à partir des numéros d'identifiants donnés
 * 
 * @author Bruju
 *
 */
public interface RMFabrique {
	
	public RMMap map(int idCarte);
	
	public default RMEvenement evenement(int idCarte, int idEvenement) {
		return map(idCarte).evenements().get(idEvenement);
	}
	
	public default RMPage page(int idCarte, int idEvenement, int idPage) {
		return evenement(idCarte, idEvenement).pages().get(idPage - 1);
	}
	
	public RMEvenementCommun evenementCommun(int idEvenementCommun);
	
	public List<RMMap> maps();
	
	public Map<Integer, RMEvenementCommun> evenementsCommuns();
}
