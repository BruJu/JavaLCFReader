package fr.bruju.lcfreader;

import fr.bruju.lcfreader.modele.DonneesLues;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;


public class Main {
	public static void main(String[] args) {
		BaseDeDonneesDesStructures codesConnus = new BaseDeDonneesDesStructures("ressources\\liblcf\\fields.csv");
		
		DonneesLues map = DonneesLues.lireFichier("A:\\Dev\\Map0452.lmu", codesConnus);

		System.out.println();
		System.out.println();
		System.out.println("}}-- Map0452.lmu --{{");
		map.afficher();
		
	}	
}
