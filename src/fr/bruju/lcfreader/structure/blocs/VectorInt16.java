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
		
		private int[] nombresLus;
		private int nombreEnCours = 0;
		
		private byte[] octetsEnCoursDeLecture;
		private int indiceOctetCourant;

		public H(int tailleLue) {
			this.octetsEnCoursDeLecture = new byte[primitif.getNombreDOctets()];
			this.indiceOctetCourant = 0;
			
			this.nombresLus = new int[tailleLue / primitif.getNombreDOctets()];
		}

		@Override
		public Data<int[]> traiter(byte octetRecu) {
			octetsEnCoursDeLecture[indiceOctetCourant++] = octetRecu;
			
			if (indiceOctetCourant == octetsEnCoursDeLecture.length) {
				indiceOctetCourant = 0;
				
				nombresLus[nombreEnCours++] = primitif.convertir(octetsEnCoursDeLecture);
				
				if (nombreEnCours == nombresLus.length) {
					return new Data<int[]>(VectorInt16.this, nombresLus);
				}
			}
			
			return null;
		}

	}

}
