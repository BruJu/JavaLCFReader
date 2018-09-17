package fr.bruju.lcfreader.structure.blocs;

import java.util.HashMap;
import java.util.Map;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;
import fr.bruju.lcfreader.structure.Structure;
import fr.bruju.lcfreader.structure.types.PrimitifCpp;

/**
 * Classe permettant d'instancier un bloc selon les données
 * 
 * @author Bruju
 *
 */
public class Blocs {
	/* =============
	 * CONFIGURATION
	 * ============= */
	/**
	 * Fonction appelée par le constructeur qui définit comment instancier les blocs correspondant à des types de base.
	 */
	private Map<String, Procede> getTypesDeBase() {
		Map<String, Procede> carteDesTypesDeBase = new HashMap<>();

		carteDesTypesDeBase.put("Int32", (c, t, d) -> new BlocInt32(c, d));
		carteDesTypesDeBase.put("String", (c, t, d) -> new BlocString(c, d));

		return carteDesTypesDeBase;
	}

	/** Liste des procédés connus */
	private Procede[] procedes = new Procede[] {
			new ProcedeArray(),
			new ProcedeEnsembleDeDonnees(),
			new ProcedeVector(),
			new ProcedeTypeDeBase()
		};

	/* ==============
	 * FONCTIONNEMENT
	 * ============== */

	/**
	 * Décrypte les paramètres du tableau de données et renvoie le bloc associé
	 * 
	 * @param donnees Les enregistrements du fichier fields.csv
	 * @return Le bloc permettant de traiter les données décrites par cet enregistrement du fichier fields.csv
	 */
	private Bloc<?> instancierBloc(String[] donnees) {
		String nom = donnees[1];
		boolean sized = donnees[2].equals("t");
		String type = donnees[3];

		int index = 0;

		if (!donnees[4].equals(""))
			index = Integer.decode(donnees[4]);

		Champ champ = new Champ(index, nom, sized);

		Bloc<?> bloc;
		if (sized) {
			bloc = new BlocInt32(champ, donnees[5]);
		} else {
			bloc = getInstance().getBloc(champ, type, donnees[5]);
		}

		return bloc;
	}

	/**
	 * Explore tous les procédés pour tenter d'instancier le bloc. Si il n'y arrive pas, renvoie un BlocInconnu
	 * 
	 * @param champ Les caractéristiques du champ
	 * @param type Le type
	 * @param defaut La valeur par défaut
	 * @return Un bloc correspondant à la valeur
	 */
	private Bloc<?> getBloc(Champ champ, String type, String defaut) {
		Bloc<?> bloc;

		for (Procede procede : procedes) {
			bloc = procede.generer(champ, type, defaut);
			if (bloc != null) {
				return bloc;
			}
		}

		return new BlocInconnu(champ, type);
	}

	/* ================================
	 * PROCEDES DE DECRYPTAGE D'UN BLOC
	 * ================================ */

	/**
	 * Un procédé est une fonction permettant de générer éventuellement un bloc avec son champ, son type et sa valeur
	 * par défaut en string
	 */
	private interface Procede {
		/**
		 * Génère éventuellement le bloc si ce procédé peut l'instancier
		 * 
		 * @param champ Les informations liées au champ
		 * @param type Le nom du type du bloc
		 * @param defaut La valeur par défaut (null si aucune)
		 * @return null si ce procédé ne peut pas instancier le bloc. Le bloc si il le peut
		 */
		Bloc<?> generer(Champ champ, String type, String defaut);
	}

	/**
	 * Génère des blocs correspondant aux types de base
	 */
	private class ProcedeTypeDeBase implements Procede {
		/** Carte associant nom d'un type de base et comment insancier le bloc correspondant */
		private Map<String, Procede> carteDesTypesDeBase = getTypesDeBase();

		@Override
		public Bloc<?> generer(Champ champ, String type, String defaut) {
			String typeAConsiderer = type.startsWith("Enum<") ? "Int32" : type;
			Procede procede = carteDesTypesDeBase.get(typeAConsiderer);
			return Utilitaire.appel(procede, p -> p.generer(champ, typeAConsiderer, defaut));
		}
	}

	/**
	 * Crée un bloc "tableau" si le type est Array<TypeDansLaBase>
	 */
	private class ProcedeArray implements Procede {
		@Override
		public Bloc<?> generer(Champ champ, String type, String defaut) {
			if (!type.startsWith("Array<") || !type.endsWith(">"))
				return null;

			String vraiType = type.substring(6, type.length() - 1); // Array<X>
			Structure structure = BaseDeDonneesDesStructures.getInstance().get(vraiType);
			return structure == null ? null : new BlocArray(champ, vraiType);
		}
	}

	/**
	 * Crée un bloc qui décrypte les vecteurs
	 */
	private class ProcedeVector implements Procede {
		@Override
		public Bloc<?> generer(Champ champ, String type, String defaut) {
			if (!type.startsWith("Vector<") || !type.endsWith(">"))
				return null;

			// Extraction de la chaîne entre Vector< et >
			String sousType = type.substring(7, type.length() - 1);

			if (PrimitifCpp.map.get(sousType) != null) {
				return new BlocIntVector(champ, sousType);
			}

			if (BaseDeDonneesDesStructures.getInstance().get(sousType) != null) {
				return new BlocEnsembleVector(champ, sousType);
			}

			return null;
		}
	}

	/** Instancie un bloc pour ensemble de données si une structure permettant de le décrypter est connue */
	private class ProcedeEnsembleDeDonnees implements Procede {
		@Override
		public Bloc<?> generer(Champ champ, String type, String defaut) {
			return BaseDeDonneesDesStructures.getInstance().get(type) == null ? null
					: new BlocEnsembleDeDonnees(champ, type);
		}
	}

	/* =========
	 * SINGLETON
	 * ========= */

	/** Instance de blocs */
	private static Blocs instance;

	/** On ne peut pas instancier Blocs de l'extérieur */
	private Blocs() {
	}

	/**
	 * Donne l'instance de Blocs
	 * 
	 * @return L'instance de Blocs
	 */
	public static Blocs getInstance() {
		if (null == instance) {
			instance = new Blocs();
		}
		return instance;
	}

	/**
	 * Décrypte les paramètres du tableau de données et renvoie le bloc associé
	 * 
	 * @param donnees Les enregistrements du fichier fields.csv
	 * @return Le bloc permettant de traiter les données décrites par cet enregistrement du fichier fields.csv
	 */
	public static Bloc<?> instancier(String[] donnees) {
		return getInstance().instancierBloc(donnees);
	}
}
