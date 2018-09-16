package fr.bruju.lcfreader;

import fr.bruju.lcfreader.modele.DonneesLues;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;


public class Main {
	public static void main(String[] args) {
		BaseDeDonneesDesStructures.initialiser("ressources\\liblcf\\fields.csv");
		
		afficherMap("Map0452");
		//afficherMap("Map0001");
		//afficherMap("Map0003");
		
		
	}

	private static void afficherMap(String nom) {
		DonneesLues map = DonneesLues.lireFichier("A:\\Dev\\"+nom+".lmu");

		System.out.println();
		System.out.println();
		System.out.println("}}-- "+nom+" --{{");
		map.afficher();
	}	
}
