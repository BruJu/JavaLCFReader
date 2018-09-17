package fr.bruju.lcfreader.structure.blocs;

import fr.bruju.lcfreader.debug.Logger;
import fr.bruju.lcfreader.sequenceur.sequences.ConvertisseurOctetsVersDonnees;
import fr.bruju.lcfreader.sequenceur.sequences.NombreBER;
import fr.bruju.lcfreader.structure.Donnee;

/**
 * Un bloc de données concernant un int32
 * @author Bruju
 *
 */
public class BlocInt32 extends Bloc<Integer> {
	/** Valeur par défaut */
	private Integer defaut;
	
	/**
	 * Construit le bloc contenant un entier avec la valeur par défaut donnée
	 * @param defaut La valeur par défaut. Si de la forme a|b, prend b.
	 */
	public BlocInt32(String defaut) {
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
	public ConvertisseurOctetsVersDonnees<Integer> getHandler(int tailleLue) {
		return new H();
	}
	
	public class H implements ConvertisseurOctetsVersDonnees<Integer> {
		
		private NombreBER accumulateur;

		public H() {
			accumulateur = new NombreBER();
		}

		@Override
		public Donnee<Integer> accumuler(byte octet) {
			Logger.octet(octet);
			boolean b = accumulateur.lireOctet(octet);
			
			return b ? null : new Donnee<>(BlocInt32.this, accumulateur.getResultat().intValue());
		}
	}

	@Override
	public ConvertisseurOctetsVersDonnees<Integer> getHandlerEnSerie() {
		return new H();
	}
}