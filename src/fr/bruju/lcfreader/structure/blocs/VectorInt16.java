package fr.bruju.lcfreader.structure.blocs;

import java.util.Arrays;

import fr.bruju.lcfreader.sequenceur.sequences.Handler;
import fr.bruju.lcfreader.structure.Data;

public class VectorInt16 extends Bloc<short[]> {
	
	@Override
	public String getRepresentation() {
		return "Vector<Int16> {" + "" + "}";
	}


	@Override
	public String valueToString(short[] values) {
		return Arrays.toString(values);
	}

	@Override
	public Handler<short[]> getHandler(int tailleLue) {
		return new H(tailleLue);
	}
	
	
	public class H implements Handler<short[]> {
		private short[] donnees;
		private int index = 0;
		private int acc = -1;

		public H(int tailleLue) {
			this.donnees = new short[tailleLue / 2];
		}

		@Override
		public Data<short[]> traiter(byte octet) {
			if (acc == -1) {
				acc = Byte.toUnsignedInt(octet);
				return null;
			} else {
				donnees[index++] = (short) (acc + Byte.toUnsignedInt(octet) * 0x100);
				
				acc = -1;
				return index == donnees.length ? new Data<short[]>(VectorInt16.this, donnees) : null;
			}
		}

	}

}
