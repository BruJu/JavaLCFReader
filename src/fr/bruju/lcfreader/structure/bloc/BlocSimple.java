package fr.bruju.lcfreader.structure.bloc;

import fr.bruju.lcfreader.structure.MiniBloc;
import fr.bruju.lcfreader.structure.modele.Desequenceur;

/**
 * Un bloc constitué d'un seul élément
 * 
 * @author Bruju
 *
 * @param <T> Le type de l'élément
 */
public class BlocSimple<T> extends Bloc<T> {
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */
	
	/** Mini bloc utilisé pour construire les éléments */
	private final MiniBloc<T> miniBloc;
	/** Valeur par défaut */
	private final T valeurParDefaut;
	
	public BlocSimple(int index, String nom, String type, MiniBloc<T> miniBloc, String defaut) {
		super(index, nom, "Simple_" + type);
		this.miniBloc = miniBloc;
		valeurParDefaut = defaut.equals("") ? null : miniBloc.convertirDefaut(defaut);
	}

	
	/* ====================
	 * PROPRIETES D'UN BLOC
	 * ==================== */

	@Override
	protected String getNomType() {
		return typeComplet;
	}

	@Override
	public T valeurParDefaut() {
		return valeurParDefaut;
	}
	
	
	/* =====================
	 * CONSTRUIRE UNE VALEUR
	 * ===================== */
	
	@Override
	public T extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		return miniBloc.extraireDonnee(desequenceur, tailleLue);
	}

	
	/* =========
	 * AFFICHAGE
	 * ========= */
	
	@Override
	public String convertirEnChaineUneValeur(T valeur) {
		return miniBloc.convertirEnChaineUneValeur(valeur);
	}

	@Override
	public String afficherSousArchi(int niveau, T value) {
		return miniBloc.afficherSousArchi(niveau, value);
	}
}