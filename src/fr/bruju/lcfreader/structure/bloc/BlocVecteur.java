package fr.bruju.lcfreader.structure.bloc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.modele.XMLInsecticide;
import fr.bruju.lcfreader.structure.blocs.mini.MiniBloc;

public class BlocVecteur<T> extends Bloc<List<T>> {
	private final MiniBloc<T> miniBloc;

	public BlocVecteur(int index, String nom, String type, MiniBloc<T> sequenceur) {
		super(index, nom, "Vecteur_" + type);
		this.miniBloc = sequenceur;
	}

	@Override
	protected String getNomType() {
		return typeComplet;
	}

	@Override
	public List<T> extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		if (tailleLue < 0) {
			throw new RuntimeException("Taille Lue = " + tailleLue);
		}
		
		XMLInsecticide.balise(typeComplet);
		
		List<T> liste = new ArrayList<>();
		
		while (desequenceur.nonVide()) {
			T element = miniBloc.extraireDonnee(desequenceur, -1);
			liste.add(element);
		}
		
		XMLInsecticide.fermer();
		
		return liste;
	}
	
	@Override
	public String convertirEnChaineUneValeur(List<T> valeur) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		sb.append(valeur.stream()
			            .map(v -> miniBloc.convertirEnChaineUneValeur(v))
			            .collect(Collectors.joining(", ")));
		
		sb.append("]");
		
		return sb.toString();
	}

	@Override
	public void afficherSousArchi(int niveau, List<T> value) {
		if (value.isEmpty())
			return;
		
		T premierElement = value.get(0);
		
		if (!(premierElement instanceof EnsembleDeDonnees)) {
			return;
		}
		
		
        value.forEach(data -> ((EnsembleDeDonnees) data).afficherArchitecture(niveau));
	}
}