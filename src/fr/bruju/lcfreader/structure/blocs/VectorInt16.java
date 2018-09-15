package fr.bruju.lcfreader.structure.blocs;

import java.util.Arrays;

import fr.bruju.lcfreader.sequenceur.sequences.Handler;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;
import fr.bruju.lcfreader.structure.Champ;
import fr.bruju.lcfreader.structure.Data;

public class VectorInt16 implements Bloc<short[]> {
	
	public VectorInt16(String defaut) {
		
	}

	@Override
	public String getRepresentation() {
		return "Vector<Int16> {" + "" + "}";
	}


	@Override
	public String valueToString(short[] values) {
		return Arrays.toString(values);
	}

	@Override
	public Handler<short[]> getHandler(Champ<short[]> champ, int tailleLue, BaseDeDonneesDesStructures codes) {
		return new H(champ, tailleLue);
	}
	
	
	public static class H implements Handler<short[]> {
		private Champ<short[]> champ;
		private short[] donnees;
		private int index = 0;
		private int acc = -1;

		public H(Champ<short[]> champ, int tailleLue) {
			this.champ = champ;
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
				return index == donnees.length ? new Data<short[]>(champ, donnees) : null;
			}
		}

	}

}
