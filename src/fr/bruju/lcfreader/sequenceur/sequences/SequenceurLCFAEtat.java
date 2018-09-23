package fr.bruju.lcfreader.sequenceur.sequences;

import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.structure.Structure;

/**
 * Un séquenceur LCF à état est un objet pouvant lire les octets d'une structure de données des fichiers LCF et leur
 * donner un sens.
 * 
 * @author Bruju
 *
 */
public interface SequenceurLCFAEtat extends Sequenceur<EnsembleDeDonnees> {
	/**
	 * Donne le séquenceur pour déséquencer la donnée selon si elle est en série ou pas
	 * 
	 * @param data La donnée à déséquencer
	 * @return Le déséquenceur
	 */
	public static SequenceurLCFAEtat instancier(Structure structure) {
		if (structure.estSerie()) {
			return new SequenceurLCFEnSerie(structure);
		} else {
			return new SequenceurLCFDiscontinu(structure);
		}
	}
}
