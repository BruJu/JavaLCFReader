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
	
	
	/**
	 * Donne une représentation en chaîne de l'évènement commun
	 * @param evenementCommun L'évènement commun
	 * @return Une chaîne représentant les données contenues
	 */
	public static String toString(RMEvenementCommun evenementCommun) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("• EvenementCommun " + evenementCommun.id() + " / " + evenementCommun.nom()).append("\n");
		
		evenementCommun.instructions().stream().forEach(instruction -> {
			sb.append("•• Instruction " + instruction.code() + " '" + instruction.argument() + "' ");
			
			for (int p : instruction.parametres()) {
				sb.append(p + " ");
			}
			
			sb.append("\n");
		});
		
		return sb.toString();
	}
}
