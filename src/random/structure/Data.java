package random.structure;


public class Data<T> {
	public Champ<T> champ;
	public T value;
	
	public Data(Champ<T> champ) {
		this.champ = champ;
		value = null;
	}
}