package random.sequenceur;

import random.DonneesLues;
import random.structure.BaseDeDonneesDesStructures;

public class SequenceurViaLCF implements LecteurDeSequence<Void> {
	private DonneesLues data;
	private BaseDeDonneesDesStructures codes;

	public SequenceurViaLCF(DonneesLues data, BaseDeDonneesDesStructures codes) {
		this.data = data;
		this.codes = codes;
	}

	@Override
	public boolean lireOctet(byte octet) {
		return false;
	}

	@Override
	public Void getResultat() {
		return null;
	}

}
