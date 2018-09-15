package fr.bruju.lcfreader.structure.blocs;

import java.util.ArrayList;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.modele.DonneesLues;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;
import fr.bruju.lcfreader.structure.Structure;

public class BlocArray implements Bloc<ArrayList<DonneesLues>> {

	private Structure structure;

	public BlocArray(Structure structure) {
		this.structure = structure;
	}

	@Override
	public String getRepresentation() {
		return "TableauDeDonnees";
	}

	public static Bloc<?> essayer(String type, BaseDeDonneesDesStructures codes) {
		String vraiType = type.substring(6, -1); // Array<X>
		
		Structure structure = codes.structures.get(vraiType);
		
		return structure == null ? null : new BlocArray(structure);
	}

	@Override
	public ArrayList<DonneesLues> convertir(byte[] bytes) {
		
		
		
		
		
		return null;
	}

	@Override
	public String valueToString(ArrayList<DonneesLues> value) {
		return "[" + value.stream().map(data -> dataToString(data)).collect(Collectors.joining(" ; ")) + "]";
	}
	
	private String dataToString(DonneesLues data) {
		return data.getRepresentation();
	}

}
