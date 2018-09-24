package fr.bruju.lcfreader.structure.blocs;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.structure.Structure;
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
	private Structure structure;

	/**
	 * Bloc constitué d'un ensemble de données
	 * 
	 * @param champ Caractéristiques
	 * @param nomStructure Nom de la structure
	 */
	public BlocEnsembleDeDonnees(Champ champ, Structure structure) {
		super(champ);
		this.structure = structure;
	}

	/* ====================
	 * PROPRIETES D'UN BLOC
	 * ==================== */

	@Override
	public String getNomType() {
		return "#" + structure.nom;
	}

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

	/* =====================
	 * CONSTRUIRE UNE VALEUR
	 * ===================== */
	
	@Override
	public EnsembleDeDonnees extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		Desequenceur.balise("ENSEMBLE_" + structure.nom);
		EnsembleDeDonnees ensemble = structure.lireOctet(desequenceur, tailleLue);
		Desequenceur.fermer();
		return ensemble;
	}
}
