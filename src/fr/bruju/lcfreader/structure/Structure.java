package fr.bruju.lcfreader.structure;

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

	/** Ajoute un champ */
	public void ajouterChamp(String[] donnees) {
		Bloc<?> bloc = Bloc.instancier(donnees);
		
		if (bloc != null) {
			champs.put(bloc.getChamp().index, bloc);
		}
	}

	/** Donne le champ ayant l'index donné */
	public Bloc<?> trouverChampIndex(byte octet) {
		return champs.get((int) octet);
	}
}