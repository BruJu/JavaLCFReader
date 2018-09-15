package fr.bruju.lcfreader.sequenceur.sequences;

import fr.bruju.lcfreader.debug.BytePrinter;
import fr.bruju.lcfreader.modele.DonneesLues;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;
import fr.bruju.lcfreader.structure.Champ;
import fr.bruju.lcfreader.structure.Data;

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
		if (etat == null) {
			return false;
		}
		
		switch (etat) {
		case LIRE_BYTES:
			BytePrinter.printByte(octet, '~');
			etat = traiterLire(octet);
			break;
		case LIRE_BYTES_A_TAILLE_INCONNUE:
			BytePrinter.printByte(octet, '?');
			etat = traiterLireInconnu(octet);
			break;
		case LIRE_CODE:
			
			if (octet == 0) {
				BytePrinter.printByte(octet, '©');
				return false;
			}

			BytePrinter.printByte(octet, 'C');
			etat = lireCode(octet);
			break;
		case LIRE_NOMBRE_DE_BYTES:
			BytePrinter.printByte(octet, 'N');
			etat = lireNbOctets(octet);
			break;
		}
		
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
		
		if (champ.sized) {
			return Etat.LIRE_BYTES_A_TAILLE_INCONNUE;
		} else {
			nbOctets = 0;
			return Etat.LIRE_NOMBRE_DE_BYTES;
		}
	}
	
	private Etat lireNbOctets(byte octet) {
		nbOctets = nbOctets * 0x80 + (octet & 0x7F);
		
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
