package fr.bruju.lcfreader;

public class Utilitaire {

	public static String toHex(int octet) {
		return String.format("%02X", octet);
	}
	
	public static String toHex(byte octet) {
		return String.format("%02X", octet);
	}
	
	/**
	 * Affiche deux espaces par niveau
	 * @param niveau Le nombre d'espaces à afficher divisé par deux
	 */
	public static void tab(int niveau) {
		for (int i = 0 ; i != niveau ; i++)
			System.out.print("  ");
	}
}
