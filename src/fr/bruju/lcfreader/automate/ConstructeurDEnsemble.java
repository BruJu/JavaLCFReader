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
		
		Octets octets = new Octets(octetsDuFichier);
		
		String typeDObjet = octets.lireChaineDeTailleInconnue();
		
		
		
		return null;
	}

	
	
	
}
