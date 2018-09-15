package fr.bruju.lcfreader.sequenceur.sequences;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.debug.BytePrinter;
import fr.bruju.lcfreader.modele.DonneesLues;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;
import fr.bruju.lcfreader.structure.Champ;
import fr.bruju.lcfreader.structure.Data;
import fr.bruju.lcfreader.structure.Structure;

/**
 * Lit des blocs de la forme [code] [taille] [données] jusqu'à trouver le code 0.
 * <br>
 * Design Pattern : Etat
 * 
 * @author Bruju
 *
 */

public class SequenceurLCFAEtat implements LecteurDeSequence<Void> {
	/** Donnée en cours de construction */
	public final DonneesLues data;
	/** Structure contenant les codes de la donnée en cours de construction */
	private final Structure structure;
	
	/** Base de données des structures pour le passage aux sous structures */
	private final BaseDeDonneesDesStructures codes;
	
	/** Etat en cours pour la lecture d'octets */
	private Etat etat;
	
	/**
	 * Crée le sequenceur à état
	 * @param data La donnée à remplir
	 * @param codes La liste des codes
	 */
	public SequenceurLCFAEtat(DonneesLues data, BaseDeDonneesDesStructures codes) {
		this.data = data;
		this.structure = codes.structures.get(data.nomStruct);
		this.codes = codes;
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
			BytePrinter.printByte(octet, 'C');
			
			if (octet == 0) {
				System.out.println("0 détecté");
				return null;
			}
			
			Champ<?> champ = structure.trouverChampIndex(octet);
			
			if (champ == null) {
				System.out.println("Pas de champ trouvé " + Utilitaire.toHex(octet));
				return null;
			}
			
			return new EtatLireTaille(champ);
		}
	}
	
	/**
	 * Un état dont le but est de lire le nombre d'octets
	 *
	 */
	private class EtatLireTaille implements Etat {
		/** Le champ en cours de lecture */
		private Champ<?> champ;
		/** La taille en cours de construction */
		private int tailleLue;

		/**
		 * Crée un état dont le but est de lire le nombre d'octets du champ
		 * @param champ Le champ
		 */
		public EtatLireTaille(Champ<?> champ) {
			this.champ = champ;
			this.tailleLue = 0;
		}

		@Override
		public Etat lireOctet(byte octet) {
			BytePrinter.printByte(octet, 'T');
			tailleLue = tailleLue * 0x80 + (octet & 0x7F);
			return ((octet & 0x80) == 0) ? new EtatLireDonnees<>(champ, tailleLue) : this;
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
		private Handler<T> handler;

		/**
		 * Crée un état de lecture des données pour le champ
		 * @param champ Le champ en cours de lecture
		 * @param tailleLue Le nombre d'octets
		 */
		public EtatLireDonnees(Champ<T> champ, int tailleLue) {
			handler = champ.getHandler(tailleLue, codes);
		}

		@Override
		public Etat lireOctet(byte octet) {
			BytePrinter.printByte(octet, 'D');
			Data<?> r = handler.traiter(octet);
			
			if (r == null) {
				return this;
			} else {
				data.push(r);
				return new EtatLireCode();
			}
		}
	}
}