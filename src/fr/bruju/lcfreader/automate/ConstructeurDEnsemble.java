package fr.bruju.lcfreader.automate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import fr.bruju.lcfreader.modele.EnsembleDeDonnees;

public class ConstructeurDEnsemble {
	
	
	public EnsembleDeDonnees lire(String chemin) {
		Path fichier = Paths.get(chemin);
		byte[] octetsDuFichier;
		try {
			octetsDuFichier = Files.readAllBytes(fichier);
		} catch (IOException e) {
			return null;
		}
		
		Octets octets = new Octets(octetsDuFichier, "BASE");
		
		String typeDObjet = octets.lireChaineDeTailleInconnue();
		
		String nomEnsemble;
		
		switch (typeDObjet) {
		case "LcfMapUnit":
			nomEnsemble = "Map";
			break;
		case "LcfMapTree":
			nomEnsemble = "TreeMap";
			break;
		// case "LcfDataBase": nomEnsemble = "Database";	break; // Non fonctionnel
		// case "LcfSaveData": nomEnsemble = "Save"; 	break; // Non fonctionnel
			
		default: // Type Inconnu
			System.out.println("Inconnu " + typeDObjet);
			return null;
		}
		
		
		EnsembleDeDonnees ensemble = octets.lireEnsemble(nomEnsemble);
		
		return ensemble;
	}
}
