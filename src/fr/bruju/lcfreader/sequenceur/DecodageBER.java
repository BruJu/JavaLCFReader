package fr.bruju.lcfreader.sequenceur;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @author Bruju
 *
 */
public class DecodageBER implements LecteurDeSequence<String> {
	private int etape = 0;
	
	private int type;
	private int taille;
	private List<Byte> donnees = new ArrayList<>();
	
	private NombreBER lectureNombre;
	int skip = 0;
	
	@Override
	public boolean lireOctet(byte octet) {
		if (etape == 0) {
			type = octet;
			
			lectureNombre = new NombreBER();
			etape = 1;
			return true;
		} else if (etape == 1) {
			boolean s = lectureNombre.lireOctet(octet);
			
			if (!s) {
				taille = lectureNombre.getResultat().intValue();
				etape = 2;
				
				return taille != 0;
			}
			return true;
		} else if (etape == 2) {
			donnees.add(octet);
			
			return skip++ == taille;
		}
		
		return false;
	}

	@Override
	public String getResultat() {
		String s= type + "/" + String.format("%02X",type) + " : " + taille + "[";
		
		s += donnees.stream().map(b -> b.toString()).collect(Collectors.joining(" "));
		
		s += "]";
		return s;
	}

}
