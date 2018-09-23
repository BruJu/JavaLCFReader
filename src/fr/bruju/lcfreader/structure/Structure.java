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
	// TODO : faire deux classes séparées pour la lecture en série et la lecture en indicé

	/** Liste des champs connus */
	private Map<Integer, Bloc<?>> champs = new TreeMap<>();
	/** Liste des blocs pour un décodage en série */
	private List<Bloc<?>> serie = new ArrayList<>();
	
	public final String nom;
	
	
	public Structure(String nom) {
		this.nom = nom;
	}

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

	public Bloc<?> getBloc(Integer numero) {
		return champs.get(numero);
	}
	

	/**
	 * Indique si il faut lire la structure de manière indicée (chaque encodage des données d'un champ est précédé du
	 * numéro du champ et de sa taille en octet) ou de manière séquentielle (chaque donnée est codée l'une après l'autre
	 * dans un ordre précis)
	 * 
	 * @return Vrai si les données présentes dans la structure sont en série
	 */
	public boolean estSerie() {
		return !serie.isEmpty();
	}

	/**
	 * Donne la liste des blocs à lire pour cette structure
	 * 
	 * @return La liste des blocs indiquant comment décrypter cette structure
	 */
	public List<Bloc<?>> getSerie() {
		return serie;
	}

	public Bloc<?> getBloc(String nomBloc) {
		for (Bloc<?> bloc : champs.values()) {
			if (bloc.nom.equals(nomBloc) && !bloc.estUnChampIndiquantLaTaille()) {
				return bloc;
			}
		}

		for (Bloc<?> bloc : serie) {
			if (bloc.nom.equals(nomBloc) && !bloc.estUnChampIndiquantLaTaille()) {
				return bloc;
			}
		}
		
		return null;
	}
	
}