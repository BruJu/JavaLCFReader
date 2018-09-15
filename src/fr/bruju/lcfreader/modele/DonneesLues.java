package fr.bruju.lcfreader.modele;

import java.util.ArrayList;
import java.util.List;

import fr.bruju.lcfreader.sequenceur.LecteurDeFichierOctetParOctet;
import fr.bruju.lcfreader.sequenceur.SequenceurViaLCF;
import fr.bruju.lcfreader.sequenceur.TailleChaine;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;
import fr.bruju.lcfreader.structure.Data;

public class DonneesLues {
	private List<Data<?>> donnees;
	public final String nomStruct;
	
	private DonneesLues(String nomStruct) {
		donnees = new ArrayList<>();
		this.nomStruct = nomStruct;
	}
	
	public static DonneesLues lireFichier(String chemin, BaseDeDonneesDesStructures codes) {
		
		// Connaître le type de fichier
		LecteurDeFichierOctetParOctet lecteur = LecteurDeFichierOctetParOctet.instancier(chemin);
		
		String type = lecteur.sequencer(new TailleChaine());
		
		String nomStruct;
		
		switch (type) {
		case "LcfMapUnit":
			nomStruct = "Map";
			break;
		default: // Type Inconnu
			System.out.println("Inconnu " + type);
			return null;
		
		}

		DonneesLues data = new DonneesLues(nomStruct);
		
		lecteur.sequencer(new SequenceurViaLCF(data, codes));
		
		return data;
	}

	public void afficher() {
		donnees.forEach(data -> System.out.println(data.champ.getRepresentation() + " -> " + data.valueToString()));
	}

	public void push(Data<?> blocData) {
		donnees.add(blocData);
	}
}
