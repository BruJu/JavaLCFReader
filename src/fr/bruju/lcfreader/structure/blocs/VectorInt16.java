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
		
		private int[] nombres;
		private int iNombres = 0;
		
		private byte[] octetsNombreEnCours;
		private int iOctetNombreEnCours;

		public H(int tailleLue) {
			this.octetsNombreEnCours = new byte[primitif.getNombreDOctets()];
			this.iOctetNombreEnCours = 0;
			
			this.nombres = new int[tailleLue / primitif.getNombreDOctets()];
		}

		@Override
		public Data<int[]> traiter(byte octetRecu) {
			octetsNombreEnCours[iOctetNombreEnCours++] = octetRecu;
			
			if (iOctetNombreEnCours == octetsNombreEnCours.length) {
				iOctetNombreEnCours = 0;
				
				nombres[iNombres++] = primitif.convertir(octetsNombreEnCours);
				
				if (iNombres == nombres.length) {
					return new Data<int[]>(VectorInt16.this, nombres);
				}
			}
			
			return null;
		}

	}

}
