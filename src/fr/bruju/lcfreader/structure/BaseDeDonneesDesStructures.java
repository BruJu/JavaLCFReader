package fr.bruju.lcfreader.structure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BaseDeDonneesDesStructures {
	public Map<String, Structure> structures;
	
	
	
	public BaseDeDonneesDesStructures(String fichier) {
		remplirStructures(fichier);
	}



	private void remplirStructures(String fichier) {
		File file = new File(fichier);
		
		structures = new HashMap<>();
		
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader buffer = new BufferedReader(fileReader);
			String line;
	
			while (true) {
				line = buffer.readLine();
	
				if (line == null) {
					break;
				}
				
				if (line.startsWith("#") || line.equals(""))
					continue;
				
				String[] donnees = line.split(",", -1);
				
				structures.putIfAbsent(donnees[0], new Structure());
				
				structures.get(donnees[0]).ajouterChamp(donnees);
			}
	
			buffer.close();
		} catch (IOException e) {
			
		}
	}
}
