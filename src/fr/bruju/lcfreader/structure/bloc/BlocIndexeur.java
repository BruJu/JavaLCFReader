package fr.bruju.lcfreader.structure.bloc;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.structure.MiniBloc;
import fr.bruju.lcfreader.structure.modele.Desequenceur;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;


public class BlocIndexeur<T> extends Bloc<Map<Integer, T>> {
	private final MiniBloc<T> sequenceur;

	public BlocIndexeur(int index, String nom, String type, MiniBloc<T> sequenceur) {
		super(index, nom, "TableauIndexe_" + type);
		this.sequenceur = sequenceur;
	}

	@Override
	protected String getNomType() {
		return typeComplet;
	}

	@Override
	public Map<Integer, T> extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		int nombreElements = desequenceur.$lireUnNombreBER();
		
		Map<Integer, T> carte = new LinkedHashMap<>();
		int id;
		T donnee;
		
		while (nombreElements != 0) {
			id = desequenceur.$lireUnNombreBER();
			donnee = sequenceur.extraireDonnee(desequenceur, -1);
			
			carte.put(id, donnee);
			nombreElements--;
		}
		
		return carte;
	}
	
	@Override
	public String convertirEnChaineUneValeur(Map<Integer, T> valeur) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		sb.append(valeur.entrySet().stream()
			            .map(this::convertirEntree)
			            .collect(Collectors.joining(", ")));
		
		sb.append("]");
		
		return sb.toString();
	}

	public String convertirEntree(Map.Entry<Integer, T> entree) {
		return  "[" + entree.getKey() + "] " + sequenceur.convertirEnChaineUneValeur(entree.getValue());
	}
	
	public void afficherSousArchi(T donnee, int niveau) {
		if (!(donnee instanceof EnsembleDeDonnees))
				return;
		
		EnsembleDeDonnees ensemble = (EnsembleDeDonnees) donnee;
		ensemble.afficherArchitecture(niveau);
	}
	
	
	@Override
	public void afficherSousArchi(int niveau, Map<Integer, T> value) {
		if (value.isEmpty())
			return;
		
		value.forEach((k, v) -> afficherSousArchi(v, niveau));
	}
}