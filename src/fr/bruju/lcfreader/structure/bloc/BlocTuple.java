package fr.bruju.lcfreader.structure.bloc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.structure.MiniBloc;
import fr.bruju.lcfreader.structure.modele.Desequenceur;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;


public class BlocTuple<T> extends Bloc<List<T>> {
	private final MiniBloc<T> miniBloc;
	private final int nombreDElements;

	public BlocTuple(int index, String nom, String type, int nombreDElements, MiniBloc<T> miniBloc) {
		super(index, nom, "Tuple_" + nombreDElements + "_" + type);
		this.miniBloc = miniBloc;
		this.nombreDElements = nombreDElements;
	}

	@Override
	protected String getNomType() {
		return typeComplet;
	}

	@Override
	public List<T> extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		int nombreDElements = this.nombreDElements;
		
		List<T> liste = new ArrayList<>(nombreDElements);
		
		while (nombreDElements != 0) {
			T element = miniBloc.extraireDonnee(desequenceur, -1);
			liste.add(element);
			nombreDElements --;
		}
		
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