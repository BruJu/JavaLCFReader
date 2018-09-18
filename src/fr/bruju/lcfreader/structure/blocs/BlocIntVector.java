package fr.bruju.lcfreader.structure.blocs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.bruju.lcfreader.sequenceur.sequences.ConvertisseurOctetsVersDonnees;
import fr.bruju.lcfreader.sequenceur.sequences.LecteurDeSequence;
import fr.bruju.lcfreader.sequenceur.sequences.NombreBER;
import fr.bruju.lcfreader.structure.Donnee;
import fr.bruju.lcfreader.structure.types.PrimitifCpp;

public class BlocIntVector extends Bloc<int[]> {
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */

	/** Nom du type primitif C++ */
	public final String nomPrimitive;

	/**
	 * Bloc qui est un vecteur d'un type primitif c++ qui est converti ici en int
	 * 
	 * @param champ Les caractéristiques
	 * @param nomPrimitive Le nom du type
	 */
	public BlocIntVector(Champ champ, String nomPrimitive) {
		super(champ);
		this.nomPrimitive = nomPrimitive;
	}

	/* ====================
	 * PROPRIETES D'UN BLOC
	 * ==================== */

	@Override
	public String getNomType() {
		return "Vector<" + nomPrimitive + ">";
	}

	/* =====================
	 * CONSTRUIRE UNE VALEUR
	 * ===================== */

	@Override
	public ConvertisseurOctetsVersDonnees<int[]> getHandler(int tailleLue) {
		return new H(tailleLue);
	}

	/* ============================
	 * INTERACTION AVEC LES VALEURS
	 * ============================ */

	@Override
	public String convertirEnChaineUneValeur(int[] values) {
		return Arrays.toString(values);
	}

	/* =============
	 * CONVERTISSEUR
	 * ============= */

	// TODO : il est surement possible d'utiliser un lecteur de séquence intermédiaire

	public class H implements ConvertisseurOctetsVersDonnees<int[]> {
		PrimitifCpp primitif = PrimitifCpp.map.get(nomPrimitive);

		//private NombreBER decodageTaille;

		private int nombreDOctetsRestants;
		
		private LecteurDeSequence<Integer> lecteurDeValeur;
		
		private List<Integer> valeursLues;

		public H(int tailleLue) {
			if (tailleLue == -1) {
				lecteurDeValeur = new NombreBER();
				nombreDOctetsRestants = -1;
			} else {
				tailleConnue(tailleLue);
			}
		}

		private void tailleConnue(int taille) {
			valeursLues = new ArrayList<>();
			nombreDOctetsRestants = taille;
			lecteurDeValeur = primitif.getLecteur();
		}


		@Override
		public Donnee<int[]> accumuler(byte octetRecu) {
			// Decompte des octets restants à lire
			if (nombreDOctetsRestants != -1) {
				nombreDOctetsRestants--;
			}
			
			boolean finDeLaLectureDUnNombre = !lecteurDeValeur.lireOctet(octetRecu);
			
			if (finDeLaLectureDUnNombre) {
				if (nombreDOctetsRestants == -1) {	// Taille en octets du vecteur
					tailleConnue(lecteurDeValeur.getResultat());
				} else { // Nombre contenu dans le vecteur
					valeursLues.add(lecteurDeValeur.getResultat());
					lecteurDeValeur = primitif.getLecteur();
				}
			}
			
			return nombreDOctetsRestants != 0 ? null : new Donnee<int[]>(BlocIntVector.this, getTableau());
		}

		private int[] getTableau() {
			int[] valeurs = new int[valeursLues.size()];
			
			for (int i = 0 ; i != valeurs.length ; i++) {
				valeurs[i] = valeursLues.get(i);
			}
			
			return valeurs;
		}
	}
}
