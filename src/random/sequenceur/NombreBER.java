package random.sequenceur;

/**
 * Lit un nombre encodé avec l'encodage BER. (?)
 * 
 * 
 * @author Bruju
 *
 */
public class NombreBER implements LecteurDeSequence<Long> {
	long nombre = 0;

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
