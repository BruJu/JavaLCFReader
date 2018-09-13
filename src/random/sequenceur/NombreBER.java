package random.sequenceur;

/**
 * Lit un nombre encodé avec un nombre indéfini d'octets. Les nombre sont encodés avec le premier octet qui indique
 * si le prochain octet fait parti du nombre et 7 octets contenant la valeur.
 * 
 * 
 * @author Bruju
 *
 */
public class NombreBER implements LecteurDeSequence<Long> {
	/** Le nombre lu */
	private long nombre = 0;

	@Override
	public boolean lireOctet(byte byteLu) {
		nombre = nombre * 0x80 + (byteLu & 0x7F);
		return (byteLu & 0x80) != 0;
	}

	@Override
	public Long getResultat() {
		return nombre;
	}
}
