package fr.bruju.lcfreader.sequenceur.sequences;

/** Un etat dans une machine à état lisant des octets */
public interface Etat {
	/** Accepte l'octet dans l'état */
	public Etat lireOctet(byte octet);
}