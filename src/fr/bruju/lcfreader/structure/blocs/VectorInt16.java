package fr.bruju.lcfreader.structure.blocs;

import java.util.Arrays;

public class VectorInt16 implements Bloc<short[]> {
	
	public VectorInt16(String defaut) {
		
	}

	@Override
	public String getRepresentation() {
		return "Vector<Int16> {" + "" + "}";
	}

	@Override
	public short[] convertir(byte[] bytes) {
		short[] tableau = new short[bytes.length / 2];
		
		for (int i = 0 ; i != tableau.length ; i++) {
			tableau[i] = (short) (Byte.toUnsignedInt(bytes[i*2 + 1]) * 0x100 + Byte.toUnsignedInt(bytes[i*2]));
		}
		
		return tableau;
	}

	@Override
	public String valueToString(short[] values) {
		return Arrays.toString(values);
	}
	
}
