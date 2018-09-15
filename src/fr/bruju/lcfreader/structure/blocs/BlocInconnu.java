package fr.bruju.lcfreader.structure.blocs;

import java.util.Arrays;

import fr.bruju.lcfreader.sequenceur.sequences.Handler;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;
import fr.bruju.lcfreader.structure.Champ;
import fr.bruju.lcfreader.structure.Data;

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
	public String valueToString(byte[] value) {
		return value.length + ">" + Arrays.toString(value);
	}

	public static class BlocHandler implements Handler<byte[]> {
		private Champ<byte[]> champ;
		private byte[] octets;
		private int i;

		public BlocHandler(Champ<byte[]> champ, int tailleLue) {
			this.champ = champ;
			octets = new byte[tailleLue];
			i = 0;
		}

		@Override
		public Data<byte[]> traiter(byte octet) {
			octets[i++] = octet;
			
			return (i == octets.length) ? new Data<byte[]>(champ, octets) : null;
		}
	}

	@Override
	public Handler<byte[]> getHandler(Champ<byte[]> champ, int tailleLue, BaseDeDonneesDesStructures codes) {
		return new BlocHandler(champ, tailleLue);
	}
	
	
}