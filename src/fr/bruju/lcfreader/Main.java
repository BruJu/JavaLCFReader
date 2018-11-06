package fr.bruju.lcfreader;

import java.io.FileNotFoundException;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.rmobjets.RMEvenementCommun;
import fr.bruju.lcfreader.rmobjets.RMMap;
import fr.bruju.lcfreader.services.LecteurDeLCF;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;

/**
 * Classe principale pour les tests manuels
 * 
 * @author Bruju
 *
 */
public class Main {	
	/**
	 * Fonction principale
	 * @param args Arguments du programme
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		String chemin;

		int numeroChemin = 1; // 0 = brut, 1 = dev
		int typeOperation = 1; // 0 = lecture de fichier, 1 = lecture d'abstractions, 2 = liste des noms
		int typeDeDonnees = 0; // 0 = map, 1 = bdd | 0 = map, 1 = ec
		int numeroElement = 2;
		
		String chemins[] = {
				"..\\RMEventReader\\ressources\\FichiersBruts\\",
				"A:\\Dev\\Projet\\"
		};
		
		chemin = chemins[numeroChemin];
		
		if (typeOperation == 0) {
			String nomFichier = chemin;
			
			if (typeDeDonnees == 0) {
				nomFichier += "Map" + String.format("%04d", numeroElement) + ".lmu";
			} else if (typeDeDonnees == 1) {
				nomFichier += "RPG_RT.ldb";
			} else if (typeDeDonnees == 2) {
				nomFichier += "RPG_RT.lmt";
			} else if (typeDeDonnees == 3) {
				nomFichier += "Save0" + numeroElement + ".lsd";
			}
			EnsembleDeDonnees map = EnsembleDeDonnees.lireFichier(nomFichier);
			
			//System.setOut(new PrintStream(new File("../structure.txt")));
			
			System.out.println("}}-- " + nomFichier + " --{{");
			System.out.println(map.afficherDonnees());
			System.out.println();
			System.out.println(map.afficherArchitecture());
		} else if (typeOperation == 1) {
			LecteurDeLCF arborescence = new LecteurDeLCF(chemin);
			
			if (typeDeDonnees == 0) {
				System.out.println(RMMap.toString(arborescence.map(numeroElement)));
			} else if (typeDeDonnees == 1) {
				System.out.println(RMEvenementCommun.toString(arborescence.evenementCommun(numeroElement)));
			}
		} else {
			LecteurDeLCF arborescence = new LecteurDeLCF(chemin);
			
			String[] categories = {"actors", "items", "switches", "variables"};
			
			for (String categorie : categories) {
				System.out.println(categorie + " -> " + arborescence
											   .getListeDeNoms(categorie)
											   .stream()
											   .limit(50)
											   .collect(Collectors.joining(", ")));
			}
		}
	}
}
