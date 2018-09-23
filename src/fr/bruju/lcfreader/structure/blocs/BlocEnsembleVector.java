package fr.bruju.lcfreader.structure.blocs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.sequenceur.lecteurs.Desequenceur;
import fr.bruju.lcfreader.sequenceur.sequences.Sequenceur;
import fr.bruju.lcfreader.sequenceur.sequences.SequenceurLCFAEtat;
import fr.bruju.lcfreader.structure.Donnee;

/**
 * Un bloc de données correspondant à un vecteur d'ensemble de données. Dans un vecteur, les données n'ont pas
 * d'identifiant et ont ne sait pas coment il y en aura.
 * 
 * @author Bruju
 *
 */
public class BlocEnsembleVector extends Bloc<List<EnsembleDeDonnees>> {
	

	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */

	/** Nom de la structure contenue */
	private String nomStructure;

	/**
	 * Bloc constitué d'un vecteur d'ensemble de données
	 * 
	 * @param champ Les caractéristiques
	 * @param nomStructure Le nom de l'ensemble
	 */
	public BlocEnsembleVector(Champ champ, String nomStructure) {
		super(champ);
		this.nomStructure = nomStructure;
	}

	/* ====================
	 * PROPRIETES D'UN BLOC
	 * ==================== */

	@Override
	public String getNomType() {
		return "Vector<#" + nomStructure + ">";
	}

	/* =====================
	 * CONSTRUIRE UNE VALEUR
	 * ===================== */


	/* ============================
	 * INTERACTION AVEC LES VALEURS
	 * ============================ */

	@Override
	public String convertirEnChaineUneValeur(List<EnsembleDeDonnees> valeur) {
		String contenu = valeur
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
	public void afficherSousArchi(int niveau, List<EnsembleDeDonnees> value) {
		value.forEach(data -> data.afficherArchitecture(niveau));
	}

	/* =============
	 * CONVERTISSEUR
	 * ============= */


	@Override
	public List<EnsembleDeDonnees> extraireDonnee(Desequenceur desequenceur, int taille) {
		if (taille == -1) {
			throw new RuntimeException("Vecteur de taille inconnue");
		}
		
		if (taille != desequenceur.octetsRestants())
			throw new RuntimeException("!!");
		
		List<EnsembleDeDonnees> ensembles = new ArrayList<>();
		Sequenceur<EnsembleDeDonnees> sequenceur = null;
		
		while (desequenceur.nonVide()) {
			// TODO : garder le même séquenceur quand les objets seront devenus stateless
			sequenceur = SequenceurLCFAEtat.instancier(new EnsembleDeDonnees(nomStructure));
			
			ensembles.add(sequenceur.lireOctet(desequenceur, -1));
		}
		
		return ensembles;
	}
}
