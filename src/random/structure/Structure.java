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
}