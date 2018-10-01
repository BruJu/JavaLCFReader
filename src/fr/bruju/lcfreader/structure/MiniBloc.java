package fr.bruju.lcfreader.structure;

import fr.bruju.lcfreader.structure.modele.Desequenceur;

public interface MiniBloc<T> {

	
	public abstract T extraireDonnee(Desequenceur desequenceur, int tailleLue);


	/**
	 * Converti la valeur en une chaîne comme si elle était issue du bloc
	 * @param valeur La valeur
	 * @return Une représentation en chaîne de la valeur
	 */
	public default String convertirEnChaineUneValeur(T valeur) {
		return valeur.toString();
	}
	
	/**
	 * Affiche les ensembles de données contenus par la valeur passée
	 * @param niveau Le niveau d'indentation actuel
	 * @param value La valeur dont on souhaite connaître les sous ensembles de données
	 */
	public default void afficherSousArchi(int niveau, T value) {
	}


	public default T convertirDefaut(String defaut) {
		return null;
	}
}
