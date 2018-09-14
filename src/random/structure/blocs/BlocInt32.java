package random.structure.blocs;

public class BlocInt32 implements Bloc<Integer> {
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

	@Override
	public String valueToString(Integer value) {
		return value.toString();
	}
}