package fr.bruju.lcfreader.structure.types;

import fr.bruju.lcfreader.structure.modele.Desequenceur;

/**
 * Un type primitif dont le but est de lire un nombre fixe d'octets comme si ils représentaient un seul nombre écrit en
 * big endian.
 * 
 * @author Bruju
 *
 */
public abstract class SequenceurIntATailleFixe implements PrimitifCpp {
	/** Nombre d'octets à lire */
	private final int nombreDOctetsUtilises;
	
	/**
	 * Crée un séquenceur d'entiers à taille fixe
	 * @param nombreDOctetsUtilises Le nombre d'octets à lire
	 */
	public SequenceurIntATailleFixe(int nombreDOctetsUtilises) {
		this.nombreDOctetsUtilises = nombreDOctetsUtilises;
	}

	@Override
	public Integer extraireDonnee(Desequenceur desequenceur, int parametre) {
		int valeur = 0;
		byte octet;
		
		for (int i = 0 ; i != this.nombreDOctetsUtilises ; i++) {
			octet = desequenceur.suivant();
			valeur = (valeur * 0x100) + Byte.toUnsignedInt(octet);
		}
		
		return valeur;
	}
	
	
	/* ===============
	 * IMPLEMENTATIONS
	 * =============== */
	
	/** Booléens (0 = vrai, 1 = faux) */
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
	
	/** Nombre de un octet */
	public static class UInt8 extends SequenceurIntATailleFixe {
		public UInt8() {
			super(1);
		}

		@Override
		public String getNom() {
			return "UInt8";
		}
	}

	/** Nombre de deux octets */
	public static class UInt16 extends SequenceurIntATailleFixe {
		public UInt16() {
			super(2);
		}

		@Override
		public String getNom() {
			return "UInt16";
		}
	}

	/** Nombre de quatre octets */
	public static class UInt32 extends SequenceurIntATailleFixe {
		public UInt32() {
			super(4);
		}

		@Override
		public String getNom() {
			return "UInt32";
		}
	}
}