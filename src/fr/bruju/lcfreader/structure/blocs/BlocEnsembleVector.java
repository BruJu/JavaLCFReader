package fr.bruju.lcfreader.structure.blocs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.sequenceur.sequences.ConvertisseurOctetsVersDonnees;
import fr.bruju.lcfreader.sequenceur.sequences.SequenceurLCFAEtat;
import fr.bruju.lcfreader.structure.Donnee;

public class BlocEnsembleVector extends Bloc<List<EnsembleDeDonnees>> {

	private String nomStructure;

	public BlocEnsembleVector(String nomStructure) {
		this.nomStructure = nomStructure;
	}
	
	@Override
	public String getRepresentation() {
		return "Vector<#" + nomStructure + ">";
	}
	
	@Override
	public String valueToString(List<EnsembleDeDonnees> value) {
		return "[" + value.stream().map(data -> dataToString(data)).collect(Collectors.joining(" ; ")) + "]";
	}
	
	private String dataToString(EnsembleDeDonnees data) {
		return data.getRepresentation();
	}
	
	@Override
	public void afficherSousArchi(int niveau, List<EnsembleDeDonnees> value) {
		value.forEach(data -> data.afficherArchi(niveau));
	}
	
	@Override
	public ConvertisseurOctetsVersDonnees<List<EnsembleDeDonnees>> getHandler(int tailleLue) {
		return new H(tailleLue);
	}
	
	public class H implements ConvertisseurOctetsVersDonnees<List<EnsembleDeDonnees>> {
		private int taille;
		private List<EnsembleDeDonnees> ensembles;

		
		private int nbOctetsRecu = 0;
		
		private SequenceurLCFAEtat sequenceur;
		
		
		public H(int taille) {
			fournirTailles(taille);
			
		}

		@Override
		public boolean fournirTailles(Integer taille) {
			if (taille == null) {
				this.taille = -1;
				return false;
			}
			
			this.taille = taille;
			this.ensembles = new ArrayList<>(this.taille);
			
			return true;
		}
		
		@Override
		public Donnee<List<EnsembleDeDonnees>> accumuler(byte octet) {
			if (sequenceur == null) {
				sequenceur = SequenceurLCFAEtat.instancier(new EnsembleDeDonnees(nomStructure));
			}
			
			boolean seq = sequenceur.lireOctet(octet);
			
			if (!seq) {
				ensembles.add(sequenceur.getResultat());
				sequenceur = null;
			}
			
			return (taille == ++nbOctetsRecu) ? new Donnee<>(BlocEnsembleVector.this, ensembles) : null;
		}
		
		
		
		
	}

}
