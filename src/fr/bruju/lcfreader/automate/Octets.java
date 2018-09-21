package fr.bruju.lcfreader.automate;

import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;
import fr.bruju.lcfreader.structure.Structure;

/**
 * Classe lisant un tableau octets par octets et offrant des services vis à vis de la lecture de fichiers encodés selon
 * l'encodage BER. <br>
 * Cette classe n'offre aucune garantie sur le dépassement de tableau ou non si le fichier est incorrect, ou si la
 * méthode avancer() est utilisée au delà de l'espace dédié.
 * 
 * @author Bruju
 *
 */
public class Octets {
	/* =========
	 * ATTRIBUTS
	 * ========= */

	// Interface d'un tableau

	/** Tableau d'octets composant le fichier */
	private byte[] tableau;
	/** Case du tableau actuellement lue */
	private int indexActuel;
	/** Case de fin du tableau */
	private int fin;

	// Lecture de fichiers BER

	/** Résultat du dernier appel à lireUnNombreEncodeEnBER(). Typiquement une taille */
	private int dernierBERLu;

	/* =============
	 * CONSTRUCTEURS
	 * ============= */

	/**
	 * Crée un objet à partir d'un tableau d'octets. Cet objet gère l'entièreté du tableau
	 * 
	 * @param tableau Le tableau d'octets
	 */
	public Octets(byte[] tableau) {
		this.tableau = tableau;
		this.indexActuel = 0;
		this.fin = tableau.length;
	}

	/**
	 * Crée une sous division du tableau
	 * 
	 * @param tableau Le tableau
	 * @param debut La première case du tableau
	 * @param fin L'index de la case après la dernière case
	 */
	private Octets(byte[] tableau, int debut, int fin) {
		this.tableau = tableau;
		this.indexActuel = debut;
		this.fin = fin;
	}

	/* ==================================
	 * MANIPULATION DE LA TETE DE LECTURE
	 * ================================== */

	/**
	 * Lit et retourne l'octet pointé et avance d'une case. <br>
	 * Aucune garantie sur le non dépassement du tableau
	 * 
	 * @return L'octet qui était pointé
	 */
	public byte avancer() {
		if (indexActuel >= fin) {
			throw new RuntimeException("Dépassement de tableau");
		}
		
		return tableau[indexActuel++];
	}

	/**
	 * Avance la tête de lecture d'une quantité égale au résultat du dernier nombre encodé en BER lu.
	 */
	public void ignorer() {
		indexActuel += dernierBERLu;
	}

	/**
	 * Extrait une nouvelle instance de la classe qui gère un nombre d'octets égal à au dernier nombre lu par la
	 * fonction de lecture d'un nombre BER. Les octets gérés par la sous instance sont ignorés par cette instance.
	 * 
	 * @return Une sous instance gérant les octets décrits precedemment.
	 */
	public Octets extraire() {
		Octets nouvelleInstance = new Octets(tableau, indexActuel, indexActuel + dernierBERLu);
		ignorer();
		return nouvelleInstance;
	}

	/* ========================================
	 * MANIPULATION DE FICHIERS ENCODES EN BER
	 * ======================================= */

	/**
	 * Lit le type du prochain bloc ainsi que sa taille. Renvoie null si le tableau est fini ou si le champ à lire est
	 * le numéro 0. Lance une lecture de nombre encodé en ber pour la taille pour pouvoir extraire() ou ignorer() le
	 * bloc facilement.
	 * 
	 * @return null si on est en fin de tableau. Le numéro du prochain bloc sinon. Lance une lecture de nombre encodé en
	 *         ber pour la taille pour pouvoir extraire() ou ignorer() le bloc facilement.
	 */
	public Integer lireTypeEtTailleBloc() {
		if (indexActuel == fin) {
			return null;
		} else {
			Integer retour = (int) avancer();

			if (retour == 0) {
				return null;
			}

			lireUnNombreEncodeEnBER();
			return retour;
		}
	}

	/**
	 * Extrait une chaîne de la taille du nombre pointé actuellement. Utilise lireUnNombreEncodeEnBER().
	 * 
	 * @return La chaîne qui était pointée.
	 */
	public String lireChaineDeTailleInconnue() {
		int taille = lireUnNombreEncodeEnBER();
		return lireChaineTailleConnue(taille);
	}

	/**
	 * Extrait une chaîne de la taille donnée
	 * 
	 * @param taille La taille de la chaîne
	 * @return La chaîne lue
	 */
	private String lireChaineTailleConnue(int taille) {
		char[] caracteres = new char[taille];

		for (int i = 0; i != taille; i++) {
			caracteres[i] = (char) avancer();
		}

		return String.valueOf(caracteres);
	}

	/**
	 * Lit autant d'octets qu'il faut pour reconstitué le nombre encodé selon le principe BER.
	 * <pre><code>
	 * int valeur = 0;
	 *		
	 * int octetRecu;
	 * do {
	 *   octetRecu = avancer();
	 *   valeur = (valeur * 0x80) + (octetRecu & 0x7F);
	 * } while ((octetRecu & 0x80) != 0);
	 *		
	 * dernierBERLu = valeur;
	 *		
	 * return valeur;
	 * </code></pre>
	 * 
	 * @return Le nombre encodé en BER lu, sachant que le champ dernierBERLu est également mis à jour
	 */
	public int lireUnNombreEncodeEnBER() {
		int valeur = 0;

		int octetRecu;
		do {
			octetRecu = avancer();
			valeur = (valeur * 0x80) + (octetRecu & 0x7F);
		} while ((octetRecu & 0x80) != 0);

		dernierBERLu = valeur;

		return valeur;
	}

	public EnsembleDeDonnees lireEnsemble(String nomEnsemble) {
		BaseDeDonneesDesStructures instance = BaseDeDonneesDesStructures.getInstance();
		
		Structure structure = instance.get(nomEnsemble);
		
		if (structure == null) {
			throw new RuntimeException("Structure " + nomEnsemble + " inconnue");
		} else if (structure.estSerie()) {
			return lireEnsembleSerie(nomEnsemble, structure);
		} else {
			return lireEnsembleDiscontinu(nomEnsemble, structure);
		}
	}

	private EnsembleDeDonnees lireEnsembleDiscontinu(String nomEnsemble, Structure structure) {
		EnsembleDeDonnees ensembleConstruit = new EnsembleDeDonnees(nomEnsemble);
		
		Integer numeroDeBloc;
		
		while (true) {
			numeroDeBloc = this.lireTypeEtTailleBloc();
			if (numeroDeBloc == null) {
				return ensembleConstruit;
			}
			
			ensembleConstruit.push(structure.bloquer(extraire(), numeroDeBloc));
		}
	}

	private EnsembleDeDonnees lireEnsembleSerie(String nomEnsemble, Structure structure) {
		EnsembleDeDonnees ensembleConstruit = new EnsembleDeDonnees(nomEnsemble);
		structure.getSerie().forEach(bloc -> ensembleConstruit.push(bloc.bloquerSansTaille(this)));
		return ensembleConstruit;
	}

}
