package fr.bruju.lcfreader.structure.blocs;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.XMLInsecticide;

/**
 * Un bloc de données concernant une chaîne
 * 
 * @author Bruju
 *
 */
public class BlocString extends Bloc<String> {
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */
	
	/** Valeur par défaut */
	private String defaut;
	
	/**
	 * Construit le bloc contenant une chaîne avec la valeur par défaut donnée
	 * @param defaut La valeur par défaut de la chaîne
	 */
	public BlocString(Champ champ, String defaut) {
		super(champ);
		this.defaut = defaut.equals("") ? null : defaut;
	}
	

	/* ====================
	 * PROPRIETES D'UN BLOC
	 * ==================== */
	
	@Override
	public String getNomType() {
		return "String(" + defaut + ")";
	}

	@Override
	public String valeurParDefaut() {
		return defaut;
	}

	
	/* =====================
	 * CONSTRUIRE UNE VALEUR
	 * ===================== */

	@Override
	public String extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		// XMLInsecticide.balise("CHAINE_" + this.nom);
		
		if (tailleLue == -1) {
			tailleLue = desequenceur.$lireUnNombreBER();
			// XMLInsecticide.xml( " ==== ");
		}
		
		String chaine = desequenceur.$lireUneChaine(tailleLue);
		
		// XMLInsecticide.fermer();
		return chaine;
	}
}