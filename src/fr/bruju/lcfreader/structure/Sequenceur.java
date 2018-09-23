package fr.bruju.lcfreader.structure;

import fr.bruju.lcfreader.modele.Desequenceur;

public interface Sequenceur<T> {
	public T lireOctet(Desequenceur desequenceur, int parametre);
}
