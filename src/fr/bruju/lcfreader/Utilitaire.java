package fr.bruju.lcfreader;

import java.util.List;
import java.util.function.Function;

public class Utilitaire {

	/**
	 * Donne une représentation hexadécimale sur deux caractère de l'octet donné
	 * 
	 * @param octet L'octet
	 * @return <code>String.format("%02X", octet)</code>
	 */
	public static String toHex(int octet) {
		return String.format("%02X", octet);
	}

	/**
	 * Donne une représentation hexadécimale sur deux caractère de l'octet donné
	 * 
	 * @param octet L'octet
	 * @return <code>String.format("%02X", octet)</code>
	 */
	public static String toHex(byte octet) {
		return String.format("%02X", octet);
	}

	/**
	 * Affiche deux espaces par niveau
	 * 
	 * @param niveau Le nombre d'espaces à afficher divisé par deux
	 */
	public static void tab(int niveau) {
		for (int i = 0; i != niveau; i++) {
			System.out.print("  ");
		}
	}

	public static <O, R> R appel(O objet, Function<O, R> fonction) {
		return objet == null ? null : fonction.apply(objet);
	}
	
	public static int[] transformerTableau(List<Integer> liste) {
		int[] tableau = new int[liste.size()];
		
		for (int i = 0 ; i != tableau.length ; i++) {
			tableau[i] = liste.get(i);
		}
		
		return tableau;
	}

	public static String bytesToString(byte[] valeurs) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		if (valeurs.length != 0) {
			sb.append(Utilitaire.toHex(valeurs[0]));
			
			for (int i = 1 ; i != valeurs.length ; i++) {
				sb.append(" ").append(Utilitaire.toHex(valeurs[i]));
			}
		}
		
		sb.append("]");
		
		return sb.toString();
	}
	
}
