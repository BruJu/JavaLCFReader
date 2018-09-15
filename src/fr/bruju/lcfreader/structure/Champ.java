package fr.bruju.lcfreader.structure;

import fr.bruju.lcfreader.structure.blocs.Bloc;

public class Champ<T> {
	public final int index;
	public final String nom;
	public final boolean sized;
	public final Bloc<T> bloc;
	
	public Champ(int index, String nom, boolean sized, Bloc<T> bloc) {
		this.index = index;
		this.nom = nom;
		this.sized = sized;
		this.bloc = bloc;
	}

	
	public String getRepresentation() {
		return String.format("%02X", index) + " " + nom + " " + bloc.getRepresentation() + " " + sized;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Champ<?> instancier(String[] donnees, BaseDeDonneesDesStructures codes) {
		String nom = donnees[1];
		boolean sized = donnees[2].equals("t");
		String type = donnees[3];
		if (donnees[4].equals(""))
			return null;
		
		int index = Integer.decode(donnees[4]);
		
		Bloc<?> bloc = Bloc.genererBloc(type, donnees[5], codes);
		
		if (bloc != null)
			return new Champ(index, nom, sized, bloc);
		else
			return null;
	}


	public Data<T> creerDonnees(byte[] bytes) {
		return new Data<T>(this, bloc.convertir(bytes));
	}
}