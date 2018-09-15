package fr.bruju.lcfreader.sequenceur.lecteurs;

import fr.bruju.lcfreader.sequenceur.sequences.LecteurDeSequence;

public interface Desequenceur {
	/**
	 * Lance la lecteur d'octets en utilisant le sequenceur donné. La lecteur se fait jusqu'à que le sequenceur
	 * renvoie faux ou que l'on arrive à la fin.
	 * @param sequenceur Le sequenceur
	 * @return Le résultat du séquenceur, ou null si une erreur se produit
	 */
	public <T> T sequencer(LecteurDeSequence<T> sequenceur);
}