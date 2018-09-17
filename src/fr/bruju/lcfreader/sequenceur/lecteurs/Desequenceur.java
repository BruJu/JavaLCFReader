package fr.bruju.lcfreader.sequenceur.lecteurs;

import fr.bruju.lcfreader.sequenceur.sequences.LecteurDeSequence;

/**
 * Un déséquenceur est une interface permettant à des lecteurs de séquence de lire des octets.
 * <br>Par exemple, si il on veut lire dans un fichier, le déséquenceur s'occupe de la partie "extraire les octets
 * du fichier". Lorsque la méthode séquencer est appelée, il va lire des octets depuis le fichier, et transmettre
 * ces octets au sequenceur via sequenceur.lireOctet(octet) jusqu'à que cette méthode renvoie faux.
 * <br><br>
 * L'objectif de cette interface est donc de séparer la lecture du fichier et l'interprétation des données de ce
 * fichier.
 * 
 * @author Bruju
 *
 */
public interface Desequenceur {
	/**
	 * Lance la lecture d'octets en utilisant le sequenceur donné. La lecture se fait jusqu'à que le sequenceur
	 * renvoie faux ou que l'on arrive à la fin.
	 * @param sequenceur Le sequenceur
	 * @return Le résultat du séquenceur, ou null si une erreur se produit
	 */
	public <T> T sequencer(LecteurDeSequence<T> sequenceur);
}