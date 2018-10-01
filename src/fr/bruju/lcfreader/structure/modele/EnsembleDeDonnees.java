package fr.bruju.lcfreader.structure.modele;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.structure.bloc.Bloc;
import fr.bruju.lcfreader.structure.structure.Structure;
import fr.bruju.lcfreader.structure.structure.Structures;
import fr.bruju.lcfreader.structure.Donnee;

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


		XMLInsecticide.vider();
		// Connaître le type de fichier
		XMLInsecticide.balise("TypeDeFichier");
		int taille = lecteur.$lireUnNombreBER();
		
		XMLInsecticide.xml(" ==== ");
		String type = lecteur.$lireUneChaine(taille);
		
		XMLInsecticide.fermer();
		String nomStruct;

		switch (type) {
		case "LcfMapUnit":
			nomStruct = "Map";
			break;
		case "LcfMapTree":
			nomStruct = "TreeMap";
			break;
		case "LcfDataBase": nomStruct = "Database";	break; // Non fonctionnel
		case "LcfSaveData": nomStruct = "Save"; 	break; // Non fonctionnel
			
		default: // Type Inconnu
			System.out.println("Inconnu " + type);
			return null;
		}

		// Sequencer le reste du fichier
		
		
		XMLInsecticide.balise("ENSEMBLE_" + nomStruct);
		Structure structure = Structures.getInstance().get(nomStruct);
		EnsembleDeDonnees ensemble = structure.extraireDonnee(lecteur, lecteur.octetsRestants());
		
		XMLInsecticide.fermer("ENSEMBLE_" + nomStruct);

		//XMLInsecticide.ecrireDebug();
		
		XMLInsecticide.vider();

		
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
		StringBuilder sb = new StringBuilder();
		
		donnees.forEach(data -> sb.append(data.bloc.getTypeEnString() + " -> " + data.getString()).append("\n"));
		
		String chaine = sb.toString();

		System.out.println(chaine);

		PrintWriter pWriter;
		try {
			pWriter = new PrintWriter(new FileWriter("../structure.xml", false));
		} catch (IOException e) {
			return;
		}
        pWriter.print(chaine);
        pWriter.close();
		
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
