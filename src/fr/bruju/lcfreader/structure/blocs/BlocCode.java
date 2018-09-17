package fr.bruju.lcfreader.structure.blocs;

import fr.bruju.lcfreader.debug.Logger;
import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.sequenceur.sequences.ConvertisseurOctetsVersDonnees;
import fr.bruju.lcfreader.sequenceur.sequences.SequenceurLCFAEtat;
import fr.bruju.lcfreader.structure.Donnee;

public class BlocCode extends Bloc<EnsembleDeDonnees> {
	private String nomStructure;
	
	

	public BlocCode(String nomStructure) {
		this.nomStructure = nomStructure;
	}

	@Override
	public String getRepresentation() {
		return "#" + nomStructure;
	}
	
	@Override
	public String valueToString(EnsembleDeDonnees value) {
		return value.getRepresentation();
	}

	@Override
	public void afficherSousArchi(int niveau, EnsembleDeDonnees value) {
		value.afficherArchi(niveau);
	}

	
	@Override
	public ConvertisseurOctetsVersDonnees<EnsembleDeDonnees> getHandler(int tailleLue) {
		return new H();
	}
	
	public class H implements ConvertisseurOctetsVersDonnees<EnsembleDeDonnees> {
		private SequenceurLCFAEtat sequenceur;
		
		public H() {
			sequenceur = SequenceurLCFAEtat.instancier(new EnsembleDeDonnees(nomStructure));
		}

		@Override
		public Donnee<EnsembleDeDonnees> accumuler(byte octet) {
			return sequenceur.lireOctet(octet) ? null : new Donnee<>(BlocCode.this, sequenceur.getResultat());
		}
	}

	@Override
	public ConvertisseurOctetsVersDonnees<EnsembleDeDonnees> getHandlerEnSerie() {
		return null;
	}
	

}
