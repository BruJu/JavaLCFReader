package random.sequenceur;

import random.DonneesLues;
import random.structure.BaseDeDonneesDesStructures;
import random.structure.Champ;
import random.structure.Data;

public class SequenceurViaLCF implements LecteurDeSequence<Void> {
	private DonneesLues data;
	private BaseDeDonneesDesStructures codes;
	private Etat etat = Etat.LIRE_CODE;
	
	private Champ<?> champ;
	private int nbOctets;
	private int iOctet;
	private byte[] bytes;
	

	public SequenceurViaLCF(DonneesLues data, BaseDeDonneesDesStructures codes) {
		this.data = data;
		this.codes = codes;
	}

	@Override
	public boolean lireOctet(byte octet) {
		System.out.print("[" + etat + "] " + String.format("%02X", octet) + " - " + octet);
		
		if (etat == null) {
			return false;
		}
		
		switch (etat) {
		case LIRE_BYTES:
			etat = traiterLire(octet);
			break;
		case LIRE_BYTES_A_TAILLE_INCONNUE:
			etat = traiterLireInconnu(octet);
			break;
		case LIRE_CODE:
			etat = lireCode(octet);
			break;
		case LIRE_NOMBRE_DE_BYTES:
			etat = lireNbOctets(octet);
			break;
		}
		
		System.out.println();
		
		return true;
	}


	private Etat lireCode(byte octet) {
		if (octet == 0)
			return etat;
		
		Champ<?> champ = codes.structures.get(data.nomStruct).trouverChampIndex(octet); 
		
		if (champ == null) {
			System.out.println("\nChamp non trouvé : " + octet);
			return null;
		}
		
		this.champ = champ;
		
		System.out.print(" " + champ.getRepresentation());
		
		if (champ.sized) {
			return Etat.LIRE_BYTES_A_TAILLE_INCONNUE;
		} else {
			nbOctets = 0;
			return Etat.LIRE_NOMBRE_DE_BYTES;
		}
	}
	
	private Etat lireNbOctets(byte octet) {
		nbOctets = nbOctets * 0x80 + (octet & 0x79);
		
		if ((octet & 0x80) != 0) {
			return etat;
		} else {
			iOctet = 0;
			bytes = new byte[nbOctets];
			return Etat.LIRE_BYTES;
		}
	}

	private Etat traiterLireInconnu(byte octet) {
		return null;
	}

	private Etat traiterLire(byte octet) {
		bytes[iOctet++] = octet;
		
		if (iOctet == nbOctets) {
			Data<?> blocData = champ.creerDonnees(bytes);
			
			data.push(blocData);
			
			
			return Etat.LIRE_CODE;
		} else {
			return etat;
		}
	}

	@Override
	public Void getResultat() {
		return null;
	}

	
	private enum Etat {
		LIRE_CODE,
		LIRE_NOMBRE_DE_BYTES,
		LIRE_BYTES,
		LIRE_BYTES_A_TAILLE_INCONNUE
	}
}
