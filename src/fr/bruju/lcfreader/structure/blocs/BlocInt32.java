package fr.bruju.lcfreader.structure.blocs;

import fr.bruju.lcfreader.sequenceur.sequences.Handler;
import fr.bruju.lcfreader.sequenceur.sequences.NombreBER;
import fr.bruju.lcfreader.structure.Champ;
import fr.bruju.lcfreader.structure.Data;

public class BlocInt32 implements Bloc<Integer> {
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
	public Handler<Integer> getHandler(Champ<Integer> champ, int tailleLue) {
		return new H(champ);
	}
	
	
	public class H implements Handler<Integer> {
		private Champ<Integer> champ;
		
		private NombreBER accumulateur;

		public H(Champ<Integer> champ) {
			this.champ = champ;
			accumulateur = new NombreBER();
		}

		@Override
		public Data<Integer> traiter(byte octet) {
			boolean b = accumulateur.lireOctet(octet);
			
			return b ? null : new Data<>(champ, accumulateur.getResultat().intValue());
		}
	}
}