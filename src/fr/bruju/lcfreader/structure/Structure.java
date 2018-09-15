package fr.bruju.lcfreader.structure;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * L'ensemble des associations index - champs pour un type de données
 * 
 * @author Bruju
 *
 */
public class Structure {
	/** Base de données rattachée à la structure */
	public final BaseDeDonneesDesStructures codes;
	/** Liste des champs connus */
	private Map<Integer, Champ<?>> champs = new TreeMap<>();
	
	/**
	 * Crée la structure
	 * @param codes
	 */
	public Structure(BaseDeDonneesDesStructures codes) {
		this.codes = codes;
	}

	/** Donne la liste des champs */
	public Collection<Champ<?>> getChamps() {
		return champs.values();
	}

	/** Ajoute un champ */
	public void ajouterChamp(String[] donnees) {
		Champ<?> champ = Champ.instancier(donnees, codes);
		
		if (champ != null) {
			champs.put(champ.index, champ);
		}
	}

	/** Donne le champ ayant l'index donné */
	public Champ<?> trouverChampIndex(byte octet) {
		return champs.get((int) octet);
	}
}