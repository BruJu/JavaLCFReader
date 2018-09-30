package fr.bruju.lcfreader.structure.dispositions;


import fr.bruju.lcfreader.structure.Sequenceur;
import fr.bruju.lcfreader.structure.blocs.Bloc;
import fr.bruju.lcfreader.structure.blocs.Champ;
import fr.bruju.lcfreader.structure.blocs.mini.MiniBloc;

public interface Disposition {
	public static Disposition get(String disposition, String type) {
		switch (disposition) {
		case "":
			return new DispositionSimple();
		case "Vector":
			if (type.equals("Int32"))
				return new DispositionListe();
			
			return new DispositionVecteur();
		case "Array":
			return new DispositionTableau();
		}
		
		return null;
	}
	
	public <T> Bloc<?> decorer(Champ champ, MiniBloc<T> miniBloc);

}
