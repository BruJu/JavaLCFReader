package fr.bruju.lcfreader.sequenceur.sequences;

import fr.bruju.lcfreader.modele.DonneesLues;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;
import fr.bruju.lcfreader.structure.Champ;
import fr.bruju.lcfreader.structure.Data;

public class SequenceurViaLCF2 implements LecteurDeSequence<Void> {
	public final DonneesLues data;
	private BaseDeDonneesDesStructures codes;
	
	private Etat etat;
	
	public SequenceurViaLCF2(DonneesLues data, BaseDeDonneesDesStructures codes) {
		this.data = data;
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

	private static interface Etat {
		public Etat lireOctet(byte octet);
	}
	
	private class EtatLireCode implements Etat {
		
		@Override
		public Etat lireOctet(byte octet) {
			if (octet == 0)
				return null;
			
			Champ<?> champ = codes.structures.get(data.nomStruct).trouverChampIndex(octet);
			
			if (champ == null)
				return null;
			
			return new EtatLireTaille(champ);
		}
	}
	
	private class EtatLireTaille implements Etat {
		private Champ<?> champ;
		private int tailleLue;

		public EtatLireTaille(Champ<?> champ) {
			this.champ = champ;
			this.tailleLue = 0;
		}

		@Override
		public Etat lireOctet(byte octet) {
			tailleLue = tailleLue * 0x80 + octet & 0x7F;
			
			return ((octet & 0x80) == 0) ? new EtatLireDonnees(champ, tailleLue) : this ;
		}
	}
	
	private class EtatLireDonnees implements Etat {
		private Handler handler;

		public EtatLireDonnees(Champ<?> champ, int tailleLue) {
			handler = champ.getHandler(tailleLue);
		}

		@Override
		public Etat lireOctet(byte octet) {
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
