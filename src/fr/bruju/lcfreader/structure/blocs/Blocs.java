package fr.bruju.lcfreader.structure.blocs;

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
	// TODO : Faire une vrai factory
	
	public static Bloc<?> instancier(String[] donnees) {
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
			bloc = genererBloc(champ, type, donnees[5]);
		}

		
		
		return bloc;
	}
	
	static Bloc<?> genererBloc(Champ champ, String type, String defaut) {
		Bloc<?> bloc;
		
		bloc = essayerArray(champ, type);
		if (bloc != null) return bloc;
		
		if (BaseDeDonneesDesStructures.getInstance().get(type) != null) {
			return new BlocEnsembleDeDonnees(champ, type);
		}
		
		
		bloc = essayerVector(champ, type);
		if (bloc != null) return bloc;
		
		
		
		if (type.startsWith("Enum<")) {
			type = "Int32";
		}
		
		switch (type) {
		case "String":
			return new BlocString(champ, defaut);
		
		
		case "Int32":
			return new BlocInt32(champ, defaut);
			
		default:
			return new BlocInconnu(champ, type);
		}
	}

	/**
	 * Crée un bloc "tableau" si le type est Array<TypeDansLaBase>
	 * @param type Le type
	 * @return Un bloc tableau si c'est pertinent
	 */
	public static Bloc<?> essayerArray(Champ champ, String type) {
		if (!type.startsWith("Array<") || !type.endsWith(">"))
			return null;
		
		String vraiType = type.substring(6, type.length() - 1); // Array<X>
		Structure structure = BaseDeDonneesDesStructures.getInstance().get(vraiType);
		return structure == null ? null : new BlocArray(champ, vraiType);
	}


	public static Bloc<?> essayerVector(Champ champ, String type) {
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

	/** Cette classe est une classe utilitaire */
	private Blocs() { }
}
