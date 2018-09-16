package fr.bruju.lcfreader.structure;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import fr.bruju.lcfreader.structure.blocs.Bloc;

/**
 * L'ensemble des associations index - champs pour un type de données
 * 
 * @author Bruju
 *
 */
public class Structure {
	/** Liste des champs connus */
	private Map<Integer, Bloc<?>> champs = new TreeMap<>();

	/** Donne la liste des blocs */
	private Collection<Bloc<?>> getBlocs() {
		return champs.values();
	}

	/** Ajoute un champ */
	public void ajouterChamp(String[] donnees) {
		Bloc<?> bloc = Bloc.instancier(donnees);
		
		if (bloc != null) {
			champs.put(bloc.champ.index, bloc);
		}
	}

	/** Donne le champ ayant l'index donné */
	public Bloc<?> trouverChampIndex(byte octet) {
		return champs.get((int) octet);
	}
}