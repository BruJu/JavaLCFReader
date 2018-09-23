package fr.bruju.lcfreader.structure.blocs;

import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.sequenceur.lecteurs.Desequenceur;
import fr.bruju.lcfreader.sequenceur.sequences.SequenceurLCFAEtat;
import fr.bruju.lcfreader.structure.Donnee;

/**
 * Un bloc de données correspondant à un ensemble de données
 * 
 * @author Bruju
 *
 */
public class BlocEnsembleDeDonnees extends Bloc<EnsembleDeDonnees> {

	
	
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */

	/** Nom de la structure */
	private String nomStructure;

	/**
	 * Bloc constitué d'un ensemble de données
	 * 
	 * @param champ Caractéristiques
	 * @param nomStructure Nom de la structure
	 */
	public BlocEnsembleDeDonnees(Champ champ, String nomStructure) {
		super(champ);
		this.nomStructure = nomStructure;
	}

	/* ====================
	 * PROPRIETES D'UN BLOC
	 * ==================== */

	@Override
	public String getNomType() {
		return "#" + nomStructure;
	}

	/* =====================
	 * CONSTRUIRE UNE VALEUR
	 * ===================== */


	/* ============================
	 * INTERACTION AVEC LES VALEURS
	 * ============================ */

	@Override
	public String convertirEnChaineUneValeur(EnsembleDeDonnees value) {
		return value.getRepresentationEnLigne();
	}

	@Override
	public void afficherSousArchi(int niveau, EnsembleDeDonnees value) {
		value.afficherArchitecture(niveau);
	}

	@Override
	public EnsembleDeDonnees extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		return SequenceurLCFAEtat.instancier(new EnsembleDeDonnees(nomStructure))
				.lireOctet(desequenceur, tailleLue);
	}
}
