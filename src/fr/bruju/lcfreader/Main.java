package fr.bruju.lcfreader;

import java.util.function.Consumer;

import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		BaseDeDonneesDesStructures.initialiser("ressources\\liblcf\\fields.csv");

		String[] fichiers = new String[] { "Projet\\Map0001", "Map0452", "Map0001", "Map0003" };

		doubleAffichage(fichiers[1]);
	}

	private static void doubleAffichage(String string) {
		afficherMap(string, EnsembleDeDonnees::afficherDonnees);
		afficherMap(string, EnsembleDeDonnees::afficherArchitecture);
	}

	private static void afficherMap(String nom, Consumer<EnsembleDeDonnees> consumer) {
		EnsembleDeDonnees map = EnsembleDeDonnees.lireFichier("A:\\Dev\\" + nom + ".lmu");

		System.out.println();
		System.out.println();
		System.out.println("}}-- " + nom + " --{{");
		consumer.accept(map);
	}
}
