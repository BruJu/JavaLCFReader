package fr.bruju.lcfreader;

import java.util.function.Consumer;

import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;


public class Main {
	public static void main(String[] args) throws InterruptedException {
		BaseDeDonneesDesStructures.initialiser("ressources\\liblcf\\fields.csv");
		
		afficherMap("Map0452", EnsembleDeDonnees::afficher);
		//afficherMap("Map0001", EnsembleDeDonnees::afficher);
		//afficherMap("Map0003", EnsembleDeDonnees::afficher);

		
		//afficherMap("Map0452", EnsembleDeDonnees::afficherArchi);
	}

	private static void afficherMap(String nom, Consumer<EnsembleDeDonnees> consumer) {
		EnsembleDeDonnees map = EnsembleDeDonnees.lireFichier("A:\\Dev\\"+nom+".lmu");

		System.out.println();
		System.out.println();
		System.out.println("}}-- "+nom+" --{{");
		consumer.accept(map);
	}	
}
