package fr.bruju.lcfreader.sequenceur.sequences;

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

public class SequenceurLCFAEtat implements LecteurDeSequence<Void> {
	// TODO : changer le comportemnet si structure.estSerie()
	
	/** Donnée en cours de construction */
	public final EnsembleDeDonnees data;
	/** Structure contenant les codes de la donnée en cours de construction */
	private final Structure structure;
	
	/** Etat en cours pour la lecture d'octets */
	private Etat etat;
	
	/**
	 * Crée le sequenceur à état
	 * @param data La donnée à remplir
	 * @param codes La liste des codes
	 */
	public SequenceurLCFAEtat(EnsembleDeDonnees data) {
		this.data = data;
		this.structure = BaseDeDonneesDesStructures.getInstance().get(data.nomStruct);
		this.etat = new EtatLireCode();
	}

	@Override
	public boolean lireOctet(byte octet) {
		etat = etat.lireOctet(octet);
		return etat != null;
	}

	@Override
	public Void getResultat() {
		return null;
	}

	/** Un etat dans la machine */
	private static interface Etat {
		/** Accepte l'octet dans l'état */
		public Etat lireOctet(byte octet);
	}
	
	/** Un état lisant le code */
	private class EtatLireCode implements Etat {
		@Override
		public Etat lireOctet(byte octet) {
			if (octet == 0) {
				return null;
			}
			
			Bloc<?> bloc = structure.trouverChampIndex(octet);
			
			if (bloc == null) {
				System.out.println("Pas de champ trouvé " + Utilitaire.toHex(octet));
				return null;
			}
			
			return new EtatLireTaille(bloc);
		}
	}
	
	/**
	 * Un état dont le but est de lire le nombre d'octets
	 *
	 */
	private class EtatLireTaille implements Etat {
		/** Le champ en cours de lecture */
		private Bloc<?> bloc;
		/** La taille en cours de construction */
		private int tailleLue;

		/**
		 * Crée un état dont le but est de lire le nombre d'octets du champ
		 * @param champ Le champ
		 */
		public EtatLireTaille(Bloc<?> bloc) {
			this.bloc = bloc;
			this.tailleLue = 0;
		}

		@Override
		public Etat lireOctet(byte octet) {
			tailleLue = tailleLue * 0x80 + (octet & 0x7F);
			return ((octet & 0x80) == 0) ? new EtatLireDonnees<>(bloc, tailleLue) : this;
		}
	}
	
	/**
	 * Un état dont le but est de lire les données du champ en cours de lecture
	 * 
	 * @author Bruju
	 *
	 */
	private class EtatLireDonnees<T> implements Etat {
		/** Le traiteur pour le champ actuel */
		private ConvertisseurOctetsVersDonnees<T> handler;

		/**
		 * Crée un état de lecture des données pour le champ
		 * @param champ Le champ en cours de lecture
		 * @param tailleLue Le nombre d'octets
		 */
		public EtatLireDonnees(Bloc<T> bloc, int tailleLue) {
			handler = bloc.getHandler(tailleLue);
			handler.fournirTailles(data.getTaille(bloc.getChamp().nom));
		}

		@Override
		public Etat lireOctet(byte octet) {
			Donnee<?> r = handler.accumuler(octet);
			
			if (r == null) {
				return this;
			} else {
				data.push(r);
				return new EtatLireCode();
			}
		}
	}
}
