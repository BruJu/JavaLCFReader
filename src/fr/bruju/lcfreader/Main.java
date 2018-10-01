package fr.bruju.lcfreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import fr.bruju.lcfreader.rmobjets.RMEvenementCommun;
import fr.bruju.lcfreader.rmobjets.RMInstruction;
import fr.bruju.lcfreader.rmobjets.RMMap;
import fr.bruju.lcfreader.services.LecteurDeLCF;
import fr.bruju.lcfreader.structure.modele.Desequenceur;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.structure.structure.Structures;

@SuppressWarnings("unused")
public class Main {
	public static void main(String[] args) throws InterruptedException {

		String chemin = "..\\RMEventReader\\ressources\\FichiersBruts\\";
		//String chemin = "A:\\Dev\\Projet\\";
		
		LecteurDeLCF arborescence = new LecteurDeLCF(chemin);
		
		afficherRMMap(arborescence.map(1));
		
		
		/*
		int idEv = 610;
		
		System.out.println(RMEvenementCommun.toString(arborescence.evenementCommun(idEv)));
		
		*/
		
		
		
		//int numeroDeMap = 2;
		
	
		//testerLecture(chemin, "RPG_RT.lmt");

		
		//testerLecture(chemin, construireNomDeMap(numeroDeMap));

		//testMap(chemin, numeroDeMap);
	}
	
	private static void testMap(String chemin, int numeroDeMap) {
		RMMap carte = new LecteurDeLCF(chemin).map(numeroDeMap);
		afficherRMMap(carte);
	}

	public static void afficherRMMap(RMMap carte) {
		System.out.println(RMMap.toString(carte));
	}

	private static void testerLecture(String chemin, String nomDeFichier) {
		String vraiChemin = chemin + nomDeFichier;
		doubleAffichage(vraiChemin);
	}
	
	
	private static String construireNomDeMap(int numeroDeMap) {
		return "Map" + String.format("%04d", numeroDeMap) + ".lmu";
	}

	private static void doubleAffichage(String string) {
		afficherMap(string, EnsembleDeDonnees::afficherDonnees);
		afficherMap(string, EnsembleDeDonnees::afficherArchitecture);
	}

	private static void afficherMap(String nom, Consumer<EnsembleDeDonnees> consumer) {
		EnsembleDeDonnees map = EnsembleDeDonnees.lireFichier(nom);
		
		System.out.println();
		System.out.println();
		System.out.println("}}-- " + nom + " --{{");
		consumer.accept(map);
	}
}
