package random.sequenceur;

public class LecteurDeNombreBER implements LecteurDeSequence<Long> {
	long nombre = 0;

	@Override
	public boolean lireByte(Byte byteLu) {
		nombre = nombre * 0x80 + (byteLu & 0x7F);
		return (byteLu & 0x80) != 0;
	}

	@Override
	public Long getResultat() {
		return nombre;
	}
}
