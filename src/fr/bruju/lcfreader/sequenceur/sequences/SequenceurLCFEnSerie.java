package fr.bruju.lcfreader.sequenceur.sequences;

import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.sequenceur.lecteurs.Desequenceur;
import fr.bruju.lcfreader.structure.Structure;
import fr.bruju.lcfreader.structure.blocs.Bloc;

/**
 * Lit des blocs de la forme [données]* jusqu'à avoir tout lu conformément à la base de données des structures
 * 
 * @author Bruju
 *
 */
public class SequenceurLCFEnSerie implements SequenceurLCFAEtat {
	private Structure structure;
	

	/**
	 * Crée le sequenceur à état
	 * 
	 * @param data La donnée à remplir
	 * @param codes La liste des codes
	 */
	SequenceurLCFEnSerie(Structure structure) {
		this.structure = structure;
	}

	/* =====================
	 * SEQUENCEUR LCF A ETAT
	 * ===================== */



	@Override
	public EnsembleDeDonnees lireOctet(Desequenceur desequenceur, int parametre) {
		EnsembleDeDonnees ensemble = new EnsembleDeDonnees(structure);
		
		for (Bloc<?> bloc : structure.getSerie()) {
			ensemble.push(bloc.lireOctet(desequenceur, -1));
		}
		
		return ensemble;
	}
}
