package fr.bruju.lcfreader.structure.blocs;

import java.util.TreeMap;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.modele.DonneesLues;
import fr.bruju.lcfreader.sequenceur.sequences.Handler;
import fr.bruju.lcfreader.sequenceur.sequences.SequenceurLCFAEtat;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;
import fr.bruju.lcfreader.structure.Champ;
import fr.bruju.lcfreader.structure.Data;
import fr.bruju.lcfreader.structure.Structure;

public class BlocArray implements Bloc<TreeMap<Integer, DonneesLues>> {
	
	private String nomStructure;

	public BlocArray(String nomStructure) {
		this.nomStructure = nomStructure;
	}

	@Override
	public String getRepresentation() {
		return "TableauDeDonnees[" + nomStructure + "]";
	}

	public static Bloc<?> essayer(String type, BaseDeDonneesDesStructures codes) {
		String vraiType = type.substring(6, type.length() - 1); // Array<X>
		
		Structure structure = codes.structures.get(vraiType);
		
		return structure == null ? null : new BlocArray(vraiType);
	}


	@Override
	public String valueToString(TreeMap<Integer, DonneesLues> value) {
		return "[" + value.values().stream().map(data -> dataToString(data)).collect(Collectors.joining(" ; ")) + "]";
	}
	
	private String dataToString(DonneesLues data) {
		return data.getRepresentation();
	}

	@Override
	public Handler<TreeMap<Integer, DonneesLues>> getHandler(Champ<TreeMap<Integer, DonneesLues>> champ, int tailleLue,
			BaseDeDonneesDesStructures codes) {
		return new H(champ, codes);
	}
	
	
	public class H implements Handler<TreeMap<Integer, DonneesLues>> {

		private Champ<TreeMap<Integer, DonneesLues>> champ;
		private BaseDeDonneesDesStructures codes;
		
		private Etat etat;
		private TreeMap<Integer, DonneesLues> map;
		
		private SequenceurLCFAEtat sequenceur;
		

		public H(Champ<TreeMap<Integer, DonneesLues>> champ, BaseDeDonneesDesStructures codes) {
			this.champ = champ;
			this.codes = codes;
			
			etat = Etat.LireTaille;
		}

		@Override
		public Data<TreeMap<Integer, DonneesLues>> traiter(byte octet) {
			switch (etat) {
			case LireTaille:
				map = new TreeMap<Integer, DonneesLues>();
				etat = Etat.LireIndex;
				System.out.println("Taille lue : " + octet);
				break;
			case LireIndex:
				if (octet == 0) {
					return new Data<>(champ, map);
				} else {
					DonneesLues donneeCourante = new DonneesLues(nomStructure);
					System.out.println(nomStructure + " push " + octet);
					map.put((int) octet, donneeCourante);
					sequenceur = new SequenceurLCFAEtat(donneeCourante, codes);
					etat = Etat.LireDonnees;
					System.out.println("<" + nomStructure +">");
				}
				break;
			case LireDonnees:
				if (!sequenceur.lireOctet(octet)) {
					System.out.println("</" + nomStructure +">");
					etat = Etat.LireIndex;
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
