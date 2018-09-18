package fr.bruju.lcfreader.structure.blocs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import fr.bruju.lcfreader.sequenceur.sequences.ConvertisseurOctetsVersDonnees;
import fr.bruju.lcfreader.sequenceur.sequences.Enchainement;
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
	
	/** Type primitif c++ */
	public final PrimitifCpp primitif;

	/**
	 * Bloc qui est un vecteur d'un type primitif c++ qui est converti ici en int
	 * 
	 * @param champ Les caractéristiques
	 * @param nomPrimitive Le nom du type
	 */
	public BlocIntVector(Champ champ, String nomPrimitive) {
		super(champ);
		this.nomPrimitive = nomPrimitive;
		primitif = PrimitifCpp.map.get(nomPrimitive);
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
		Function<Integer, LecteurDeSequence<int[]>> secondLecteur;
		
		
		if (nomPrimitive.equals("Int32")) {
			secondLecteur = taille -> new Convertisseur(new CompteurValeurs(taille));
		} else {
			secondLecteur = taille -> new Convertisseur(new CompteurOctets(taille));
		}
		

		LecteurDeSequence<int[]> sequenceurGlobal;
		
		if (tailleLue == -1) {
			sequenceurGlobal = new Enchainement<>(new NombreBER(), secondLecteur, () -> new int[0]);
		} else {
			sequenceurGlobal = secondLecteur.apply(tailleLue);
		}
		
		return new ConvertisseurOctetsVersDonnees.ViaSequenceur<int[]>(sequenceurGlobal,
					tableau -> new Donnee<int[]>(this, tableau));
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

	
	private interface Compteur {
		default boolean notifierOctetLu() {
			return false;
		}
		
		default boolean notifierValeurLue() {
			return false;
		}
	}
	
	private class CompteurValeurs implements Compteur {
		private int quantite;

		CompteurValeurs(int quantite) {
			this.quantite = quantite;
		}
		
		public boolean notifierValeurLue() {
			return --quantite == 0;
		}
	}

	private class CompteurOctets implements Compteur {
		private int quantite;

		CompteurOctets(int quantite) {
			this.quantite = quantite;
		}
		
		public boolean notifierOctetLu() {
			return --quantite == 0;
		}
	}
	
	
	private class Convertisseur implements LecteurDeSequence<int[]> {
		private Compteur compteur;
		private LecteurDeSequence<Integer> lecteurDeValeur;
		
		private List<Integer> valeursLues;

		public Convertisseur(Compteur compteur) {
			this.compteur = compteur;
			lecteurDeValeur = primitif.getLecteur();
			valeursLues = new ArrayList<>();
		}

		@Override
		public boolean lireOctet(byte octetRecu) {
			boolean fin = false;
			
			fin |= compteur.notifierOctetLu();
			
			boolean finDeLaLectureDUnNombre = !lecteurDeValeur.lireOctet(octetRecu);
			
			if (finDeLaLectureDUnNombre) {
				valeursLues.add(lecteurDeValeur.getResultat());
				fin |= compteur.notifierValeurLue();
				lecteurDeValeur = primitif.getLecteur();
			}
			
			return !fin;
		}

		@Override
		public int[] getResultat() {
			int[] valeurs = new int[valeursLues.size()];
			
			for (int i = 0 ; i != valeurs.length ; i++) {
				valeurs[i] = valeursLues.get(i);
			}
			
			return valeurs;
		}
	}
}
