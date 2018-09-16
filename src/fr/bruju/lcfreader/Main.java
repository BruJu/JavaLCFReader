package fr.bruju.lcfreader;

import fr.bruju.lcfreader.modele.DonneesLues;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;


public class Main {
	public static void main(String[] args) {
		BaseDeDonneesDesStructures codesConnus = new BaseDeDonneesDesStructures("ressources\\liblcf\\fields.csv");
		
		afficherMap("Map0452", codesConnus);
		//afficherMap("Map0001", codesConnus);
		//afficherMap("Map0003", codesConnus);
		
		
	}

	private static void afficherMap(String nom, BaseDeDonneesDesStructures codesConnus) {
		
		DonneesLues map = DonneesLues.lireFichier("A:\\Dev\\"+nom+".lmu", codesConnus);

		System.out.println();
		System.out.println();
		System.out.println("}}-- "+nom+" --{{");
		map.afficher();
	}	
}
