package fr.bruju.lcfreader.structure.structure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.bruju.lcfreader.structure.MiniBloc;

/**
 * Il s'agit de la classe indiquant à quoi correspondent les différents codes rencontrés dans l'encodage BER. <br>
 * Les fichiers sont en effet encodés sous la forme ([Code] [Taille] [Données])*. Il est donc important d'avoir la
 * connaissance de la correspondance entre les codes et comment décrypter les données qu'ils décrivent. <br>
 * Cette classe est codée avec une variante de singleton (on initialise manuellement le singleton avec le nom du
 * fichier) afin de ne pas devoir injecter sans arrêt l'instance de la classe. La base pouvant contenir des codes de
 * structures de donnéees différentes, une seule instance est nécessaire.
 * 
 * <br>
 * <br>
 * Design pattern utilisé : Singleton
 * 
 * @author Bruju
 *
 */
public class Structures {
	private static final String FIELDSCSV = "liblcf/fields.csv";
	
	/* =========
	 * SINGLETON
	 * ========= */

	/** Initialise la base de données avec le fichier donné si cela n'a pas été fait */
	private static void initialiser() {
		if (instance != null) {
			return;
		}

		instance = new Structures();
		instance.remplirStructures(); // Remplir après l'assignation permet de ne pas initialiser à l'infini
	}

	/** Instance connue */
	private static Structures instance;

	/** Constructeur privé */
	private Structures() {
	}

	/**
	 * Rempli la liste des structures avec le fichier FIELDSCSV.
	 */
	private void remplirStructures() {
		// On parcours deux fois les données du fichier resssource :
		// - Une première fois pour le nom des structures
		// - Une seconde fois pour les champs
		// Cela est fait pour pouvoir référencer des structures qui sont plus loin dans la liste

		URL is = getClass().getClassLoader().getResource(FIELDSCSV);

		//File file = new File(fichier);
		structures = new HashMap<>();

		try {
			// Lire les noms de structure

			List<String[]> donneesLues = lireToutesLesLignes(is.openStream());

			donneesLues.forEach(donnees -> {
				String nomStructure = donnees[1];

				if (!structures.containsKey(nomStructure)) {
					Structure structure = (donnees[5].equals("")) ?
							new StructureSerie(nomStructure) : new StructureDiscontinue(nomStructure);

					structures.put(nomStructure, structure);
				}
			});

			donneesLues.forEach(donnees -> structures.get(donnees[1]).ajouterChamp(donnees));

			donneesLues.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Donne l'instance de la base de données */
	public static Structures getInstance() {
		Structures.initialiser();
		
		return instance;
	}

	/* ===============
	 * BASE DE DONNEES
	 * =============== */

	/** Association nom de la structure - codes qu'elle contient */
	private Map<String, Structure> structures;

	/**
	 * Donne la structure contenant les codes pour la structure demandée
	 * 
	 * @param nomStructure Le nom de la structure
	 * @return La structure contenant les codes voulus, ou null
	 */
	public Structure get(String nomStructure) {
		return structures.get(nomStructure);
	}

	/**
	 * Lis toutes les lignes du fichier en appliquant action
	 * 
	 * @param is Le flux de lecture des données du fichier fields.csv
	 * @throws IOException
	 */
	private static List<String[]> lireToutesLesLignes(InputStream is) throws IOException {
		List<String[]> donneesLues = new ArrayList<>();
		
		BufferedReader buffer = new BufferedReader(new InputStreamReader(is));

		String ligne;

		while ((ligne = buffer.readLine()) != null) {
			if (!ligne.startsWith("#") && !ligne.equals("")) {
				donneesLues.add(ligne.split(",", -1));
			}
		}
		
		buffer.close();
		
		return donneesLues;
	}

	/**
	 * Ajoute dans la map donnée les champs connus
	 * @param miniBlocsConnus La carte où ajouter les champs
	 */
	public void injecter(Map<String, MiniBloc<?>> miniBlocsConnus) {
		miniBlocsConnus.putAll(structures);
	}
}
