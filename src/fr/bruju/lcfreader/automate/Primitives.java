package fr.bruju.lcfreader.automate;

import java.util.List;


interface GestionnaireDePrimitives {
	public Integer consommer(List<Integer> liste, byte octet);
	public void vider();
}


abstract class GestionnaireATailleFixe implements GestionnaireDePrimitives {
	private byte[] accumulateur;
	private int i;
	
	public GestionnaireATailleFixe(int taille) {
		accumulateur = new byte[taille];
		i = 0;
	}
	
	@Override
	public Integer consommer(List<Integer> liste, byte octet) {
		accumulateur[i++] = octet;
		
		if (i == accumulateur.length) {
			liste.add(transformerAccumulateur(accumulateur));
			i = 0;
		}
		
		return null;
	}
	
	protected abstract Integer transformerAccumulateur(byte[] accumulateur);
	
	@Override
	public void vider() {
		if (i != 0)
			throw new RuntimeException("Lecture incomplète");
	}
}

class GestionnaireATailleFixeNormal extends GestionnaireATailleFixe {
	public GestionnaireATailleFixeNormal(int taille) {
		super(taille);
	}

	@Override
	protected Integer transformerAccumulateur(byte[] accumulateur) {
		int valeur = 0;
		for (int i = 0 ; i != accumulateur.length ; i++) {
			valeur = valeur * 0x100 + Byte.toUnsignedInt(accumulateur[i]);
		}
		
		return valeur;
	}
}

class GestionnaireInt16 extends GestionnaireATailleFixe {
	public GestionnaireInt16() {
		super(2);
	}

	@Override
	protected Integer transformerAccumulateur(byte[] accumulateur) {
		return (Byte.toUnsignedInt(accumulateur[0]) + Byte.toUnsignedInt(accumulateur[1]) * 0x100);
	}
}



