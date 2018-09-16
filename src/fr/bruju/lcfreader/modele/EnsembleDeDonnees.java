package fr.bruju.lcfreader.modele;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.sequenceur.lecteurs.LecteurDeFichierOctetParOctet;
import fr.bruju.lcfreader.sequenceur.sequences.SequenceurLCFAEtat;
import fr.bruju.lcfreader.sequenceur.sequences.TailleChaine;
import fr.bruju.lcfreader.structure.Donnee;

public class EnsembleDeDonnees {
	private List<Donnee<?>> donnees;
	private Map<String, Integer> tailles;
	
	public final String nomStruct;
	
	public EnsembleDeDonnees(String nomStruct) {
		donnees = new ArrayList<>();
		this.nomStruct = nomStruct;
	}
	
	public static EnsembleDeDonnees lireFichier(String chemin) {
		
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

		EnsembleDeDonnees data = new EnsembleDeDonnees(nomStruct);
		
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
			System.out.print(data.bloc.getTrueRepresetantion());
			
			if (data.value instanceof byte[]) {
				System.out.print(" " + Arrays.toString((byte[]) data.value));
			}
			
			System.out.println();
			data.afficherSousArchi(niveau+1);
		});
	}
	
	private static void tab(int niveau) {
		for (int i = 0 ; i != niveau ; i++)
			System.out.print("  ");
	}
	

	public void push(Donnee<?> blocData) {
		donnees.add(blocData);
		
		if (blocData.bloc.getChamp().sized) {
			if (tailles == null)
				tailles = new HashMap<>();
			
			tailles.put(blocData.bloc.getChamp().nom, (Integer) blocData.value);
		}
	}

	public String getRepresentation() {
		return nomStruct + " -> " +
			donnees.stream()
				   .map(d -> d.bloc.getChamp().nom + ":" + d.valueToString())
				   .collect(Collectors.joining(" ; "));
	}

	public Integer getTaille(String nom) {
		if (tailles == null)
			return null;
		
		return tailles.get(nom);
	}
}
