package fr.bruju.lcfreader.sequenceur.sequences;

/**
 * Un lecteur de séquence traite tous les octets qu'il reçoit. Il peut renvoyer false pour demander à ne pas recevoir
 * d'octets supplémentaires. On peut alors appeler sa méthode getResultat pour obtenir le résultat de ce qu'il a lu.
 * 
 * @author Bruju
 *
 * @param <T> Type des données renvoyées
 */
public interface LecteurDeSequence<T> {
	/**
	 * Traite l'octet donné
	 * 
	 * @param octet L'octet à traiter
	 * @return Vrai si ce lecteur veut recevoir d'autres octets
	 */
	public boolean lireOctet(byte octet);

	/**
	 * Renvoie le résultat de la lecture
	 * 
	 * @return Le résultat
	 */
	public T getResultat();
}
