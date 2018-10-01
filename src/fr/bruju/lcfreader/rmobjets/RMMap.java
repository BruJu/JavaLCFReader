package fr.bruju.lcfreader.rmobjets;

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
	
	/**
	 * Donne une représentation en chaîne la carte
	 * @param carte La carte
	 * @return Une chaîne représentant les données contenues
	 */
	public static String toString(RMMap carte) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("• Carte " +carte.id() + " / " + carte.nom()).append("\n");
		
		carte.evenements().values().stream().forEach(evenement -> {
			sb.append("•• Evenement " + evenement.id() + " [" + evenement.x() + ", " + evenement.y() + "]").append("\n");
			evenement.pages().forEach(page -> {
				sb.append("••• Page " + page.id()).append("\n");
				page.instructions().forEach(instruction -> {
					sb.append("•••• Instruction " + instruction.code() + " '" + instruction.argument() + "' ");
					
					for (int p : instruction.parametres()) {
						sb.append(p + " ");
					}
					
					sb.append("\n");
				});
			});
		});
		
		return sb.toString();
	}
}
