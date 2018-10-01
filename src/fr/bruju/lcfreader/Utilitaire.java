package fr.bruju.lcfreader;

import java.util.List;

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
	 * Affiche deux espaces par niveau
	 * 
	 * @param niveau Le nombre d'espaces à afficher divisé par deux
	 * @return Des espaces 
	 */
	public static String tab(int niveau) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i != niveau; i++) {
			sb.append("  ");
		}
		return sb.toString();
	}

	/**
	 * Transforme la liste d'integer donnée en un tableau d'int
	 * @param liste La liste à transformer
	 * @return Un tableau d'int avec les mêmes nombres
	 */
	public static int[] transformerTableau(List<Integer> liste) {
		int[] tableau = new int[liste.size()];
		
		for (int i = 0 ; i != tableau.length ; i++) {
			tableau[i] = liste.get(i);
		}
		
		return tableau;
	}

	/**
	 * Transforme le tableau de byte donné en une chaîne en utilisant une écriture hexadécimale pour représenter les
	 * octets.
	 * @param valeurs Le tableau de valeurs
	 * @return Une chaîne représentant les octets sous forme hexadécimale
	 */
	public static String bytesToString(byte[] valeurs) {
		StringBuilder sb = new StringBuilder().append("[");
		
		if (valeurs.length != 0) {
			sb.append(Utilitaire.toHex(valeurs[0]));
			
			for (int i = 1 ; i != valeurs.length ; i++) {
				sb.append(" ").append(Utilitaire.toHex(valeurs[i]));
			}
		}
		
		return sb.append("]").toString();
	}
}
