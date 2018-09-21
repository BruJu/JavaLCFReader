package fr.bruju.lcfreader.automate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.automate.DecompositionDeNom.Disposition;
import fr.bruju.lcfreader.automate.DecompositionDeNom.Type;
import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;
import fr.bruju.lcfreader.structure.Donnee;
import fr.bruju.lcfreader.structure.Structure;
import fr.bruju.lcfreader.structure.blocs.Bloc;

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
	
	static Map<String, GestionnaireDePrimitives> mapDePrimitives;
	
	static {
		mapDePrimitives = new HashMap<>();
		mapDePrimitives.put("UInt32", new GestionnaireATailleFixeNormal(4));
		mapDePrimitives.put("Int16", new GestionnaireInt16());
		
	}
	

	
	
	/* =============
	 * CONSTRUCTEURS
	 * ============= */

	/**
	 * Crée un objet à partir d'un tableau d'octets. Cet objet gère l'entièreté du tableau
	 * 
	 * @param tableau Le tableau d'octets
	 */
	public Octets(byte[] tableau, String n) {
		this.tableau = tableau;
		this.indexActuel = 0;
		this.fin = tableau.length;
		init = 0;
		afficherInfo(n);
	}

	/**
	 * Crée une sous division du tableau
	 * 
	 * @param tableau Le tableau
	 * @param debut La première case du tableau
	 * @param fin L'index de la case après la dernière case
	 */
	private Octets(byte[] tableau, int debut, int fin, String n) {
		this.tableau = tableau;
		this.indexActuel = debut;
		this.fin = fin;
		init = debut;
		
		afficherInfo(n);
	}
	
	int init;
	
	private void afficherInfo(String n) {
		//System.out.println("Sequence " + n + " " + Utilitaire.toHex(init) + " -> " + Utilitaire.toHex(fin));
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
			throw new RuntimeException("Dépassement de tableau " + Utilitaire.toHex(fin) + " (a commencé à " + Utilitaire.toHex(init));
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
	public Octets extraire(String n) {
		Octets nouvelleInstance = new Octets(tableau, indexActuel, indexActuel + dernierBERLu, n);
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
			
			Bloc<?> bloc = structure.getBloc(numeroDeBloc);
			
			if (bloc == null) {
				throw new RuntimeException("Bloc inconnu");
			}
			
			extraire("["+numeroDeBloc + "," + Utilitaire.toHex(this.dernierBERLu) + "," + bloc.nom+";" + bloc.getTypeEnString() + "-" + bloc.estUnChampIndiquantLaTaille() +"]").lireBloc1(ensembleConstruit, bloc, true);
		}
	}


	private EnsembleDeDonnees lireEnsembleSerie(String nomEnsemble, Structure structure) {
		EnsembleDeDonnees ensembleConstruit = new EnsembleDeDonnees(nomEnsemble);
		structure.getSerie().forEach(bloc -> lireBloc1(ensembleConstruit, bloc, false));
		return ensembleConstruit;
	}

	

	private void lireBloc1(EnsembleDeDonnees ensembleConstruit, Bloc<?> bloc, boolean blocUnique) {
		
		DecompositionDeNom decomposition = bloc.getDecomposition();
		
		if (bloc.estUnChampIndiquantLaTaille()) {
			decomposition = new DecompositionDeNom(Disposition.SIMPLE, Type.NOMBRE, "Int32");
		}
		lireBloc2(ensembleConstruit, decomposition, bloc, blocUnique);
	}

	private void lireBloc2(EnsembleDeDonnees ensembleConstruit, DecompositionDeNom decomposition, Bloc<?> bloc,
			boolean blocUnique) {
		switch (decomposition.disposition) {
		case SIMPLE:
			lireBlocSimple(ensembleConstruit, decomposition, bloc, blocUnique);
			break;
		case TABLEAU:
			lireBlocTableau(ensembleConstruit, decomposition, bloc);
			break;
		case VECTEUR:
			lireBlocVecteur(ensembleConstruit, decomposition, bloc, blocUnique);
			break;
		}
	}

	private void lireBlocVecteur(EnsembleDeDonnees ensembleConstruit, DecompositionDeNom decomposition, Bloc<?> bloc,
			boolean blocUnique) {
		switch (decomposition.type) {
		case CHAINE:
		case INCONNU:
			throw new RuntimeException("Vecteur de " + decomposition.type);
		case ENSEMBLEDEDONNEES:
			if (!blocUnique) {
				throw new RuntimeException("Vecteur de " + decomposition.type + " sans bloc unique");
			}
			List<EnsembleDeDonnees> ensembles = new ArrayList<>(10);
			
			while (indexActuel != fin) {
				ensembles.add(lireEnsemble(decomposition.nom));
			}
			ensembleConstruit.push(new Donnee<>(bloc, ensembles));
			break;
		case NOMBRE:
			List<Integer> nombres;
			if (decomposition.nom.equals("Int32")) {
				int nombreDeNombres = lireUnNombreEncodeEnBER();
				
				nombres = new ArrayList<>(nombreDeNombres);
				
				for (int i = 0 ; i != nombreDeNombres ; i++) {
					nombres.add(lireUnNombreEncodeEnBER());
				}
				
				
			} else {
				if (!blocUnique) {
					throw new RuntimeException("Vecteur de " + decomposition.type + " sans bloc unique");
				}
				nombres = new ArrayList<>();
				
				GestionnaireDePrimitives fonctionDeTraitement = mapDePrimitives.get(decomposition.nom);
				
				if (fonctionDeTraitement == null) {
					throw new RuntimeException("Pas de traitement pour " + decomposition.nom);
				}
				
				while (indexActuel != fin) {
					fonctionDeTraitement.consommer(nombres, avancer());
				}
				
			}
			int[] tableauReel = new int[nombres.size()];
			for (int i = 0 ; i != tableauReel.length ; i++)
				tableauReel[i] = nombres.get(i);
			
			
			ensembleConstruit.push(new Donnee<>(bloc, tableauReel));
			break;
		}
	}

	private void lireBlocTableau(EnsembleDeDonnees ensembleConstruit, DecompositionDeNom decomposition, Bloc<?> bloc) {
		int nombreDelements = this.lireUnNombreEncodeEnBER();
		
		Map<Integer, EnsembleDeDonnees> donnees = new LinkedHashMap<>(nombreDelements);
		
		for (int idElem = 0 ; idElem != nombreDelements ; idElem++) {
			int indice = lireUnNombreEncodeEnBER();
			
			if (decomposition.type != DecompositionDeNom.Type.ENSEMBLEDEDONNEES) {
				throw new RuntimeException("Cas non géré");
			}
			
			EnsembleDeDonnees ensemble = lireEnsemble(decomposition.nom);
			
			donnees.put(indice, ensemble);
		}
		
		ensembleConstruit.push(new Donnee<>(bloc, donnees));
	}

	private void lireBlocSimple(EnsembleDeDonnees ensembleConstruit, DecompositionDeNom decomposition, Bloc<?> bloc,
			boolean blocUnique) {
		switch (decomposition.type) {
		case CHAINE:
			String chaineLue = blocUnique ?
					lireChaineTailleConnue(nombreDOctetsRestants()) : lireChaineDeTailleInconnue();
			
			ensembleConstruit.push(new Donnee<String>(bloc, chaineLue));
			break;
		case ENSEMBLEDEDONNEES:
			ensembleConstruit.push(new Donnee<EnsembleDeDonnees>(bloc, lireEnsemble(decomposition.nom)));
			break;
		case NOMBRE:
			Integer nombre;
			if (decomposition.nom.equals("Int32")) {
				nombre = lireUnNombreEncodeEnBER();
			} else {
				List<Integer> nombres = new ArrayList<>();
				
				GestionnaireDePrimitives fonctionDeTraitement = mapDePrimitives.get(decomposition.nom);
				while (nombres.size() != 1) {
					fonctionDeTraitement.consommer(nombres, avancer());
				}
				nombre = nombres.get(0);
			}
			ensembleConstruit.push(new Donnee<Integer>(bloc, nombre));
			break;
		case INCONNU:
			ensembleConstruit.push(new Donnee<byte[]>(bloc.inconnu(), enTableau()));
			break;
		}
	}
	


	private byte[] enTableau() {
		byte[] tableau = Arrays.copyOfRange(this.tableau, indexActuel, fin);
		indexActuel = fin;
		return tableau;
	}

	private int nombreDOctetsRestants() {
		return this.fin - this.indexActuel;
	}


}
