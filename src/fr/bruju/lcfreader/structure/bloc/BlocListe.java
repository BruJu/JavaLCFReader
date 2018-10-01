package fr.bruju.lcfreader.structure.bloc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.structure.MiniBloc;
import fr.bruju.lcfreader.structure.modele.Desequenceur;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.structure.modele.XMLInsecticide;

public class BlocListe<T> extends Bloc<List<T>> {
	private final MiniBloc<T> miniBloc;

	public BlocListe(int index, String nom, String type, MiniBloc<T> miniBloc) {
		super(index, nom, "Liste_" + type);
		this.miniBloc = miniBloc;
	}

	@Override
	protected String getNomType() {
		return typeComplet;
	}

	@Override
	public List<T> extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		XMLInsecticide.balise(typeComplet);
		
		XMLInsecticide.balise("nbElem");
		int nombreDElements = desequenceur.$lireUnNombreBER();
		XMLInsecticide.fermer();
		
		List<T> liste = new ArrayList<>(nombreDElements);
		
		while (nombreDElements != 0) {
			T element = miniBloc.extraireDonnee(desequenceur, -1);
			liste.add(element);
			nombreDElements --;
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