package fr.bruju.lcfreader.structure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Il s'agit de la classe indiquant à quoi correspondent les différents codes rencontrés dans l'encodage BER.
 * <br>
 * Les fichiers sont en effet encodés sous la forme ([Code] [Taille] [Données])*. Il est donc important d'avoir
 * la connaissance de la correspondance entre les codes et comment décrypter les données qu'ils décrivent.
 * <br>Cette classe est codée avec une variante de singleton (on initialise manuellement le singleton avec le
 * nom du fichier) afin de ne pas devoir injecter sans arrêt l'instance de la classe. La base pouvant contenir des
 * codes de structures de donnéees différentes, une seule instance est nécessaire.
 * 
 * <br><br>Design pattern utilisé :
 * Variante de singleton
 * 
 * @author Bruju
 *
 */
public class BaseDeDonneesDesStructures {
	/* =========
	 * SINGLETON
	 * ========= */
	
	/**
	 * Initialise la base de données avec le fichier donné si cela n'a pas été fait
	 * @param fichier Le chemin vers le fichier contenant la base
	 */
	public static void initialiser(String fichier) {
		if (instance != null) {
			return;
		}
		
		instance = new BaseDeDonneesDesStructures();
		instance.remplirStructures(fichier);
	}
	
	/** Instance connue */
	private static BaseDeDonneesDesStructures instance;

	/** Constructeur privé */
	private BaseDeDonneesDesStructures() {
	}

	/** Donne l'instance de la base de données */
	public static BaseDeDonneesDesStructures getInstance() {
		return instance;
	}
	
	
	/* ===============
	 * BASE DE DONNEES
	 * =============== */
	
	/** Association nom de la structure - codes qu'elle contient */
	public Map<String, Structure> structures;
	
	/**
	 * Donne la structure contenant les codes pour la structure demandée
	 * @param nomStructure Le nom de la structure
	 * @return La structure contenant les codes voulus, ou null
	 */
	public Structure get(String nomStructure) {
		return structures.get(nomStructure);
	}
	
	/**
	 * Rempli la structure avec le fichier donné
	 * @param fichier Le chemin vers le fichier
	 */
	private void remplirStructures(String fichier) {
		File file = new File(fichier);
		
		structures = new HashMap<>();
		
		try {
			// Lire les noms de structure
			lireToutesLesLignes(file, ligne -> {
				String[] donnees = ligne.split(",", -1);
				structures.putIfAbsent(donnees[0], new Structure());
			});
			
			
			// Lire les arguments
			lireToutesLesLignes(file, ligne -> {
				String[] donnees = ligne.split(",", -1);
				structures.get(donnees[0]).ajouterChamp(donnees);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void lireToutesLesLignes(File file, Consumer<String> action) throws IOException {
		FileReader fileReader = new FileReader(file);
		BufferedReader buffer = new BufferedReader(fileReader);
		
		String ligne;
		
		while (true) {
			ligne = buffer.readLine();

			if (ligne == null) {
				break;
			}
			
			if (ligne.startsWith("#") || ligne.equals(""))
				continue;
			
			action.accept(ligne);
		}

		buffer.close();
	}
}
