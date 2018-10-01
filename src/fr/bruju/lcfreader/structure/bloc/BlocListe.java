package fr.bruju.lcfreader.structure.bloc;

import fr.bruju.lcfreader.structure.MiniBloc;
import fr.bruju.lcfreader.structure.modele.Desequenceur;

/**
 * Un bloc qui construit des listes d'éléments en commenant par liste le nombre d'éléments
 * 
 * @author Bruju
 *
 * @param <T> Le type des éléments
 */
public final class BlocListe<T> extends BlocListeDeTailleConnue<T> {
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */
	
	/**
	 * Construit un bloc demandant la taille de la liste avant de la lire
	 * @param index ID du champ
	 * @param nom Nom du champ
	 * @param type Type du champ indiqué
	 * @param miniBloc Mini bloc à utiliser pour instancier les éléments
	 */
	public BlocListe(int index, String nom, String type, MiniBloc<T> miniBloc) {
		super(index, nom, "Liste_" + type, miniBloc);
	}

	/* =====================
	 * CONSTRUIRE UNE VALEUR
	 * ===================== */

	@Override
	protected int getNombreDElements(Desequenceur desequenceur) {
		return desequenceur.$lireUnNombreBER();
	}
}
