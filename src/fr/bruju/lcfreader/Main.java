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

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.modele.FabriqueLCF;
import fr.bruju.lcfreader.rmobjets.RMEvenementCommun;
import fr.bruju.lcfreader.rmobjets.RMInstruction;
import fr.bruju.lcfreader.rmobjets.RMMap;
import fr.bruju.lcfreader.structure.structure.Structures;

@SuppressWarnings("unused")
public class Main {
	public static void main(String[] args) throws InterruptedException {

		//String chemin = "A:\\Dev\\Projet\\";
		
		
		String chemin = "..\\RMEventReader\\ressources\\FichiersBruts\\";
		
		/*
		int idEv = 610;
		
		FabriqueLCF fabrique = new FabriqueLCF(chemin);
		
		RMEvenementCommun evenementCommun = fabrique.evenementCommun(idEv);
		
		List<RMInstruction> instructions = evenementCommun.instructions();
		
		instructions.forEach(i -> System.out.println(i.code() + " " + i.argument() + " " + Arrays.toString(i.parametres())));
		*/
		
		
		
		//int numeroDeMap = 2;
		
	
		testerLecture(chemin, "Save01.lsd");

		
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
