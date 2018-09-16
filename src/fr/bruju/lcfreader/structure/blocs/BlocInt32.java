package fr.bruju.lcfreader.structure.blocs;

import fr.bruju.lcfreader.sequenceur.sequences.Handler;
import fr.bruju.lcfreader.sequenceur.sequences.NombreBER;
import fr.bruju.lcfreader.structure.Champ;
import fr.bruju.lcfreader.structure.Data;

public class BlocInt32 extends Bloc<Integer> {
	private Integer defaut;
	
	public BlocInt32(String defaut) {
		if (defaut.equals("")) {
			this.defaut = null;
		} else {
			if (defaut.contains("|")) {
				defaut = defaut.split("|")[1];
			}
			
			
			this.defaut = Integer.parseInt(defaut);
		}
	}
	
	@Override
	public Integer defaut() {
		return defaut;
	}

	@Override
	public String getRepresentation() {
		return "Integer(" + defaut + ")";
	}

	@Override
	public String valueToString(Integer value) {
		return value.toString();
	}

	@Override
	public Handler<Integer> getHandler(int tailleLue) {
		return new H();
	}
	
	
	public class H implements Handler<Integer> {
		
		private NombreBER accumulateur;

		public H() {
			accumulateur = new NombreBER();
		}

		@Override
		public Data<Integer> traiter(byte octet) {
			boolean b = accumulateur.lireOctet(octet);
			
			return b ? null : new Data<>(BlocInt32.this, accumulateur.getResultat().intValue());
		}
	}
}