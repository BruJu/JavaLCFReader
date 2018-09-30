package fr.bruju.lcfreader.structure.types;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.XMLInsecticide;

public abstract class SequenceurIntATailleFixe implements PrimitifCpp {
	public static class Boolean extends SequenceurIntATailleFixe {
		public Boolean() {
			super(1);
		}

		@Override
		public String getNom() {
			return "Boolean";
		}

		@Override
		public Integer convertirDefaut(String defaut) {
			switch (defaut) {
			case "False":
				return 1;
			case "True":
				return 0;
			default:
				throw new RuntimeException("Boolean avec valeur par défaut " + defaut);
			}
		}
		
	}

	public static class UInt8 extends SequenceurIntATailleFixe {
		public UInt8() {
			super(1);
		}

		@Override
		public String getNom() {
			return "UInt8";
		}
	}
	
	public static class UInt16 extends SequenceurIntATailleFixe {
		public UInt16() {
			super(2);
		}

		@Override
		public String getNom() {
			return "UInt16";
		}
	}
	
	public static class UInt32 extends SequenceurIntATailleFixe {
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
	public Integer extraireDonnee(Desequenceur desequenceur, int parametre) {
		int valeur = 0;
		byte octet;
		
		for (int i = 0 ; i != this.nombreDOctetsUtilises ; i++) {
			octet = desequenceur.suivant();
			XMLInsecticide.ajouterXML(octet);
			valeur = (valeur * 0x100) + Byte.toUnsignedInt(octet);
		}
		
		XMLInsecticide.crocheter(valeur); 
		
		return valeur;
	}
	

	@Override
	public Integer convertirDefaut(String defaut) {
		return Integer.parseInt(defaut);
	}
}