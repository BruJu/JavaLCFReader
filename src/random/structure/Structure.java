package random.structure;

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
	/** Liste des champs connus */
	private Map<Integer, Champ<?>> champs = new TreeMap<>();
	
	/** Donne la liste des champs */
	public Collection<Champ<?>> getChamps() {
		return champs.values();
	}

	/** Ajoute un champ */
	public void ajouterChamp(String[] donnees) {
		Champ<?> champ = Champ.instancier(donnees);
		
		if (champ != null) {
			champs.put(champ.index, champ);
		}
	}

	/** Donne le champ ayant l'index donné */
	public Champ<?> trouverChampIndex(byte octet) {
		return champs.get((int) octet);
	}
}