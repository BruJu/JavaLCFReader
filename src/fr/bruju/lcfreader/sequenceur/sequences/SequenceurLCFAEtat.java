package fr.bruju.lcfreader.sequenceur.sequences;

import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;

public interface SequenceurLCFAEtat extends LecteurDeSequence<EnsembleDeDonnees> {
	public static SequenceurLCFAEtat instancier(EnsembleDeDonnees data) {
		if (BaseDeDonneesDesStructures.getInstance().get(data.nomStruct).estSerie()) {
			System.out.println("SERIE : " + data.nomStruct);
			return new SequenceurLCFEnSerie(data);
		} else {
			return new SequenceurLCFDiscontinu(data);
		}
	}
}
