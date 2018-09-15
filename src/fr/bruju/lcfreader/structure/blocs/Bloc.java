package fr.bruju.lcfreader.structure.blocs;

import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;

public interface Bloc<T> {

	static Bloc<?> genererBloc(String type, String defaut, BaseDeDonneesDesStructures codes) {
		if (type.startsWith("Array<") && type.endsWith(">")) {
			Bloc<?> bloc = BlocArray.essayer(type, codes);
			
			if (bloc != null)
				return null;
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
	
	public String getRepresentation();

	public T convertir(byte[] bytes);

	public default T defaut() {
		return null;
	}

	public String valueToString(T value);
	
}