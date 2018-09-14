package random;

import java.util.ArrayList;
import java.util.List;

import random.sequenceur.SequenceurViaLCF;
import random.sequenceur.TailleChaine;
import random.structure.BaseDeDonneesDesStructures;
import random.structure.Data;

public class DonneesLues {
	private List<Data<?>> donnees;
	public final String nomStruct;
	
	private DonneesLues(String nomStruct) {
		donnees = new ArrayList<>();
		this.nomStruct = nomStruct;
	}
	
	public static DonneesLues lireFichier(String chemin, BaseDeDonneesDesStructures codes) {
		
		// Connaître le type de fichier
		LecteurDeBytes lecteur = new LecteurDeBytes(new Lecture().mapToBytes(chemin));
		String type = lecteur.sequencer(new TailleChaine());
		
		String nomStruct;
		
		switch (type) {
		case "LcfMapUnit":
			nomStruct = "Map";
			break;
		default: // Type Inconnu
			System.out.println(type);
			return null;
		
		}

		DonneesLues data = new DonneesLues(nomStruct);
		
		lecteur.sequencer(new SequenceurViaLCF(data, codes));
		
		return data;
	}

	public void afficher() {
		donnees.forEach(data -> System.out.println(data.champ.getRepresentation() + " -> " + data.value.toString()));
	}

	public void push(Data<?> blocData) {
		donnees.add(blocData);
	}
}
