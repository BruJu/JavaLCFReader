package fr.bruju.lcfreader.structure.blocs;

import java.util.Arrays;

import fr.bruju.lcfreader.sequenceur.sequences.Handler;
import fr.bruju.lcfreader.structure.Data;
import fr.bruju.lcfreader.structure.types.PrimitifCpp;

public class VectorInt16 extends Bloc<int[]> {
	
	@Override
	public String getRepresentation() {
		return "Vector<Int16> {" + "" + "}";
	}


	@Override
	public String valueToString(int[] values) {
		return Arrays.toString(values);
	}

	@Override
	public Handler<int[]> getHandler(int tailleLue) {
		return new H(tailleLue);
	}
	
	
	public class H implements Handler<int[]> {
		PrimitifCpp primitif = PrimitifCpp.map.get("Int16");
		
		private int[] donnees;
		private int iDonnees = 0;
		
		private byte[] octets;
		private int iByte;

		public H(int tailleLue) {
			this.octets = new byte[primitif.getNombreDOctets()];
			this.iByte = 0;
			
			this.donnees = new int[tailleLue / primitif.getNombreDOctets()];
		}

		@Override
		public Data<int[]> traiter(byte octet) {
			octets[iByte++] = octet;
			
			if (iByte == octets.length) {
				iByte = 0;
				
				donnees[iDonnees++] = primitif.convertir(octets);
				
				if (iDonnees == donnees.length) {
					return new Data<int[]>(VectorInt16.this, donnees);
				}
				
			}
			
			return null;
		}

	}

}
