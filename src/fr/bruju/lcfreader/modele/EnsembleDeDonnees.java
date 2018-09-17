package fr.bruju.lcfreader.modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.debug.BytePrinter;
import fr.bruju.lcfreader.sequenceur.lecteurs.LecteurDeFichierOctetParOctet;
import fr.bruju.lcfreader.sequenceur.sequences.SequenceurLCFAEtat;
import fr.bruju.lcfreader.sequenceur.sequences.TailleChaine;
import fr.bruju.lcfreader.structure.Donnee;
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
	 * @param nomStruct Le nom de la structure
	 */
	public EnsembleDeDonnees(String nomStruct) {
		donnees = new ArrayList<>();
		this.nomStruct = nomStruct;
	}

	// Services offerts pour la constitution
	
	/**
	 * Enregistre le bloc de données dans l'ensemble
	 * @param blocData L'ensemble de données à enregistrer
	 */
	public void push(Donnee<?> blocData) {
		donnees.add(blocData);
		
		if (blocData.bloc.getChamp().sized) {
			if (tailles == null)
				tailles = new HashMap<>();
			
			tailles.put(blocData.bloc.getChamp().nom, (Integer) blocData.value);
		}
	}
	
	/**
	 * Donne la taille du bloc de données si un champ taille a été lu pour ce bloc
	 * @param bloc Le bloc dont on souhaite connaître la taille
	 * @return -1 si aucune taille n'est connue, le nombre d'octets du bloc si il est connu
	 */
	public int getTaille(Bloc<?> bloc) {
		if (tailles == null)
			return -1;
		
		return tailles.getOrDefault(bloc.getChamp().nom, -1);
	}
	

	/* =======================================================
	 * INSTANCIER UN ENSEMBLE DE DONNEES A PARTIR D'UN FICHIER
	 * ======================================================= */

	/**
	 * Lit le fichier et en donne une représentation dans la mémoire
	 * @param chemin Le fichier
	 * @return L'objet représentant le fichier, null si non lisible
	 */
	public static EnsembleDeDonnees lireFichier(String chemin) {
		LecteurDeFichierOctetParOctet lecteur = LecteurDeFichierOctetParOctet.instancier(chemin);
		
		// Connaître le type de fichier
		String type = lecteur.sequencer(new TailleChaine());
		String nomStruct;
		
		switch (type) {
		case "LcfMapUnit":
			nomStruct = "Map";
			break;
		default: // Type Inconnu
			System.out.println("Inconnu " + type);
			return null;
		}

		// Sequencer le reste du fichier
		EnsembleDeDonnees data = new EnsembleDeDonnees(nomStruct);
		lecteur.sequencer(SequenceurLCFAEtat.instancier(data));
		
		// Renvoyer les données
		lecteur.fermer();
		return data;
	}
	
	/* ====================
	 * SERVICES D'AFFICHAGE
	 * ==================== */
	
	// Pour le debug / comprendre ce qu'il y a dans cette classe
	
	/** Affiche les données contenues par l'objet */
	public void afficherDonnees() {
		donnees.forEach(data -> System.out.println(data.bloc.getTrueRepresetantion() + " -> " + data.valueToString()));
	}
	
	/** Affiche l'architecture des données en considérant que le niveau est 0 */
	public void afficherArchitecture() {
		afficherArchitecture(0);
	}
	
	/**
	 * Affiche l'architecture des données avec une marge égale à niveau
	 * @param niveau La marge
	 */
	public void afficherArchitecture(int niveau) {
		Utilitaire.tab(niveau);
		System.out.println(nomStruct);
		donnees.forEach(data -> {
			Utilitaire.tab(niveau);
			System.out.print(data.bloc.getTrueRepresetantion());
			
			if (data.value instanceof byte[]) {
				System.out.print(" " + BytePrinter.getTable((byte[]) data.value));
			}
			
			System.out.println();
			data.afficherSousArchi(niveau+1);
		});
	}
	
	/**
	 * Donne une représentation en ligne du type NomStructure -> nomChamp1:valeur1 ; nomChamp2:valeur2 
	 * @return La représentation en ligne du type NomStructure -> nomChamp1:valeur1 ; nomChamp2:valeur2 
	 */
	public String getRepresentationEnLigne() {
		return nomStruct + " -> " +
			donnees.stream()
				   .map(d -> d.bloc.getChamp().nom + ":" + d.valueToString())
				   .collect(Collectors.joining(" ; "));
	}
}
