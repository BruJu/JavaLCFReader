package fr.bruju.lcfreader.debug;

import java.util.List;

import fr.bruju.lcfreader.Utilitaire;

/**
 * Classe fournissant des méthodes static pour afficher des octets
 * 
 * @author Bruju
 *
 */
public class BytePrinter {
	/** printByte : Compteur du nombre d'octets affichés à la suite */
	private static int id = 0;
	
	/** Affiche l'octet avec le prefixe donné devant. Produit un retour à la ligne tous les 16 appels */
	public static void printByte(byte octet, char prefixe) {
		System.out.print(prefixe + String.format("%02X", octet) + " ");
		
		id++;
		
		if (id == 16) {
			id = 0;
			System.out.println();
		}
	}
	
	/** Affiche les nbDeLignes premières lignes de 16 octets extraits de la liste donnée */
	public static void afficherBytes(List<Byte> bytes, int nbDeLignes) {
		for (int i = 0 ; i != 16; i++) {
			System.out.print(Utilitaire.toHex(i));
		}
		System.out.println();
		
		for (int i = 0 ; i != 16 ; i++) {
			System.out.print("---");
		}
		System.out.println();
		
		for (int i = 0 ; i != bytes.size() ; i++) {
			System.out.print(Utilitaire.toHex(bytes.get(i)) + " ");
			
			if (i % 16 == 15) {
				System.out.println();
				
				nbDeLignes --;
				
				if (nbDeLignes == 0)
					return;
			}
			
		}
	}

	public static String getTable(byte[] value) {
		String s = "[";
		boolean a = false;
		for (byte v : value) {
			if (a) {
				s += " ";
			}
			
			a = true;
			
			s += Utilitaire.toHex(v);
			
		}
		s +="]";
		return s;
	}
}
