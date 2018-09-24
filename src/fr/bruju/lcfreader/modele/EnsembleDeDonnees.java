package fr.bruju.lcfreader.modele;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.structure.Structures;
import fr.bruju.lcfreader.structure.Donnee;
import fr.bruju.lcfreader.structure.Structure;
import fr.bruju.lcfreader.structure.blocs.Bloc;

/**
 * Classe représentant les valeurs liées à une structure de données encodés dans le fichier binaire
 * 
 * @author Bruju
 *
 */
public class EnsembleDeDonnees {
	/* =========================
	 * OBJET ENSEMBLE DE DONNEES
	 * ========================= */

	/** Nom de la structure */
	public final String nomStruct;

	/** Liste des données */
	private List<Donnee<?>> donnees;
	/** Tailles connues (données de type "size field") */
	private Map<String, Integer> tailles;

	/**
	 * Crée un conteneur de données pour la structure dont le nom est donné
	 * 
	 * @param structure Le nom de la structure
	 */
	public EnsembleDeDonnees(Structure structure) {
		donnees = new ArrayList<>();
		this.nomStruct = structure.nom;
	}

	// Services offerts pour la cons
	/**
	 * Enregistre le bloc de données dans l'ensemble
	 * 
	 * @param blocData L'ensemble de données à enregistrer
	 */
	public void push(Donnee<?> blocData) {
		donnees.add(blocData);

		if (blocData.bloc.estUnChampIndiquantLaTaille()) {
			if (tailles == null) {
				tailles = new HashMap<>();
			}

			tailles.put(blocData.bloc.nom, (Integer) blocData.value);
		}
	}

	/**
	 * Donne la taille du bloc de données si un champ taille a été lu pour ce bloc
	 * 
	 * @param bloc Le bloc dont on souhaite connaître la taille
	 * @return -1 si aucune taille n'est connue, le nombre d'octets du bloc si il est connu
	 */
	public int getTaille(Bloc<?> bloc) {
		if (tailles == null)
			return -1;

		return tailles.getOrDefault(bloc.nom, -1);
	}

	/* =======================================================
	 * INSTANCIER UN ENSEMBLE DE DONNEES A PARTIR D'UN FICHIER
	 * ======================================================= */

	/**
	 * Lit le fichier et en donne une représentation dans la mémoire
	 * 
	 * @param chemin Le fichier
	 * @return L'objet représentant le fichier, null si non lisible
	 */
	public static EnsembleDeDonnees lireFichier(String chemin) {
		Desequenceur lecteur = Desequenceur.instancier(chemin);


		Desequenceur.vider();
		// Connaître le type de fichier
		Desequenceur.balise("TypeDeFichier");
		int taille = lecteur.$lireUnNombreBER();
		
		Desequenceur.xml += " ==== ";
		String type = lecteur.$lireUneChaine(taille);
		
		Desequenceur.fermer();
		String nomStruct;

		switch (type) {
		case "LcfMapUnit":
			nomStruct = "Map";
			break;
		case "LcfMapTree":
			nomStruct = "TreeMap";
			break;
		case "LcfDataBase": nomStruct = "Database";	break; // Non fonctionnel
		// case "LcfSaveData": nomStruct = "Save"; 	break; // Non fonctionnel
			
		default: // Type Inconnu
			System.out.println("Inconnu " + type);
			return null;
		}

		// Sequencer le reste du fichier
		
		
		Desequenceur.balise("ENSEMBLE_" + nomStruct);
		Structure structure = Structures.getInstance().get(nomStruct);
		EnsembleDeDonnees ensemble = structure.lireOctet(lecteur, lecteur.octetsRestants());
		
		Desequenceur.fermer("ENSEMBLE_" + nomStruct);

		try {
			Desequenceur.ecrireDebug();
		} catch (IOException e) {
		}
		
		return ensemble;
	}

	/* =================
	 * ACCES AUX DONNEES
	 * ================= */
	
	public <T> T getDonnee(String nomBloc, Class<T> classe) {
		Bloc<?> bloc = Structures.getInstance().get(nomStruct).getBloc(nomBloc);
		
		for (Donnee<?> donnee : donnees) {
			if (donnee.bloc == bloc) {
				return classe.cast(donnee.value);
			}
		}
		
		return classe.cast(bloc.valeurParDefaut());
	}
	
	
	/* ====================
	 * SERVICES D'AFFICHAGE
	 * ==================== */

	// Pour le debug / comprendre ce qu'il y a dans cette classe

	/** Affiche les données contenues par l'objet */
	public void afficherDonnees() {
		donnees.forEach(data -> System.out.println(data.bloc.getTypeEnString() + " -> " + data.getString()));
	}

	/** Affiche l'architecture des données en considérant que le niveau est 0 */
	public void afficherArchitecture() {
		afficherArchitecture(0);
	}

	/**
	 * Affiche l'architecture des données avec une marge égale à niveau
	 * 
	 * @param niveau La marge
	 */
	public void afficherArchitecture(int niveau) {
		Utilitaire.tab(niveau);
		System.out.println(nomStruct);
		donnees.forEach(data -> {
			Utilitaire.tab(niveau);
			System.out.print(data.bloc.getTypeEnString());

			if (data.value instanceof byte[]) {
				System.out.print(data.getString());
			}

			System.out.println();
			data.afficherSousArchi(niveau + 1);
		});
	}

	/**
	 * Donne une représentation en ligne du type NomStructure -> nomChamp1:valeur1 ; nomChamp2:valeur2
	 * 
	 * @return La représentation en ligne du type NomStructure -> nomChamp1:valeur1 ; nomChamp2:valeur2
	 */
	public String getRepresentationEnLigne() {
		return nomStruct + " -> "
				+ donnees.stream().map(d -> d.bloc.nom + ":" + d.getString()).collect(Collectors.joining(" ; "));
	}
}
