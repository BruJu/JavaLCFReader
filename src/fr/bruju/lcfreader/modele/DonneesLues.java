package fr.bruju.lcfreader.modele;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.sequenceur.lecteurs.LecteurDeFichierOctetParOctet;
import fr.bruju.lcfreader.sequenceur.sequences.SequenceurLCFAEtat;
import fr.bruju.lcfreader.sequenceur.sequences.TailleChaine;
import fr.bruju.lcfreader.structure.Data;

public class DonneesLues {
	private List<Data<?>> donnees;
	public final String nomStruct;
	
	public DonneesLues(String nomStruct) {
		donnees = new ArrayList<>();
		this.nomStruct = nomStruct;
	}
	
	public static DonneesLues lireFichier(String chemin) {
		
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
		
		lecteur.sequencer(new SequenceurLCFAEtat(data));
		
		return data;
	}

	public void afficher() {
		donnees.forEach(data -> System.out.println(data.bloc.getTrueRepresetantion() + " -> " + data.valueToString()));
	}
	
	public void afficherArchi() {
		afficherArchi(0);
	}
	
	

	public void afficherArchi(int niveau) {
		tab(niveau);
		System.out.println(nomStruct);
		donnees.forEach(data -> {
			tab(niveau);
			System.out.println(data.bloc.getTrueRepresetantion());
			data.afficherSousArchi(niveau+1);
		});
	}
	
	private static void tab(int niveau) {
		for (int i = 0 ; i != niveau ; i++)
			System.out.print("  ");
	}
	

	public void push(Data<?> blocData) {
		donnees.add(blocData);
	}

	public String getRepresentation() {
		return nomStruct + " -> " +
			donnees.stream()
				   .map(d -> d.bloc.getChamp().nom + ":" + d.valueToString())
				   .collect(Collectors.joining(" ; "));
	}
}
