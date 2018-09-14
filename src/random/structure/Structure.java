package random.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Structure {
	public List<Champ<?>> champs = new ArrayList<>();
	
	public List<Data<?>> getData() {
		return champs.stream().map(champ -> new Data<>(champ)).collect(Collectors.toList());
	}

	public void ajouterChamp(String[] donnees) {
		champs.add(Champ.instancier(donnees));
	}

	public Champ<?> trouverChampIndex(byte octet) {
		for (Champ<?> champ : champs) {
			if (champ.index == octet) {
				return champ;
			}
		}
		
		return null;
	}
}