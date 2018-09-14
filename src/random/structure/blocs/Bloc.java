package random.structure.blocs;


public interface Bloc<T> {

	static Bloc<?> genererBloc(String type, String defaut) {
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