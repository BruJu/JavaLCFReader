package fr.bruju.lcfreader.sequenceur.sequences;

/**
 * Lit un nombre encodé avec un nombre indéfini d'octets. Les nombre sont encodés avec le premier octet qui indique si
 * le prochain octet fait parti du nombre et 7 octets contenant la valeur.
 * 
 * 
 * @author Bruju
 *
 */
public class NombreBER implements LecteurDeSequence<Integer> {
	/** Le nombre lu */
	protected int nombre = 0;

	@Override
	public boolean lireOctet(byte byteLu) {
		nombre = nombre * 0x80 + (byteLu & 0x7F);
		boolean r = (byteLu & 0x80) != 0;

		return r;
	}

	@Override
	public Integer getResultat() {
		return nombre;
	}
	

	/**
	 * Un cas particulier de NombreBER qui renvoie null au lieu de 0.
	 * <br>Permet d'accéder au comportement plus facilement en cas d'absence de valeur retournée dans Enchaînement.
	 * 
	 * @author Bruju
	 *
	 */
	public static class NonNull extends NombreBER {
		@Override
		public Integer getResultat() {
			return nombre == 0 ? null : nombre;
		}
	}
}
