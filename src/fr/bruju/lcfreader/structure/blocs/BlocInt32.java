package fr.bruju.lcfreader.structure.blocs;

import fr.bruju.lcfreader.sequenceur.lecteurs.Desequenceur;
import fr.bruju.lcfreader.sequenceur.sequences.ConvertisseurOctetsVersDonnees;
import fr.bruju.lcfreader.sequenceur.sequences.NombreBER;
import fr.bruju.lcfreader.structure.Donnee;

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
	public ConvertisseurOctetsVersDonnees<Integer> getHandler(int tailleLue) {
		return new ConvertisseurOctetsVersDonnees.ViaSequenceur<>(new NombreBER(),
				r -> new Donnee<>(BlocInt32.this, r));
	}

	@Override
	protected Integer extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		return desequenceur.$lireUnNombreBER();
	}
}