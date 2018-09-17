package fr.bruju.lcfreader.sequenceur.sequences;

import java.util.List;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;
import fr.bruju.lcfreader.structure.Donnee;
import fr.bruju.lcfreader.structure.Structure;
import fr.bruju.lcfreader.structure.blocs.Bloc;

/**
 * Lit des blocs de la forme [code] [taille] [données] jusqu'à trouver le code 0.
 * <br>
 * Design Pattern : Etat
 * 
 * @author Bruju
 *
 */

public class SequenceurLCFEnSerie implements SequenceurLCFAEtat {
	/** Donnée en cours de construction */
	public final EnsembleDeDonnees data;
	/** Structure contenant les codes de la donnée en cours de construction */
	private final Structure structure;
	
	private final List<Bloc<?>> blocsAExplorer;
	
	
	private int sousChampActuel = -1;
	
	/**
	 * Crée le sequenceur à état
	 * @param data La donnée à remplir
	 * @param codes La liste des codes
	 */
	SequenceurLCFEnSerie(EnsembleDeDonnees data) {
		this.data = data;
		this.structure = BaseDeDonneesDesStructures.getInstance().get(data.nomStruct);
		blocsAExplorer = structure.getSerie();
		
		nouveauSousChamp();
	}
	
	private boolean nouveauSousChamp() {
		sousChampActuel ++;
		
		if (sousChampActuel == blocsAExplorer.size()) {
			return false;
		} else {
			Bloc<?> bloc = blocsAExplorer.get(sousChampActuel);
			String n = bloc.getChamp().nom;
			Integer t = data.getTaille(n);
			//handler.fournirTailles(t);

			if (t == null)
				t = -1;
			
			handler = bloc.getHandler(t);
			//handler.fournirTailles(t);
			return true;
		}
	}

	@Override
	public EnsembleDeDonnees getResultat() {
		return data;
	}

	/** Le traiteur pour le champ actuel */
	private ConvertisseurOctetsVersDonnees<?> handler;


	@Override
	public boolean lireOctet(byte octet) {

		System.out.print(" " + data.nomStruct + Utilitaire.toHex(octet));
		Donnee<?> r = handler.accumuler(octet);
		
		if (r == null) {
			return true;
		} else {
			data.push(r);
			
			boolean suite = nouveauSousChamp();

			if (suite) {
				
			}
			
			return suite;
		}
	}
}
