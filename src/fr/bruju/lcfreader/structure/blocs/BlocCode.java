package fr.bruju.lcfreader.structure.blocs;

import fr.bruju.lcfreader.modele.DonneesLues;
import fr.bruju.lcfreader.sequenceur.sequences.Handler;
import fr.bruju.lcfreader.sequenceur.sequences.SequenceurLCFAEtat;
import fr.bruju.lcfreader.structure.Data;

public class BlocCode extends Bloc<DonneesLues> {
	private String nomStructure;
	
	

	public BlocCode(String nomStructure) {
		this.nomStructure = nomStructure;
	}

	@Override
	public String getRepresentation() {
		return "#" + nomStructure;
	}
	
	@Override
	public String valueToString(DonneesLues value) {
		return value.getRepresentation();
	}

	@Override
	public void afficherSousArchi(int niveau, DonneesLues value) {
		value.afficherArchi(niveau);
	}

	
	@Override
	public Handler<DonneesLues> getHandler(int tailleLue) {
		return new H();
	}
	
	public class H implements Handler<DonneesLues> {
		private SequenceurLCFAEtat sequenceur;
		
		public H() {
			sequenceur = new SequenceurLCFAEtat(new DonneesLues(nomStructure));
		}

		@Override
		public Data<DonneesLues> traiter(byte octet) {
			if (sequenceur.lireOctet(octet)) {
				return null;
			} else {
				return new Data<>(BlocCode.this, sequenceur.data);
			}
		}
	}
}
