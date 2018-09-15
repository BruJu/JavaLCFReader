package fr.bruju.lcfreader.sequenceur.sequences;

/**
 * Cherche une chaîne dont la taille est connue
 * @author Bruju
 *
 */
public class Chaine implements LecteurDeSequence<String> {
	/** Caractères lus */
	private char[] caracteres;
	/** Position de prochain caractère à lire */
	private int i = 0;

	/**
	 * Construit un lecteur de séquences dont le but est de lire une chaîne
	 * @param nombreDeCaracteres Taille de la chaîne à lire
	 */
	public Chaine(int nombreDeCaracteres) {
		caracteres = new char[nombreDeCaracteres];
	}
	
	@Override
	public boolean lireOctet(byte octet) {
		caracteres[i++] = (char) (octet & 0xFF);
		return i != caracteres.length;
	}

	@Override
	public String getResultat() {
		return String.valueOf(caracteres);
	}
}
