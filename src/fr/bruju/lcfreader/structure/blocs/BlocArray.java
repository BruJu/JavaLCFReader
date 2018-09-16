package fr.bruju.lcfreader.structure.blocs;

import java.util.TreeMap;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.sequenceur.sequences.ConvertisseurOctetsVersDonnees;
import fr.bruju.lcfreader.sequenceur.sequences.SequenceurLCFAEtat;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;
import fr.bruju.lcfreader.structure.Donnee;
import fr.bruju.lcfreader.structure.Structure;

public class BlocArray extends Bloc<TreeMap<Integer, EnsembleDeDonnees>> {
	/* ======
	 * STATIC
	 * ====== */
	
	/**
	 * Crée un bloc "tableau" si le type est Array<TypeDansLaBase>
	 * @param type Le type
	 * @return Un bloc tableau si c'est pertinent
	 */
	public static Bloc<?> essayer(String type) {
		if (!type.startsWith("Array<") || !type.endsWith(">"))
			return null;
		
		String vraiType = type.substring(6, type.length() - 1); // Array<X>
		Structure structure = BaseDeDonneesDesStructures.getInstance().get(vraiType);
		return structure == null ? null : new BlocArray(vraiType);
	}
	
	
	/*
	 * 
	 */
	
	private String nomStructure;

	public BlocArray(String nomStructure) {
		this.nomStructure = nomStructure;
	}

	@Override
	public String getRepresentation() {
		return "TableauDeDonnees[" + nomStructure + "]";
	}



	@Override
	public String valueToString(TreeMap<Integer, EnsembleDeDonnees> value) {
		return "[" + value.values().stream().map(data -> dataToString(data)).collect(Collectors.joining(" ; ")) + "]";
	}
	
	private String dataToString(EnsembleDeDonnees data) {
		return data.getRepresentation();
	}

	@Override
	public ConvertisseurOctetsVersDonnees<TreeMap<Integer, EnsembleDeDonnees>> getHandler(int tailleLue) {
		return new H();
	}

	@Override
	public void afficherSousArchi(int niveau, TreeMap<Integer, EnsembleDeDonnees> value) {
		value.values().forEach(data -> data.afficherArchi(niveau));
	}
	
	
	public class H implements ConvertisseurOctetsVersDonnees<TreeMap<Integer, EnsembleDeDonnees>> {

		private int nombreDElements;
		
		private Etat etat;
		private TreeMap<Integer, EnsembleDeDonnees> map;
		
		private SequenceurLCFAEtat sequenceur;
		

		public H() {
			etat = Etat.LireTaille;
		}

		@Override
		public Donnee<TreeMap<Integer, EnsembleDeDonnees>> accumuler(byte octet) {
			switch (etat) {
			case LireTaille:
				nombreDElements = octet;
				map = new TreeMap<Integer, EnsembleDeDonnees>();
				etat = Etat.LireIndex;
				break;
			case LireIndex:
				EnsembleDeDonnees donneeCourante = new EnsembleDeDonnees(nomStructure);
				map.put((int) octet, donneeCourante);
				sequenceur = SequenceurLCFAEtat.instancier(donneeCourante);
				etat = Etat.LireDonnees;
				break;
			case LireDonnees:
				if (!sequenceur.lireOctet(octet)) {
					if (map.size() == nombreDElements) {
						return new Donnee<>(BlocArray.this, map);
					} else {
						etat = Etat.LireIndex;
					}
				}
				break;
			}
			
			return null;
		}
		
		
		
	}

	private static enum Etat {
		LireTaille,
		LireIndex,
		LireDonnees
	}

	@Override
	public ConvertisseurOctetsVersDonnees<TreeMap<Integer, EnsembleDeDonnees>> getHandlerEnSerie() {
		return null;
	};
}
