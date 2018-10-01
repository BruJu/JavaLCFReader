package fr.bruju.lcfreader.structure;

import fr.bruju.lcfreader.structure.bloc.Bloc;

/**
 * Une donnée représente une valeur par rapport à un bloc.
 * 
 * @author Bruju
 *
 * @param <T> Le type java utilisé pour encoder la donnée
 */
public class Donnee<T> {
	/** Le bloc associé décrivant le type */
	public Bloc<T> bloc;
	/** La valeur */
	public T value;

	/**
	 * Crée une donnée liée au bloc donné et avec la valeur par défaut lié au bloc
	 * 
	 * @param bloc Le bloc décrivant le type de la donnée
	 */
	public Donnee(Bloc<T> bloc) {
		this.bloc = bloc;
		value = null;
	}

	/**
	 * Crée une donnée liée au bloc donné et avec la valeur donnée
	 * 
	 * @param bloc Le bloc décrivant le type
	 * @param value La valeur
	 */
	public Donnee(Bloc<T> bloc, T value) {
		this.bloc = bloc;
		this.value = value;
	}


	/**
	 * Converti la valeur de la donnée en chaîne par rapport au bloc
	 * 
	 * @return Une représentation en chaîne de la valeur
	 */
	public String getString() {
		return bloc.convertirEnChaineUneValeur(value);
	}

	/**
	 * Affiche l'architecture des données contenues par cette donnée en considérant qu'on est au niveau d'identation
	 * donné.
	 * 
	 * @param niveau Le niveau d'indentation
	 */
	public String afficherSousArchi(int niveau) {
		return bloc.afficherSousArchi(niveau, value);
	}
}