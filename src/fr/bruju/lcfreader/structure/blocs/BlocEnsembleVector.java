package fr.bruju.lcfreader.structure.blocs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
		return new H();
	}
	
	public class H implements ConvertisseurOctetsVersDonnees<List<EnsembleDeDonnees>> {
		private int taille;
		private List<EnsembleDeDonnees> ensembles;

		private SequenceurLCFAEtat sequenceur;

		@Override
		public void fournirTailles(Integer taille) {
			this.taille = taille;
			this.ensembles = new ArrayList<>(this.taille);
			System.out.println("Recoit une taille de " + taille);
		}

		@Override
		public Donnee<List<EnsembleDeDonnees>> accumuler(byte octet) {
			//if (sequenceur == )
			
			return null;
		}
		
		
		
		
	}

}
