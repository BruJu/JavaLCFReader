package fr.bruju.lcfreader.structure;

public class Champ {
	public final int index;
	public final String nom;
	public final boolean sized;
	
	public Champ(int index, String nom, boolean sized) {
		this.index = index;
		this.nom = nom;
		this.sized = sized;
	}
	
	public String getRepresentation(String representationBloc) {
		return String.format("%02X", index) + " " + nom + " " + representationBloc + " " + sized;
	}
}