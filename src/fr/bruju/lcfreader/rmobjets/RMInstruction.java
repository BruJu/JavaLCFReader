package fr.bruju.lcfreader.rmobjets;

/**
 * Représente une instruction RPG Maker 2003
 */
public interface RMInstruction {
	/**
	 * Renvoie le code de l'instruction
	 * @return Le code de l'instruction
	 */
	public int code();

	/**
	 * Renvoie l'argument de type chaîne de l'instruction
	 * @return L'argument de type chaîne
	 */
	public String argument();

	/**
	 * Renvoie les paramètres numériques de l'instruction
	 * @return Un tableau contenant les paramètres numériques
	 */
	public int[] parametres();
}
