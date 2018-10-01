package fr.bruju.lcfreader.structure;

import fr.bruju.lcfreader.structure.modele.Desequenceur;

/**
 * Un minibloc est une classe permettant de lire une série d'octets et de transformer les octets lus en un type lisible
 * en java par l'utilisateur.
 * 
 * @author Bruju
 *
 * @param <T> Le type à retranscrire.
 */
public interface MiniBloc<T> {
	/**
	 * Lit des octets dans le desequenceur donné et renvoie une valeur
	 * @param desequenceur Le tableau d'octets à lire
	 * @param tailleLue La taille allouée (non fiable dans la plupart des cas)
	 * @return La valeur lue
	 */
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
	 * Donne sous forme de chaîne les ensembles de données contenus par la valeur passée
	 * @param niveau Le niveau d'indentation actuel
	 * @param value La valeur dont on souhaite connaître les sous ensembles de données
	 */
	public default String afficherSousArchi(int niveau, T value) {
		return "";
	}

	/**
	 * Converti la chaîne en une valeur par défaut
	 * @param defaut La chaîne
	 * @return La valeur par défaut à adopter
	 */
	public default T convertirDefaut(String defaut) {
		return null;
	}
}
