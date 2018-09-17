package fr.bruju.lcfreader.sequenceur.sequences;

import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;

/**
 * Un séquenceur LCF à état est un objet pouvant lire les octets d'une structure de données des fichiers LCF et leur
 * donner un sens.
 * 
 * @author Bruju
 *
 */
public interface SequenceurLCFAEtat extends LecteurDeSequence<EnsembleDeDonnees> {
	/**
	 * Donne le séquenceur pour déséquencer la donnée selon si elle est en série ou pas
	 * 
	 * @param data La donnée à déséquencer
	 * @return Le déséquenceur
	 */
	public static SequenceurLCFAEtat instancier(EnsembleDeDonnees data) {
		if (BaseDeDonneesDesStructures.getInstance().get(data.nomStruct).estSerie()) {
			return new SequenceurLCFEnSerie(data);
		} else {
			return new SequenceurLCFDiscontinu(data);
		}
	}
}
