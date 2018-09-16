package fr.bruju.lcfreader.structure.blocs;

import fr.bruju.lcfreader.sequenceur.sequences.Handler;
import fr.bruju.lcfreader.sequenceur.sequences.NombreBER;
import fr.bruju.lcfreader.structure.Data;

/**
 * Un bloc de données concernant un int32
 * @author Bruju
 *
 */
public class BlocString extends Bloc<Integer> {
	/** Valeur par défaut */
	private Integer defaut;
	
	/**
	 * Construit le bloc contenant un entier avec la valeur par défaut donnée
	 * @param defaut La valeur par défaut. Si de la forme a|b, prend b.
	 */
	public BlocString(String defaut) {
		if (!defaut.equals("")) {
			// On prend la valeur par défaut pour RPG Maker 2003
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
			
			return b ? null : new Data<>(BlocString.this, accumulateur.getResultat().intValue());
		}
	}
}