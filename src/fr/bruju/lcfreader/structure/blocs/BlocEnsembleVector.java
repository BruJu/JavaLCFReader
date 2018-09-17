package fr.bruju.lcfreader.structure.blocs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.sequenceur.sequences.ConvertisseurOctetsVersDonnees;
import fr.bruju.lcfreader.sequenceur.sequences.LecteurDeSequence;
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

	@Override
	public ConvertisseurOctetsVersDonnees<List<EnsembleDeDonnees>> getHandler(int tailleLue) {
		return new H(tailleLue);
	}

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

	/**
	 * Convertisseur d'un tableau d'octets représentants un vecteur d'ensemble de données en une liste d'ensemble de
	 * données.
	 * 
	 * @author Bruju
	 *
	 */
	public class H implements ConvertisseurOctetsVersDonnees<List<EnsembleDeDonnees>> {
		/** Données lues */
		private List<EnsembleDeDonnees> ensembles;

		/** Le lecteur de séquence de l'ensemble de données en cours de lecture */
		private LecteurDeSequence<EnsembleDeDonnees> sequenceur = null;

		/** Nombre d'octets restant à lire */
		private int nombreDOctetsRestants;

		/**
		 * Construit le convertisseur
		 * 
		 * @param taille Le nombre d'octets du vecteur
		 */
		public H(int taille) {
			if (taille == -1) {
				throw new RuntimeException("Vecteur de taille inconnue");
			}

			this.nombreDOctetsRestants = taille;
			this.ensembles = new ArrayList<>();
		}

		@Override
		public Donnee<List<EnsembleDeDonnees>> accumuler(byte octet) {
			if (sequenceur == null) {
				sequenceur = SequenceurLCFAEtat.instancier(new EnsembleDeDonnees(nomStructure));
			}

			boolean seq = sequenceur.lireOctet(octet);

			if (!seq) {
				ensembles.add(sequenceur.getResultat());
				sequenceur = null;
			}

			nombreDOctetsRestants--;

			return (nombreDOctetsRestants == 0) ? new Donnee<>(BlocEnsembleVector.this, ensembles) : null;
		}
	}
}
