package fr.bruju.lcfreader.structure.blocs;

import java.util.Arrays;

import fr.bruju.lcfreader.sequenceur.sequences.Handler;
import fr.bruju.lcfreader.structure.Data;

public class BlocInconnu extends Bloc<byte[]> {
	public final String type;
	
	public BlocInconnu(String type) {
		this.type = type;
	}
	
	@Override
	public String getRepresentation() {
		return "Inconnu {" + type +"}";
	}

	@Override
	public String valueToString(byte[] value) {
		return value.length + ">" + Arrays.toString(value);
	}
	

	@Override
	public Handler<byte[]> getHandler(int tailleLue) {
		return new BlocHandler(tailleLue);
	}

	public class BlocHandler implements Handler<byte[]> {
		private byte[] octets;
		private int i;

		public BlocHandler(int tailleLue) {
			octets = new byte[tailleLue];
			i = 0;
		}

		@Override
		public Data<byte[]> traiter(byte octet) {
			octets[i++] = octet;
			
			return (i == octets.length) ? new Data<byte[]>(BlocInconnu.this, octets) : null;
		}
	}
}