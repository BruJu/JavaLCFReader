package fr.bruju.lcfreader;

import java.util.function.Consumer;

import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.modele.FabriqueLCF;
import fr.bruju.lcfreader.rmobjets.RMMap;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		testerLecture();
		
		
		RMMap carte = new FabriqueLCF("A:\\Dev\\Projet\\").map(1);
		
		afficherRMMap(carte);

	}
	
	private static void afficherRMMap(RMMap carte) {
		System.out.println("• Carte " +carte.id() + " / " + carte.nom());
		
		carte.evenements().stream().forEach(evenement -> {
			System.out.println("•• Evenement " + evenement.id() + " [" + evenement.x() + ", " + evenement.y() + "]");
			evenement.pages().forEach(page -> {
				System.out.println("••• Page " + page.id());
				page.instructions().forEach(instruction -> {
					System.out.print("••• Instruction " + instruction.code() + " '" + instruction.argument() + "' ");
					
					for (int p : instruction.parametres()) {
						System.out.println(p + " ");
					}
					
					System.out.println();
				});
				
				
				
			});
		});
	}

	@SuppressWarnings("unused")
	private static void testerLecture() {
		BaseDeDonneesDesStructures.initialiser("ressources\\liblcf\\fields.csv");

		String[] fichiers = new String[] { "Projet\\Map0001", "Map0452", "Map0001", "Map0003" };

		doubleAffichage(fichiers[2]);
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
