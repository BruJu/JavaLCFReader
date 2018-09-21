package fr.bruju.lcfreader.automate;

import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;
import fr.bruju.lcfreader.structure.types.PrimitifCpp;

/**
 * 
 * <br><br>
 * Cette classe a été developpée avec pour objectif de n'instancier aucune classe. En effet, déchiffrer des type est
 * une opération qui est produite très souvent.
 * 
 * @author Bruju
 *
 */
class DecompositionDeNom {
	public final Disposition disposition;
	public final Type type;
	public final String nom;
	
	public DecompositionDeNom(Disposition disposition, Type type, String nom) {
		this.disposition = disposition;
		this.type = type;
		this.nom = nom;
	}

	enum Disposition {
		SIMPLE,
		VECTEUR,
		TABLEAU
	}
	
	 enum Type {
		ENSEMBLEDEDONNEES,
		NOMBRE,
		CHAINE,
		INCONNU
	}
	
	
	static DecompositionDeNom maj(String nomComplet) {
		if (nomComplet.endsWith(">")) {
			if (nomComplet.startsWith("Enum<")) {
				return lireType(Disposition.SIMPLE, nomComplet);
			} else if (nomComplet.startsWith("Ref<")) {
				return lireType(Disposition.SIMPLE, nomComplet);
			} else if (nomComplet.startsWith("Vector<")){
				return lireType(Disposition.VECTEUR, nomComplet.substring(7, nomComplet.length() - 1));
			} else if (nomComplet.startsWith("Array<")) {
				return lireType(Disposition.TABLEAU, nomComplet.substring(6, nomComplet.length() - 1));
			} else {
				throw new RuntimeException("Type inconnu : " + nomComplet);
			}
		} else {
			return lireType(Disposition.SIMPLE, nomComplet);
		}
	}


	private static DecompositionDeNom lireType(Disposition disposition, String nomComplet) {
		if (nomComplet.startsWith("Ref<"))
			nomComplet = "Int32";
		
		nomComplet = interpreterEnum(nomComplet);
		
		Type type;
		
		if ("String".equals(nomComplet)) {
			type = Type.CHAINE;
		} else if (BaseDeDonneesDesStructures.getInstance().get(nomComplet) != null) {
			type = Type.ENSEMBLEDEDONNEES;
		} else if (PrimitifCpp.map.get(nomComplet) != null) {
			type = Type.NOMBRE;
		} else {
			type = Type.INCONNU;
		}
		
		return new DecompositionDeNom(disposition, type, nomComplet);
	}


	private static String interpreterEnum(String nomComplet) {
		if (!nomComplet.endsWith(">")) {
			return nomComplet;
		} else {
			// Par construction, on a affaire à un Enum<Chaine>
			String typeEnum = nomComplet.substring(5, nomComplet.length() - 1);
			String[] decomposition = typeEnum.split("\\:");
			return decomposition.length == 2 ? decomposition[1] : "Int32";
		}
	}
	
	
}
