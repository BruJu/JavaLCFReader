package fr.bruju.lcfreader.structure.blocs;

import fr.bruju.lcfreader.sequenceur.sequences.Handler;
//import fr.bruju.lcfreader.structure.Champ;
import fr.bruju.lcfreader.structure.Champ;

public abstract class Bloc<T> {

	public Champ champ;
	
	public static Bloc<?> instancier(String[] donnees) {
		String nom = donnees[1];
		boolean sized = donnees[2].equals("t");
		String type = donnees[3];
		
		if (donnees[4].equals(""))
			return null;
		int index = Integer.decode(donnees[4]);
		
		Bloc<?> bloc = Bloc.genererBloc(type, donnees[5]);

		bloc.champ = new Champ(index, nom, sized);
		
		return bloc;
	}
	
	static Bloc<?> genererBloc(String type, String defaut) {
		if (type.startsWith("Array<") && type.endsWith(">")) {
			Bloc<?> bloc = BlocArray.essayer(type);
			
			
			
			if (bloc != null)
				return bloc;
		}
		
		switch (type) {
		
		case "Int32":
			return new BlocInt32(defaut);
		case "Vector<Int16>":
			return new VectorInt16(defaut);
			
		default:
			return new BlocInconnu(type);
		}
	}
	
	public final String getTrueRepresetantion() {
		return champ.getRepresentation(getRepresentation());
	}
	
	public abstract String getRepresentation();

	public T defaut() {
		return null;
	}

	public abstract String valueToString(T value);

	public abstract Handler<T> getHandler(int tailleLue);
	
}