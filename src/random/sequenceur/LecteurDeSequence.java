package random.sequenceur;

public interface LecteurDeSequence<T> {
	public boolean lireByte(Byte byteLu);
	
	public T getResultat();
}
