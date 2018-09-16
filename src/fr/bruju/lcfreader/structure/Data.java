package fr.bruju.lcfreader.structure;

import fr.bruju.lcfreader.structure.blocs.Bloc;

public class Data<T> {
	public Bloc<T> bloc;
	public T value;
	
	public Data(Bloc<T> bloc) {
		this.bloc = bloc;
		value = null;
	}
	

	public Data(Bloc<T> bloc, T value) {
		this.bloc = bloc;
		this.value = value;
	}


	public String valueToString() {
		return bloc.valueToString(value);
	}
}