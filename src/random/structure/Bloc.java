package random.structure;


public interface Bloc<T> {

	static Bloc<?> genererBloc(String type, String defaut) {
		switch (type) {
		case "Int32":
			return new BlocInt32(defaut);
		
		
		default:
			return new BlocInconnu();
		}
	}

	public String getRepresentation();

	public T convertir(byte[] bytes);

	public default T defaut() {
		return null;
	}
	
	public static class BlocInconnu implements Bloc<byte[]> {
		@Override
		public String getRepresentation() {
			return "Inconnu";
		}

		@Override
		public byte[] convertir(byte[] bytes) {
			return bytes;
		}
	}
	
	
	public static class BlocInt32 implements Bloc<Integer> {
		private Integer defaut;
		
		public BlocInt32(String defaut) {
			if (defaut.equals("")) {
				this.defaut = null;
			} else {
				if (defaut.contains("|")) {
					defaut = defaut.split("|")[1];
				}
				
				
				this.defaut = Integer.parseInt(defaut);
			}
		}
		
		@Override
		public Integer convertir(byte[] bytes) {
			int valeur = 0;
			
			for (byte octet : bytes) {
				valeur = valeur * 0x80 + (octet & 0x7F);
			}
			
			return valeur;
		}

		@Override
		public Integer defaut() {
			return defaut;
		}

		@Override
		public String getRepresentation() {
			return "Integer(" + defaut + ")";
		}
	}
	
}