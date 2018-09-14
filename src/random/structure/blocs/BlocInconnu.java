package random.structure.blocs;

import java.util.Arrays;

public class BlocInconnu implements Bloc<byte[]> {
	public final String type;
	
	public BlocInconnu(String type) {
		this.type = type;
	}
	
	@Override
	public String getRepresentation() {
		return "Inconnu {" + type +"}";
	}

	@Override
	public byte[] convertir(byte[] bytes) {
		return bytes;
	}

	@Override
	public String valueToString(byte[] value) {
		return Arrays.toString(value);
	}
}