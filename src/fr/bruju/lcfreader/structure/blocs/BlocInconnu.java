package fr.bruju.lcfreader.structure.blocs;


import fr.bruju.lcfreader.debug.BytePrinter;
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
		return value.length + ">" + BytePrinter.getTable(value);
	}
	

	@Override
	public ConvertisseurOctetsVersDonnees<byte[]> getHandler(int tailleLue) {
		return new BlocHandler(tailleLue);
	}

	public class BlocHandler implements ConvertisseurOctetsVersDonnees<byte[]> {
		private byte[] octets;
		private int i;

		public BlocHandler(int tailleLue) {
			if (tailleLue == -1) {
				throw new RuntimeException("Bloc inconnu en lecture en série détecté : " + type);
			}
			
			octets = new byte[tailleLue];
			i = 0;
		}

		@Override
		public Donnee<byte[]> accumuler(byte octet) {
			octets[i++] = octet;
			
			return (i == octets.length) ? new Donnee<byte[]>(BlocInconnu.this, octets) : null;
		}
	}
}