package random;

import java.util.List;
import java.util.Map;

public class Codes {
	Map<String, Champs> structures;
	
	
	
	
	public static class Champs {
		public List<Champ> champs;
		
		
	}
	
	public static class Champ {
		public byte index;
		public String nom;
		public boolean sized;
		public Data donnee;
	}
	
	public static interface Data {
		
		
		
	}
}
