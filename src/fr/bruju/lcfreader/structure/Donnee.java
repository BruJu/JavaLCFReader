package fr.bruju.lcfreader.structure;

import fr.bruju.lcfreader.structure.blocs.Bloc;

public class Donnee<T> {
	public Bloc<T> bloc;
	public T value;
	
	public Donnee(Bloc<T> bloc) {
		this.bloc = bloc;
		value = null;
	}
	

	public Donnee(Bloc<T> bloc, T value) {
		this.bloc = bloc;
		this.value = value;
	}


	public String valueToString() {
		return bloc.convertirEnChaineUneValeur(value);
	}


	public void afficherSousArchi(int i) {
		bloc.afficherSousArchi(i, value);
	}
}