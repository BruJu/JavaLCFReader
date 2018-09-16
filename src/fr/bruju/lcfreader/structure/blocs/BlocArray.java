package fr.bruju.lcfreader.structure.blocs;

import java.util.TreeMap;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.modele.DonneesLues;
import fr.bruju.lcfreader.sequenceur.sequences.Handler;
import fr.bruju.lcfreader.sequenceur.sequences.SequenceurLCFAEtat;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;
import fr.bruju.lcfreader.structure.Data;
import fr.bruju.lcfreader.structure.Structure;

public class BlocArray extends Bloc<TreeMap<Integer, DonneesLues>> {
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
	public String valueToString(TreeMap<Integer, DonneesLues> value) {
		return "[" + value.values().stream().map(data -> dataToString(data)).collect(Collectors.joining(" ; ")) + "]";
	}
	
	private String dataToString(DonneesLues data) {
		return data.getRepresentation();
	}

	@Override
	public Handler<TreeMap<Integer, DonneesLues>> getHandler(int tailleLue) {
		return new H();
	}
	
	public void afficherSousArchi(int niveau, TreeMap<Integer, DonneesLues> value) {
		value.values().forEach(data -> data.afficherArchi(niveau));
		
	}
	
	
	public class H implements Handler<TreeMap<Integer, DonneesLues>> {

		private int nombreDElements;
		
		private Etat etat;
		private TreeMap<Integer, DonneesLues> map;
		
		private SequenceurLCFAEtat sequenceur;
		

		public H() {
			etat = Etat.LireTaille;
		}

		@Override
		public Data<TreeMap<Integer, DonneesLues>> traiter(byte octet) {
			switch (etat) {
			case LireTaille:
				nombreDElements = octet;
				map = new TreeMap<Integer, DonneesLues>();
				etat = Etat.LireIndex;
				break;
			case LireIndex:
				DonneesLues donneeCourante = new DonneesLues(nomStructure);
				map.put((int) octet, donneeCourante);
				sequenceur = new SequenceurLCFAEtat(donneeCourante);
				etat = Etat.LireDonnees;
				break;
			case LireDonnees:
				if (!sequenceur.lireOctet(octet)) {
					if (map.size() == nombreDElements) {
						return new Data<>(BlocArray.this, map);
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
	};
}
