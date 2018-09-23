package fr.bruju.lcfreader.structure.blocs;

import fr.bruju.lcfreader.sequenceur.lecteurs.Desequenceur;
import fr.bruju.lcfreader.structure.Donnee;
import fr.bruju.lcfreader.structure.types.PrimitifCpp;

/**
 * Un bloc de données concernant un type primitif c++
 * 
 * @author Bruju
 *
 */
public class BlocPrimitifCpp extends Bloc<Integer> {
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */
	
	/** Nom du type primitif C++ */
	public final String nomPrimitive;
	
	/** Type primitif c++ */
	public final PrimitifCpp primitif;
	
	/** Valeur par défaut */
	private Integer defaut;

	/**
	 * Construit le bloc contenant un entier avec la valeur par défaut donnée
	 * 
	 * @param defaut La valeur par défaut. Si de la forme a|b, prend b.
	 */
	public BlocPrimitifCpp(Champ champ, String nomDuType, String defaut) {
		super(champ);
		
		this.nomPrimitive = nomDuType;
		primitif = PrimitifCpp.map.get(nomDuType);
		
		if (defaut.equals("False")) {
			defaut = "1";
		} else if (defaut.endsWith("True")) {
			defaut = "0";
		} else if (defaut.contains("|")){
			defaut = defaut.split("|")[1];
		}
		
		this.defaut = defaut == null ? null : Integer.parseInt(defaut);
	}

	/* ====================
	 * PROPRIETES D'UN BLOC
	 * ==================== */

	@Override
	public String getNomType() {
		return "Primitif<"+nomPrimitive+">(" + defaut + ")";
	}

	@Override
	public Integer valeurParDefaut() {
		return defaut;
	}

	/* =====================
	 * CONSTRUIRE UNE VALEUR
	 * ===================== */


	@Override
	public Integer extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		return primitif.lireOctet(desequenceur, 0);
	}
}