package fr.bruju.lcfreader.structure.bloc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.structure.MiniBloc;
import fr.bruju.lcfreader.structure.modele.Desequenceur;

/**
 * Un bloc qui construit une liste selon une méthode de taille définie dans la classe fille
 * 
 * @author Bruju
 *
 * @param <T> Le type des éléments
 */
abstract class BlocListeDeTailleConnue<T> extends Bloc<List<T>> {
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */
	
	/** Mini bloc utilisé pour construire les éléments */
	private final MiniBloc<T> miniBloc;

	/**
	 * Crée un bloc listeur
	 * @param index ID du champ
	 * @param nom Nom du champ
	 * @param type Type du champ indiqué
	 * @param miniBloc Mini bloc à utiliser pour instancier les éléments
	 */
	public BlocListeDeTailleConnue(int index, String nom, String type, MiniBloc<T> miniBloc) {
		super(index, nom, type);
		this.miniBloc = miniBloc;
	}
	
	
	/* ====================
	 * PROPRIETES D'UN BLOC
	 * ==================== */

	@Override
	protected final String getNomType() {
		return typeComplet;
	}
	
	
	/* =====================
	 * CONSTRUIRE UNE VALEUR
	 * ===================== */
	
	/**
	 * Donne le nombre d'éléments à lire
	 * @param desequenceur Les octets en cours de lecture
	 * @return Le nombre d'éléments à lire
	 */
	protected abstract int getNombreDElements(Desequenceur desequenceur);
	
	@Override
	public final List<T> extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		int nombreDElements = getNombreDElements(desequenceur);
		
		List<T> liste = new ArrayList<>(nombreDElements);
		
		while (nombreDElements != 0) {
			T element = miniBloc.extraireDonnee(desequenceur, -1);
			liste.add(element);
			nombreDElements --;
		}
		
		return liste;
	}
	
	/* =========
	 * AFFICHAGE
	 * ========= */
	
	@Override
	public final String convertirEnChaineUneValeur(List<T> valeur) {
		return new StringBuilder()
				  .append("[")
				  .append(valeur.stream()
				            	.map(miniBloc::convertirEnChaineUneValeur)
					            .collect(Collectors.joining(", ")))
				  .append("]")
				  .toString();
	}

	@Override
	public final String afficherSousArchi(int niveau, List<T> valeurs) {
		StringBuilder sb = new StringBuilder();
		valeurs.forEach(valeur -> sb.append(miniBloc.afficherSousArchi(niveau, valeur)));
		return sb.toString();
	}
}