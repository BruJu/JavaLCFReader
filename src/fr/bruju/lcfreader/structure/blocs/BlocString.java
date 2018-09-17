package fr.bruju.lcfreader.structure.blocs;

import fr.bruju.lcfreader.sequenceur.sequences.Chaine;
import fr.bruju.lcfreader.sequenceur.sequences.ConvertisseurOctetsVersDonnees;
import fr.bruju.lcfreader.sequenceur.sequences.LecteurDeSequence;
import fr.bruju.lcfreader.sequenceur.sequences.TailleChaine;
import fr.bruju.lcfreader.structure.Donnee;

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
	public ConvertisseurOctetsVersDonnees<String> getHandler(int tailleLue) {
		LecteurDeSequence<String> base = (tailleLue == -1) ? new TailleChaine() : new Chaine(tailleLue);
		return new ConvertisseurOctetsVersDonnees.ViaSequenceur<>(base, chaine -> new Donnee<>(this, chaine));
	}
}