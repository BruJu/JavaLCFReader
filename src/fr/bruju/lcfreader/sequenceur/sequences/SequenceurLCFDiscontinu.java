package fr.bruju.lcfreader.sequenceur.sequences;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.sequenceur.lecteurs.Desequenceur;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;
import fr.bruju.lcfreader.structure.Donnee;
import fr.bruju.lcfreader.structure.Structure;
import fr.bruju.lcfreader.structure.blocs.Bloc;

/**
 * Lit des blocs de la forme [code] [taille] [données] jusqu'à trouver le code 0. <br>
 * Design Pattern : Etat
 * 
 * @author Bruju
 *
 */
public class SequenceurLCFDiscontinu implements SequenceurLCFAEtat {
	/** Donnée en cours de construction */
	public final EnsembleDeDonnees data;
	/** Structure contenant les codes de la donnée en cours de construction */
	private final Structure structure;


	/**
	 * Crée le sequenceur à état
	 * 
	 * @param data La donnée à remplir
	 * @param codes La liste des codes
	 */
	SequenceurLCFDiscontinu(EnsembleDeDonnees data) {
		this.data = data;
		this.structure = BaseDeDonneesDesStructures.getInstance().get(data.nomStruct);
	}

	/* =====================
	 * SEQUENCEUR LCF A ETAT
	 * ===================== */


	@Override
	public EnsembleDeDonnees lireOctet(Desequenceur desequenceur, int parametre) {
		EnsembleDeDonnees ensembleConstruit = data;
		
		Integer numeroDeBloc;
		int taille;
		
		while (desequenceur.nonVide()) {
			numeroDeBloc = Byte.toUnsignedInt(desequenceur.suivant());
			
			if (numeroDeBloc == 0) {
				return ensembleConstruit;
			}
			
			Bloc<?> bloc = structure.getBloc(numeroDeBloc);
			
			if (bloc == null) {
				throw new RuntimeException("Bloc inconnu");
			}
			
			taille = desequenceur.$lireUnNombreBER();
			
			if (taille != 0) {
				Desequenceur sousDesequenceur = desequenceur.sousSequencer(taille);
				
				ensembleConstruit.push(bloc.lireOctet(sousDesequenceur, taille));
				
				

				if (parametre != -1 && sousDesequenceur.nonVide()) {
					throw new RuntimeException("Lecture d'un bloc non terminé " + sousDesequenceur.octetsRestants());
				}
			}
		}
		
		return ensembleConstruit;
	}


}
