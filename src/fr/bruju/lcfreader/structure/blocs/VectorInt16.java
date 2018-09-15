package fr.bruju.lcfreader.structure.blocs;

import java.util.Arrays;

import fr.bruju.lcfreader.sequenceur.sequences.Handler;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;
import fr.bruju.lcfreader.structure.Champ;

public class VectorInt16 implements Bloc<short[]> {
	
	public VectorInt16(String defaut) {
		
	}

	@Override
	public String getRepresentation() {
		return "Vector<Int16> {" + "" + "}";
	}


	@Override
	public String valueToString(short[] values) {
		return Arrays.toString(values);
	}

	@Override
	public Handler<short[]> getHandler(Champ<short[]> champ, int tailleLue, BaseDeDonneesDesStructures codes) {
		return null;
	}
	
}
