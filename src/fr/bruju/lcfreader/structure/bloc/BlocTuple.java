package fr.bruju.lcfreader.structure.bloc;

import fr.bruju.lcfreader.structure.MiniBloc;
import fr.bruju.lcfreader.structure.modele.Desequenceur;

/**
 * Un bloc faisant des listes avec un nombre prédéterminé d'éléments
 * 
 * @author Bruju
 *
 * @param <T> Le type des éléments de la liste
 */
public final class BlocTuple<T> extends BlocListeDeTailleConnue<T> {
	/** Le nombre d'éléments */
	private final int nombreDElements;

	/**
	 * Crée un bloc listeur
	 * @param index ID du champ
	 * @param nom Nom du champ
	 * @param type Type du champ indiqué
	 * @param nombreDElements Le nombre d'éléments
	 * @param miniBloc Mini bloc à utiliser pour instancier les éléments
	 */
	public BlocTuple(int index, String nom, String type, int nombreDElements, MiniBloc<T> miniBloc) {
		super(index, nom, "Tuple_" + nombreDElements + "_" + type, miniBloc);
		this.nombreDElements = nombreDElements;
	}

	@Override
	protected int getNombreDElements(Desequenceur desequenceur) {
		return nombreDElements;
	}
}