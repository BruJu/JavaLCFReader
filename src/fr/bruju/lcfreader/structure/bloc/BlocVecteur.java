package fr.bruju.lcfreader.structure.bloc;

import java.util.ArrayList;
import java.util.List;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.XMLInsecticide;
import fr.bruju.lcfreader.structure.blocs.Bloc;
import fr.bruju.lcfreader.structure.blocs.Champ;
import fr.bruju.lcfreader.structure.blocs.mini.MiniBloc;

public class BlocVecteur<T> extends Bloc<List<T>> {
	private MiniBloc<T> sequenceur;
	private String nomChamp;

	public BlocVecteur(Champ champ, MiniBloc<T> sequenceur) {
		super(champ);
		this.sequenceur = sequenceur;
		this.nomChamp = "Vecteur_" + champ.vraiType;
	}

	@Override
	protected String getNomType() {
		return nomChamp;
	}

	@Override
	public List<T> extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		if (tailleLue < 0) {
			throw new RuntimeException("Taille Lue = " + tailleLue);
		}
		
		XMLInsecticide.balise(nomChamp);
		
		List<T> liste = new ArrayList<>();
		
		while (desequenceur.nonVide()) {
			T element = sequenceur.extraireDonnee(desequenceur, -1);
			liste.add(element);
		}
		
		XMLInsecticide.fermer();
		
		return liste;
	}
}