package fr.bruju.lcfreader.structure.types;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.XMLInsecticide;
import fr.bruju.lcfreader.structure.Sequenceur;

abstract class SequenceurIntATailleFixe implements Sequenceur<Integer>, PrimitifCpp {
	static class Boolean extends SequenceurIntATailleFixe {
		public Boolean() {
			super(1);
		}

		@Override
		public String getNom() {
			return "Boolean";
		}
	}

	static class UInt8 extends SequenceurIntATailleFixe {
		public UInt8() {
			super(1);
		}

		@Override
		public String getNom() {
			return "UInt8";
		}
	}
	
	static class UInt16 extends SequenceurIntATailleFixe {
		public UInt16() {
			super(2);
		}

		@Override
		public String getNom() {
			return "UInt16";
		}
	}
	
	static class UInt32 extends SequenceurIntATailleFixe {
		public UInt32() {
			super(4);
		}

		@Override
		public String getNom() {
			return "UInt32";
		}
	}
	
	
	
	private final int nombreDOctetsUtilises;
	
	public SequenceurIntATailleFixe(int nombreDOctetsUtilises) {
		this.nombreDOctetsUtilises = nombreDOctetsUtilises;
	}

	@Override
	public Integer lireOctet(Desequenceur desequenceur, int parametre) {
		int valeur = 0;
		byte octet;
		
		for (int i = 0 ; i != this.nombreDOctetsUtilises ; i++) {
			octet = desequenceur.suivant();
			XMLInsecticide.ajouterXML(octet);
			valeur = (valeur * 0x100) + Byte.toUnsignedInt(octet);
		}
		
		XMLInsecticide.xml(" [" + valeur + "]"); 
		
		return valeur;
	}
	
	
}