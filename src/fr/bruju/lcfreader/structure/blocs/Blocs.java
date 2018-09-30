package fr.bruju.lcfreader.structure.blocs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.structure.Structures;
import fr.bruju.lcfreader.structure.dispositions.Disposition;
import fr.bruju.lcfreader.structure.Sequenceur;
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

		carteDesTypesDeBase.put("Int32", (c, t, d, z) -> new BlocInt32(c, d));
		carteDesTypesDeBase.put("String", (c, t, d, z) -> new BlocString(c, d));

		return carteDesTypesDeBase;
	}

	/** Liste des procédés connus */
	private Procede[] procedes = new Procede[] {
			new ProcedeArray(),
			new ProcedeEnsembleDeDonnees(),
			new ProcedeVector(),
			new ProcedePrimitif(),
			new ProcedeTypeDeBase()
		};

	/* ==============
	 * FONCTIONNEMENT
	 * ============== */

	private Bloc<?> nouvelleImplementation(String[] donnees) {
		String nom = donnees[2];
		boolean sized = donnees[3].equals("SizeField");
		String type = donnees[3];
		String disposition = donnees[4];

		int index = 0;

		if (!donnees[5].equals(""))
			index = Integer.decode(donnees[5]);
		
		Champ champ = new Champ(index, nom, sized, donnees[3] + "_" + donnees[4]);
		
		if (sized) {
			return new BlocInt32(champ, donnees[6]);
		}
		
		Disposition dispo = Disposition.get(disposition, type);
		MiniBloc<?> sequenceur = getSequenceur(nom, type);
		
		
		
		return dispo.decorer(champ, sequenceur);
	}
	
	private MiniBloc<?> getSequenceur(String nom, String type) {
		
		switch (type) {
		case "Int32":
			return (o, s) -> o.$lireUnNombreBER();
		case "String":
			return (o, s) -> o.$lireUneChaine(s);
		}
		
		if (PrimitifCpp.map.containsKey(type)) {
			return (o, s) -> PrimitifCpp.map.get(type).lireOctet(o, s);
		}
		
		if (Structures.getInstance().get(type) != null) {
			return new MiniBloc<EnsembleDeDonnees>() {

				@Override
				public EnsembleDeDonnees extraireDonnee(Desequenceur o, int s) {
					return Structures.getInstance().get(type).lireOctet(o, s);
				}

				@Override
				public String convertirEnChaineUneValeur(EnsembleDeDonnees valeur) {
					return valeur.getRepresentationEnLigne();
				}
			};
		}
		
		System.out.println("Pas de séquenceur pour " + type + " " + nom);
		
		return new MiniBloc<byte[]>() {

			@Override
			public byte[] extraireDonnee(Desequenceur desequenceur, int parametre) {
				
				byte[] tableau = new byte[desequenceur.octetsRestants()];
				
				for (int i = 0 ; i != tableau.length ; i++) {
					tableau[i] = desequenceur.suivant();
				}
				
				return tableau;
			}
			
			
			
		};
	}

	/**
	 * Décrypte les paramètres du tableau de données et renvoie le bloc associé
	 * 
	 * @param donnees Les enregistrements du fichier fields.csv
	 * @return Le bloc permettant de traiter les données décrites par cet enregistrement du fichier fields.csv
	 */
	private Bloc<?> instancierBloc(String[] donnees) {
		if (true)
			return nouvelleImplementation(donnees);
			
		String nom = donnees[2];
		boolean sized = donnees[3].equals("SizeField");
		String type = donnees[3];
		String disposition = donnees[4];
		int idDispo = 0;
		
		if (disposition.equals("Array")) {
			idDispo = 1;
		}
		
		if (disposition.equals("Vector")) {
			idDispo = 2;
		}
		
		
		int index = 0;

		if (!donnees[5].equals(""))
			index = Integer.decode(donnees[5]);
		

		

		Champ champ = new Champ(index, nom, sized, donnees[3] + "_" + donnees[4]);

		if (type.equals("MoveRoute")) {
			return new BlocInconnu(champ, "MoveRoute");
		}
		
		if (nom.equals("battle_commands")) {
			return new BlocTableauCInt32(champ);
		}
		
		Bloc<?> bloc;
		if (sized) {
			bloc = new BlocInt32(champ, donnees[6]);
		} else {
			bloc = getInstance().getBloc(champ, type, donnees[6], idDispo);
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
	private Bloc<?> getBloc(Champ champ, String type, String defaut, int idDispo) {
		Bloc<?> bloc;

		for (Procede procede : procedes) {
			bloc = procede.generer(champ, type, defaut, idDispo);
			if (bloc != null) {
				return bloc;
			}
		}

		return new BlocInconnu(champ, type);
	}

	private String enleverRef(String type) {
		return type;
		
		/*
		if (!(type.startsWith("Ref<") && type.endsWith(">"))) {
			return type;
		} else {
			// Enlever Ref
			type = type.substring(4, type.length() - 1);
			
			// Pour les Ref<A:B>, renvoyer B, pour les Ref<A> renvoyer Int32
			String[] split = type.split("\\:");
			if (split.length == 1) {
				return "Int32";
			} else {
				return split[1];
			}
		}
		*/
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
		Bloc<?> generer(Champ champ, String type, String defaut, int idDispo);
	}

	/**
	 * Génère des blocs correspondant aux types de base
	 */
	private class ProcedeTypeDeBase implements Procede {
		/** Carte associant nom d'un type de base et comment insancier le bloc correspondant */
		private Map<String, Procede> carteDesTypesDeBase = getTypesDeBase();

		@Override
		public Bloc<?> generer(Champ champ, String type, String defaut, int idDispo) {
			String typeAConsiderer = type; //type.startsWith("Enum<") ? "Int32" : type;
			Procede procede = carteDesTypesDeBase.get(typeAConsiderer);
			return Utilitaire.appel(procede, p -> p.generer(champ, typeAConsiderer, defaut, idDispo));
		}
	}

	/**
	 * Crée un bloc "tableau" si le type est Array<TypeDansLaBase>
	 */
	private class ProcedeArray implements Procede {
		@Override
		public Bloc<?> generer(Champ champ, String type, String defaut, int idDispo) {
			if (idDispo != 1)
				return null;
			
			String vraiType = type;
			Structure structure = Structures.getInstance().get(vraiType);
			return structure == null ? null : new BlocArray(champ, structure);
		}
	}

	/**
	 * Crée un bloc qui décrypte les vecteurs
	 */
	private class ProcedeVector implements Procede {
		@Override
		public Bloc<?> generer(Champ champ, String type, String defaut, int idDispo) {
			if (idDispo != 2)
				return null;

			// Extraction de la chaîne entre Vector< et >
			String sousType = type;

			if (PrimitifCpp.map.get(sousType) != null) {
				return new BlocIntVector(champ, sousType);
			}

			Structure structure = Structures.getInstance().get(sousType);
			
			if (structure != null) {
				return new BlocEnsembleVector(champ, structure);
			}

			return null;
		}
	}

	/** Instancie un bloc pour ensemble de données si une structure permettant de le décrypter est connue */
	private class ProcedeEnsembleDeDonnees implements Procede {
		@Override
		public Bloc<?> generer(Champ champ, String type, String defaut, int idDispo) {
			if (idDispo != 0)
				return null;
			
			Structure structure = Structures.getInstance().get(type);
			return structure == null ? null : new BlocEnsembleDeDonnees(champ, structure);
		}
	}
	
	/**
	 * Crée un bloc qui décrypte les valeurs primitives
	 */
	private class ProcedePrimitif implements Procede {
		@Override
		public Bloc<?> generer(Champ champ, String type, String defaut, int idDispo) {
			if (idDispo != 0)
				return null;
			
			if (PrimitifCpp.map.get(type) != null) {
				return new BlocPrimitifCpp(champ, type, defaut);
			} else {
				return null;
			}
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
