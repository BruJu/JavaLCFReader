package fr.bruju.lcfreader.sequenceur.sequences;

import java.util.List;

import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.sequenceur.lecteurs.Desequenceur;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;
import fr.bruju.lcfreader.structure.Donnee;
import fr.bruju.lcfreader.structure.Structure;
import fr.bruju.lcfreader.structure.blocs.Bloc;

/**
 * Lit des blocs de la forme [données]* jusqu'à avoir tout lu conformément à la base de données des structures
 * 
 * @author Bruju
 *
 */
public class SequenceurLCFEnSerie implements SequenceurLCFAEtat {
	/** Donnée en cours de construction */
	public final EnsembleDeDonnees data;

	/** Liste des blocs à lire */
	private final List<Bloc<?>> blocsAExplorer;
	/** Numéro du bloc actuel */
	private int indiceBlocActuel = -1;

	/**
	 * Crée le sequenceur à état
	 * 
	 * @param data La donnée à remplir
	 * @param codes La liste des codes
	 */
	SequenceurLCFEnSerie(EnsembleDeDonnees data) {
		this.data = data;

		Structure structure = BaseDeDonneesDesStructures.getInstance().get(data.nomStruct);
		blocsAExplorer = structure.getSerie();

		nouveauSousChamp();
	}

	/* =====================
	 * SEQUENCEUR LCF A ETAT
	 * ===================== */

	@Override
	public boolean lireOctet(byte octet) {
		Donnee<?> r = handler.accumuler(octet);

		if (r == null) {
			return true;
		} else {
			data.push(r);
			return nouveauSousChamp();
		}
	}

	@Override
	public EnsembleDeDonnees getResultat() {
		return data;
	}

	/* ======================================================
	 * Un seul état : celui consistant à lire le champ actuel
	 * ====================================================== */

	/** Le traiteur pour le champ actuel */
	private ConvertisseurOctetsVersDonnees<?> handler;

	/**
	 * Avance vers le bloc suivant
	 * 
	 * @return Vrai si il y a un bloc suivant à lire
	 */
	private boolean nouveauSousChamp() {
		if (++indiceBlocActuel == blocsAExplorer.size()) {
			return false;
		}

		Bloc<?> blocAExplorer = blocsAExplorer.get(indiceBlocActuel);
		handler = blocAExplorer.getHandler(data.getTaille(blocAExplorer));
		return true;
	}

	@Override
	public EnsembleDeDonnees lireOctet(Desequenceur desequenceur, int parametre) {
		while (lireOctet(desequenceur.suivant()));
		return data;
	}
}
