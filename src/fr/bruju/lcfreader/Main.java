package fr.bruju.lcfreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.function.Consumer;

import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.modele.FabriqueLCF;
import fr.bruju.lcfreader.rmobjets.RMMap;
import fr.bruju.lcfreader.sequenceur.lecteurs.Desequenceur;
import fr.bruju.lcfreader.sequenceur.sequences.LecteurDeSequence;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;

@SuppressWarnings("unused")
public class Main {
	public static void main(String[] args) throws InterruptedException {
		
		
		//String chemin = "..\\RMEventReader\\ressources\\FichiersBruts\\";
		String chemin = "A:\\Dev\\Projet\\";
		int numeroDeMap = 2;
		
	
		testerLecture(chemin, "RPG_RT.lmt");

		
		//testerLecture(chemin, construireNomDeMap(numeroDeMap));

		//testMap(chemin, numeroDeMap);
	}
	
	private static void testMap(String chemin, int numeroDeMap) {
		RMMap carte = new FabriqueLCF(chemin).map(numeroDeMap);
		afficherRMMap(carte);
	}

	private static void afficherRMMap(RMMap carte) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("• Carte " +carte.id() + " / " + carte.nom()).append("\n");
		
		carte.evenements().stream().forEach(evenement -> {
			sb.append("•• Evenement " + evenement.id() + " [" + evenement.x() + ", " + evenement.y() + "]").append("\n");
			evenement.pages().forEach(page -> {
				sb.append("••• Page " + page.id()).append("\n");
				page.instructions().forEach(instruction -> {
					sb.append("•••• Instruction " + instruction.code() + " '" + instruction.argument() + "' ");
					
					for (int p : instruction.parametres()) {
						sb.append(p + " ");
					}
					
					sb.append("\n");
				});
			});
		});
		
		System.out.println(sb.toString());
	}

	private static void testerLecture(String chemin, String nomDeFichier) {
		BaseDeDonneesDesStructures.initialiser("ressources\\liblcf\\fields.csv");
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
