package fr.bruju.lcfreader.structure.blocs;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.sequenceur.lecteurs.Desequenceur;
import fr.bruju.lcfreader.sequenceur.sequences.SequenceurLCFAEtat;
import fr.bruju.lcfreader.structure.Structure;

/**
 * Un bloc de données correspondant à un tableau d'ensemble de données
 * 
 * @author Bruju
 *
 */
public class BlocArray extends Bloc<Map<Integer, EnsembleDeDonnees>> {
	
	
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */

	private Structure structure;
	
	/** Nom de la structure contenue dans chaque case du tableau */
	private String nomStructure;

	/**
	 * Bloc constitué d'un tableau d'ensemble de données
	 * 
	 * @param champ Les caractéristiques
	 * @param nomStructure Le nom de l'ensemble
	 */
	public BlocArray(Champ champ, Structure structure) {
		super(champ);
		this.structure = structure;
	}

	/* ====================
	 * PROPRIETES D'UN BLOC
	 * ==================== */

	@Override
	public String getNomType() {
		return "Tableau[" + nomStructure + "]";
	}

	/* ============================
	 * INTERACTION AVEC LES VALEURS
	 * ============================ */

	@Override
	public String convertirEnChaineUneValeur(Map<Integer, EnsembleDeDonnees> valeur) {
		String contenu = valeur
				.values()
				.stream()
				.map(d -> d.getRepresentationEnLigne())
				.collect(Collectors.joining(";"));

		return new StringBuilder()
				.append("[")
				.append(contenu)
				.append("]")
				.toString();
	}

	@Override
	public void afficherSousArchi(int niveau, Map<Integer, EnsembleDeDonnees> value) {
		value.values().forEach(data -> data.afficherArchitecture(niveau));
	}

	/* =====================
	 * CONSTRUIRE UNE VALEUR
	 * ===================== */


	/* =============
	 * CONVERTISSEUR
	 * ============= */


	@Override
	public Map<Integer, EnsembleDeDonnees> extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		int nombreElements = desequenceur.$lireUnNombreBER();
		
		int id;
		EnsembleDeDonnees ensemble;
		
		Map<Integer, EnsembleDeDonnees> carte = new LinkedHashMap<>();
		
		
		while (nombreElements != 0) {
			id = desequenceur.$lireUnNombreBER();
			
			
			ensemble = SequenceurLCFAEtat.instancier(structure).lireOctet(desequenceur, -1);
			
			carte.put(id, ensemble);
			
			nombreElements--;
		}
		
		return carte;
	}
}
