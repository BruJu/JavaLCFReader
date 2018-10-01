package fr.bruju.lcfreader.structure.bloc;

import fr.bruju.lcfreader.structure.MiniBloc;
import fr.bruju.lcfreader.structure.modele.Desequenceur;


public class BlocSimple<T> extends Bloc<T> {
	private final MiniBloc<T> sequenceur;
	private final T valeurParDefaut;
	
	public BlocSimple(int index, String nom, String type, MiniBloc<T> sequenceur, String defaut) {
		super(index, nom, "Simple_" + type);
		this.sequenceur = sequenceur;
		valeurParDefaut = defaut.equals("") ? null : sequenceur.convertirDefaut(defaut);
	}

	@Override
	protected String getNomType() {
		return typeComplet;
	}

	@Override
	public T extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		return sequenceur.extraireDonnee(desequenceur, tailleLue);
	}

	@Override
	public String convertirEnChaineUneValeur(T valeur) {
		return sequenceur.convertirEnChaineUneValeur(valeur);
	}

	@Override
	public void afficherSousArchi(int niveau, T value) {
		sequenceur.afficherSousArchi(niveau, value);
	}

	@Override
	public T valeurParDefaut() {
		return valeurParDefaut;
	}
	
	
}