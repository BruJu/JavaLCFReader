package fr.bruju.lcfreader.sequenceur.sequences;

import fr.bruju.lcfreader.structure.Donnee;

/**
 * Interface permettant de convertir une série d'octets qui sont reçus à la suite en une donnée intelligible.
 * 
 * @author Bruju
 *
 * @param <T> Le type de données généré par le convertisseur
 */
public interface ConvertisseurOctetsVersDonnees<T> {
	/**
	 * Reçoit l'octet suivant composant la donnée sous forme binaire.
	 * @param octet L'octet reçu
	 * @return null si la lecture n'est pas terminée (l'octet reçu n'est pas le dernier). L'objet construit si tous
	 * les octets ont été reçus.
	 */
	public Donnee<T> accumuler(byte octet);
}
