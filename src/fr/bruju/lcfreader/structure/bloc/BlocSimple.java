package fr.bruju.lcfreader.structure.bloc;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.XMLInsecticide;
import fr.bruju.lcfreader.structure.blocs.Bloc;
import fr.bruju.lcfreader.structure.blocs.Champ;
import fr.bruju.lcfreader.structure.blocs.mini.MiniBloc;

public class BlocSimple<T> extends Bloc<T> {
	private final String nom;
	private final MiniBloc<T> sequenceur;
	private final T valeurParDefaut;
	
	public BlocSimple(Champ champ, MiniBloc<T> sequenceur, String defaut) {
		super(champ);
		nom = "Simple_" + champ.vraiType;
		this.sequenceur = sequenceur;
		valeurParDefaut = defaut.equals("") ? null : sequenceur.convertirDefaut(defaut);
	}

	@Override
	protected String getNomType() {
		return nom;
	}

	@Override
	public T extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		XMLInsecticide.balise(nom);
		T objet = sequenceur.extraireDonnee(desequenceur, tailleLue);
		XMLInsecticide.fermer();
		return objet;
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