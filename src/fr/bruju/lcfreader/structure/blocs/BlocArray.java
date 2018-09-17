package fr.bruju.lcfreader.structure.blocs;

import java.util.TreeMap;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.sequenceur.sequences.ConvertisseurOctetsVersDonnees;
import fr.bruju.lcfreader.sequenceur.sequences.SequenceurLCFAEtat;
import fr.bruju.lcfreader.structure.Donnee;

/**
 * Un bloc de données correspondant à un tableau d'ensemble de données
 * 
 * @author Bruju
 *
 */
public class BlocArray extends Bloc<TreeMap<Integer, EnsembleDeDonnees>> {
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */

	/** Nom de la structure contenue dans chaque case du tableau */
	private String nomStructure;

	/**
	 * Bloc constitué d'un tableau d'ensemble de données
	 * 
	 * @param champ Les caractéristiques
	 * @param nomStructure Le nom de l'ensemble
	 */
	public BlocArray(Champ champ, String nomStructure) {
		super(champ);
		this.nomStructure = nomStructure;
	}

	/* ====================
	 * PROPRIETES D'UN BLOC
	 * ==================== */

	@Override
	public String getNomType() {
		return "Tableau[" + nomStructure + "]";
	}

	/* =====================
	 * CONSTRUIRE UNE VALEUR
	 * ===================== */

	@Override
	public ConvertisseurOctetsVersDonnees<TreeMap<Integer, EnsembleDeDonnees>> getHandler(int tailleLue) {
		return new H();
	}

	/* ============================
	 * INTERACTION AVEC LES VALEURS
	 * ============================ */

	@Override
	public String convertirEnChaineUneValeur(TreeMap<Integer, EnsembleDeDonnees> valeur) {
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
	public void afficherSousArchi(int niveau, TreeMap<Integer, EnsembleDeDonnees> value) {
		value.values().forEach(data -> data.afficherArchitecture(niveau));
	}

	/* =============
	 * CONVERTISSEUR
	 * ============= */

	/** Etats de l'automate */
	private static enum Etat {
		LireNombreElements, LireIndex, LireDonnees
	}

	// TODO : utiliser de vrais états

	public class H implements ConvertisseurOctetsVersDonnees<TreeMap<Integer, EnsembleDeDonnees>> {
		/** Etat actuel */
		private Etat etat;
		/** Données lues */
		private TreeMap<Integer, EnsembleDeDonnees> map;

		/** Nombre d'éléments dans le tableau */
		private int nombreDElements;
		/** Séquenceur */
		private SequenceurLCFAEtat sequenceur;

		/**
		 * Construit le convertisseur avec l'état "lire le nombre d'élément"
		 */
		private H() {
			etat = Etat.LireNombreElements;
		}

		@Override
		public Donnee<TreeMap<Integer, EnsembleDeDonnees>> accumuler(byte octet) {
			switch (etat) {
			case LireNombreElements:
				nombreDElements = octet;
				map = new TreeMap<Integer, EnsembleDeDonnees>();

				if (nombreDElements == 0) {
					return new Donnee<>(BlocArray.this, map);
				}

				etat = Etat.LireIndex;
				break;
			case LireIndex:
				EnsembleDeDonnees donneeCourante = new EnsembleDeDonnees(nomStructure);
				map.put((int) octet, donneeCourante);
				sequenceur = SequenceurLCFAEtat.instancier(donneeCourante);
				etat = Etat.LireDonnees;
				break;
			case LireDonnees:
				if (!sequenceur.lireOctet(octet)) {
					if (map.size() == nombreDElements) {
						return new Donnee<>(BlocArray.this, map);
					} else {
						etat = Etat.LireIndex;
					}
				}
				break;
			}

			return null;
		}
	}
}
