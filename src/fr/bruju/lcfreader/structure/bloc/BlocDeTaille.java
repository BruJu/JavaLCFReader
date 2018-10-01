package fr.bruju.lcfreader.structure.bloc;

import fr.bruju.lcfreader.structure.modele.Desequenceur;
import fr.bruju.lcfreader.structure.modele.XMLInsecticide;

/**
 * Un bloc de données concernant un int32
 * 
 * @author Bruju
 *
 */
public class BlocDeTaille extends Bloc<Integer> {
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
	public BlocDeTaille(int index, String nom, String defaut) {
		super(index, nom, "Taille");
		if (!defaut.equals("")) {
			this.defaut = Integer.parseInt(defaut);
		}
	}

	
	
	/* ====================
	 * PROPRIETES D'UN BLOC
	 * ==================== */

	@Override
	public boolean estUnChampIndiquantLaTaille() {
		return true;
	}



	@Override
	public String getNomType() {
		return "Taille(" + defaut + ")";
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
		XMLInsecticide.balise(typeComplet);
		Integer valeur = desequenceur.$lireUnNombreBER();
		XMLInsecticide.fermer();
		return valeur;
	}

	@Override
	public Integer convertirDefaut(String defaut) {
		return Integer.parseInt(defaut);
	}
}