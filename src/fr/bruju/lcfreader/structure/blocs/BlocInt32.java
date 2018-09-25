package fr.bruju.lcfreader.structure.blocs;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.XMLInsecticide;

/**
 * Un bloc de données concernant un int32
 * 
 * @author Bruju
 *
 */
public class BlocInt32 extends Bloc<Integer> {
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */

	/** Valeur par défaut */
	private Integer defaut;

	/**
	 * Construit le bloc contenant un entier avec la valeur par défaut donnée
	 * 
	 * @param defaut La valeur par défaut. Si de la forme a|b, prend b.
	 */
	public BlocInt32(Champ champ, String defaut) {
		super(champ);
		if (!defaut.equals("")) {
			// On prend la valeur par défaut pour RPG Maker 2003
			if (defaut.contains("|")) {
				defaut = defaut.split("|")[1];
			}

			this.defaut = Integer.parseInt(defaut);
		}
	}

	/* ====================
	 * PROPRIETES D'UN BLOC
	 * ==================== */

	@Override
	public String getNomType() {
		return "Integer(" + defaut + ")";
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
		// XMLInsecticide.balise("Nombre_" + this.nom);
		Integer valeur = desequenceur.$lireUnNombreBER();
		// XMLInsecticide.fermer();
		return valeur;
	}
}