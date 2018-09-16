package fr.bruju.lcfreader.structure.blocs;

import java.util.Arrays;

import fr.bruju.lcfreader.sequenceur.sequences.ConvertisseurOctetsVersDonnees;
import fr.bruju.lcfreader.structure.Donnee;

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
	public ConvertisseurOctetsVersDonnees<byte[]> getHandler(int tailleLue) {
		return new BlocHandler(tailleLue);
	}

	public class BlocHandler implements ConvertisseurOctetsVersDonnees<byte[]> {
		private byte[] octets;
		private int i;

		public BlocHandler(int tailleLue) {
			octets = new byte[tailleLue];
			i = 0;
		}

		@Override
		public Donnee<byte[]> accumuler(byte octet) {
			octets[i++] = octet;
			
			return (i == octets.length) ? new Donnee<byte[]>(BlocInconnu.this, octets) : null;
		}
	}

	@Override
	public ConvertisseurOctetsVersDonnees<byte[]> getHandlerEnSerie() {
		return null;
	}
}