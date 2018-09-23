package fr.bruju.lcfreader.sequenceur.sequences;

import fr.bruju.lcfreader.sequenceur.lecteurs.Desequenceur;

public interface Sequenceur<T> {
	
	
	
	public T lireOctet(Desequenceur desequenceur);
}
