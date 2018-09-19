package fr.bruju.lcfreader.structure.blocs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.sequenceur.sequences.ConvertisseurOctetsVersDonnees;
import fr.bruju.lcfreader.sequenceur.sequences.Enchainement;
import fr.bruju.lcfreader.sequenceur.sequences.LecteurDeSequence;
import fr.bruju.lcfreader.sequenceur.sequences.NombreBER;
import fr.bruju.lcfreader.structure.Donnee;
import fr.bruju.lcfreader.structure.types.PrimitifCpp;

/**
 * Un bloc qui correspond à un vecteur de nombres
 * 
 * @author Bruju
 *
 */
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
		// Comment décrypter les valeurs une fois la taille connue
		Function<Integer, LecteurDeSequence<List<Integer>>> secondLecteur;
		
		if (nomPrimitive.equals("Int32")) {
			secondLecteur = taille -> new Convertisseur(new CompteurValeurs(taille));
		} else {
			secondLecteur = taille -> new Convertisseur(new CompteurOctets(taille));
		}
		
		// Si la taille est inconnue, il faut appliquer une phase de lecture de la taille
		LecteurDeSequence<List<Integer>> sequenceurGlobal;
		
		if (tailleLue == -1) {
			Function<Integer, LecteurDeSequence<List<Integer>>> alternatif = taille -> taille == 0 ? null :
				secondLecteur.apply(taille);
			
			sequenceurGlobal = new Enchainement<>(new NombreBER(), alternatif
					, () -> new ArrayList<>());
		} else {
			sequenceurGlobal = secondLecteur.apply(tailleLue);
		}
		
		// Créer le convertisseur
		return new ConvertisseurOctetsVersDonnees.ViaSequenceur<>(sequenceurGlobal, this::sApproprierListeResultat);
	}
	
	/**
	 * Converti une liste d'Integer en un tableau d'int qui est une donnée fournie par ce bloc
	 * @param valeursLues La liste d'integer
	 * @return Une donnée correspondant à la liste d'integer sous forme de tableau, et rattaché à ce bloc
	 */
	private Donnee<int[]> sApproprierListeResultat(List<Integer> valeursLues) {
		int[] valeurs = new int[valeursLues.size()];
		
		for (int i = 0 ; i != valeurs.length ; i++) {
			valeurs[i] = valeursLues.get(i);
		}
		
		return new Donnee<>(this, valeurs);
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

	/** Un compteur est une classe qui renvoie faux quand quantite passe à 0 lors d'une notification */
	private abstract class Compteur {
		/** Valeur du compteur */
		protected int quantite;
		
		/**
		 * Crée un compteur
		 * @param quantite La valeur initiale
		 */
		private Compteur(int quantite) {
			this.quantite = quantite;
		}

		/**
		 * Notifie la lecture d'un octet
		 * @return Vrai si cette opération fait passer quantite à 0
		 */
		protected boolean notifierOctetLu() {
			return false;
		}
		
		/**
		 * Notifie la lecture d'une valeur
		 * @return Vrai si cette opération fait passer quantite à 0
		 */
		protected boolean notifierValeurLue() {
			return false;
		}
	}
	
	/** Compteur qui se décrémente quand on lui notifie avoir lu une valeur */
	private class CompteurValeurs extends Compteur {
		/**
		 * Construit le compteur
		 * @param quantite Le nombre de valeurs à déchiffrer
		 */
		private CompteurValeurs(int quantite) {
			super(quantite);
		}

		@Override
		protected boolean notifierValeurLue() {
			return --quantite == 0;
		}
	}

	/** Compteur qui se décrémente quand on lui notifie la lecture d'un octet */
	private class CompteurOctets extends Compteur {
		/**
		 * Construit le compteur
		 * @param quantite Le nombre d'octets à lire
		 */
		private CompteurOctets(int quantite) {
			super(quantite);
		}

		@Override
		protected boolean notifierOctetLu() {
			return --quantite == 0;
		}
	}
	
	/**
	 * Un convertisseur qui lit des octets et déchiffre les valeurs au moyen d'un lecteur de valeur fourni par le
	 * type primitif du bloc et en respectant un compteur.
	 * <br>Dit autrement, le but est de lire des octets jusqu'à expiration des données attribuées à ce bloc. Ces
	 * octets sont convertis en nombre selon l'algorithme fourni par le type primitif associé.
	 *
	 */
	private class Convertisseur implements LecteurDeSequence<List<Integer>> {
		/** Compteur donnant la quantité d'informations à lire */
		private Compteur compteur;
		/** Algorithme de lecture de valeurs */
		private LecteurDeSequence<Integer> lecteurDeValeur;
		
		/** Entiers déjà lus */
		private List<Integer> valeursLues;

		/**
		 * Crée un convertisseur octets vers vecteur de nombres
		 * @param compteur Le compteur indiquant le nombre de données à recevoir
		 */
		private Convertisseur(Compteur compteur) {
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
		public List<Integer> getResultat() {
			return valeursLues;
		}
	}
}
