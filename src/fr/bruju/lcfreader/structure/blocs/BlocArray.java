package fr.bruju.lcfreader.structure.blocs;

import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.sequenceur.sequences.ConvertisseurOctetsVersDonnees;
import fr.bruju.lcfreader.sequenceur.sequences.Enchainement;
import fr.bruju.lcfreader.sequenceur.sequences.Etat;
import fr.bruju.lcfreader.sequenceur.sequences.LecteurDeSequence;
import fr.bruju.lcfreader.sequenceur.sequences.NombreBER;
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

	/* =====================
	 * CONSTRUIRE UNE VALEUR
	 * ===================== */

	@Override
	public ConvertisseurOctetsVersDonnees<TreeMap<Integer, EnsembleDeDonnees>> getHandler(int tailleLue) {
		Enchainement<Integer, TreeMap<Integer, EnsembleDeDonnees>> enchainement =
				new Enchainement<>(new NombreBER.NonNull(), ConstructeurArbre::new, TreeMap::new);
		return new ConvertisseurOctetsVersDonnees.ViaSequenceur<>(enchainement, r -> new Donnee<>(this, r));
	}

	/* =============
	 * CONVERTISSEUR
	 * ============= */

	/** Etats de l'automate */
	
	private class ConstructeurArbre implements LecteurDeSequence<TreeMap<Integer, EnsembleDeDonnees>> {

		private int nombreDElementsRequis;
		
		private TreeMap<Integer, EnsembleDeDonnees> donneesConnues = new TreeMap<>();
		
		private Etat etatActuel = new EtatLectureIndex();

		public ConstructeurArbre(Integer taille) {
			this.nombreDElementsRequis = taille;
		}

		@Override
		public boolean lireOctet(byte octet) {
			etatActuel = etatActuel.lireOctet(octet);
			return etatActuel != null;
		}

		@Override
		public TreeMap<Integer, EnsembleDeDonnees> getResultat() {
			return donneesConnues;
		}
		
		/** Lit l'index du prochain élément */
		private class EtatLectureIndex implements Etat {
			/** Lecteur de nombre encodé en BER */
			private NombreBER nombreBER = new NombreBER();
			
			@Override
			public Etat lireOctet(byte octet) {
				if (nombreBER.lireOctet(octet)) {
					return this;
				} else {
					return new EtatLireValeur(nombreBER.getResultat());
				}
			}
		}
		
		/** Lit le contenu de la valeur */
		private class EtatLireValeur implements Etat {
			private SequenceurLCFAEtat sequenceur;
			
			public EtatLireValeur(Integer index) {
				EnsembleDeDonnees valeur = new EnsembleDeDonnees(nomStructure);
				donneesConnues.put(index, valeur);
				sequenceur = SequenceurLCFAEtat.instancier(valeur);
			}

			@Override
			public Etat lireOctet(byte octet) {
				if (sequenceur.lireOctet(octet)) {
					return this;
				} else {
					nombreDElementsRequis--;
					
					if (nombreDElementsRequis != 0) {
						return new EtatLectureIndex();
					} else {
						return null;
					}
				}
			}
		}
		
		
	}

	/*
	public class H implements ConvertisseurOctetsVersDonnees<TreeMap<Integer, EnsembleDeDonnees>> {

		private Etat etat;

		private TreeMap<Integer, EnsembleDeDonnees> map;

		private int nombreDElements;

		private SequenceurLCFAEtat sequenceur;

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
	*/
	
	
}
