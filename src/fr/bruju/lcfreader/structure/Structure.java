package fr.bruju.lcfreader.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fr.bruju.lcfreader.structure.blocs.Bloc;
import fr.bruju.lcfreader.structure.blocs.Blocs;

/**
 * L'ensemble des associations index - champs pour un type de données
 * 
 * @author Bruju
 *
 */
public class Structure {
	/** Liste des champs connus */
	private Map<Integer, Bloc<?>> champs = new TreeMap<>();
	
	private List<Bloc<?>> serie = new ArrayList<>();

	/** Ajoute un champ */
	public void ajouterChamp(String[] donnees) {
		Bloc<?> bloc = Blocs.instancier(donnees);
		
		if (bloc.index != 0) {
			champs.put(bloc.index, bloc);
		} else {
			serie.add(bloc);
		}
	}

	/** Donne le champ ayant l'index donné */
	public Bloc<?> trouverChampIndex(byte octet) {
		return champs.get((int) octet);
	}
	
	
	public boolean estSerie() {
		return !serie.isEmpty();
	}

	public List<Bloc<?>> getSerie() {
		return serie;
	}
	
	
}