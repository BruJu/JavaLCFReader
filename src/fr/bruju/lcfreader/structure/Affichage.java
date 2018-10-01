package fr.bruju.lcfreader.structure;

import fr.bruju.lcfreader.rmobjets.RMMap;

public class Affichage {
	/**
	 * Transforme en chaîne une carte
	 * @param carte La carte
	 * @return Un affichage de la carte
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
