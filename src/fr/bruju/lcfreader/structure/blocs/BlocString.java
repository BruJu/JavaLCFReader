package fr.bruju.lcfreader.structure.blocs;

import fr.bruju.lcfreader.sequenceur.sequences.Chaine;
import fr.bruju.lcfreader.sequenceur.sequences.ConvertisseurOctetsVersDonnees;
import fr.bruju.lcfreader.sequenceur.sequences.LecteurDeSequence;
import fr.bruju.lcfreader.sequenceur.sequences.NombreBER;
import fr.bruju.lcfreader.sequenceur.sequences.TailleChaine;
import fr.bruju.lcfreader.structure.Donnee;

/**
 * Un bloc de données concernant un int32
 * @author Bruju
 *
 */
public class BlocString extends Bloc<String> {
	/** Valeur par défaut */
	private String defaut;
	
	/**
	 * Construit le bloc contenant un entier avec la valeur par défaut donnée
	 * @param defaut La valeur par défaut. Si de la forme a|b, prend b.
	 */
	public BlocString(String defaut) {
		this.defaut = defaut.equals("") ? null : defaut;
	}
	
	@Override
	public String defaut() {
		return defaut;
	}

	@Override
	public String getRepresentation() {
		return "String(" + defaut + ")";
	}

	@Override
	public ConvertisseurOctetsVersDonnees<String> getHandler(int tailleLue) {
		return new H(new Chaine(tailleLue));
	}

	@Override
	public ConvertisseurOctetsVersDonnees<String> getHandlerEnSerie() {
		return new H(new TailleChaine());
	}

	
	public class H implements ConvertisseurOctetsVersDonnees<String> {
		
		public LecteurDeSequence<String> sequenceur;
		
		public H(LecteurDeSequence<String> sequenceur) {
			this.sequenceur = sequenceur;
		}
		
		@Override
		public Donnee<String> accumuler(byte octet) {
			return sequenceur.lireOctet(octet) ? null : new Donnee<>(BlocString.this, sequenceur.getResultat());
		}
	}

}