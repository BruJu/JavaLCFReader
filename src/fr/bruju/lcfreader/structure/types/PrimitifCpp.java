package fr.bruju.lcfreader.structure.types;

import java.util.HashMap;
import java.util.Map;

public interface PrimitifCpp {
	public String getNom();
	public int getNombreDOctets();
	public int convertir(byte[] octets);
	
	
	public static Map<String, PrimitifCpp> map = remplirHashMap(
			new PrimitifCpp[] {
					new Int16()
					
			});

	
	public static Map<String, PrimitifCpp> remplirHashMap(PrimitifCpp[] primitifCpps) {
		HashMap<String, PrimitifCpp> map = new HashMap<>();
		
		for (PrimitifCpp primitif : primitifCpps) {
			map.put(primitif.getNom(), primitif);
		}
		
		return map;
	}
	
	
	
	public class Int16 implements PrimitifCpp {
		@Override
		public String getNom() {
			return "Int16";
		}

		@Override
		public int getNombreDOctets() {
			return 2;
		}

		@Override
		public int convertir(byte[] octets) {
			// TODO : corriger pour les nombres négatifs
			return (Byte.toUnsignedInt(octets[0]) + Byte.toUnsignedInt(octets[1]) * 0x100);
		}
	}
	
	public class UInt32 implements PrimitifCpp {
		@Override
		public String getNom() {
			return "UInt32";
		}

		@Override
		public int getNombreDOctets() {
			return 0;
		}

		@Override
		public int convertir(byte[] octets) {
			return 0;
		}
		
		
	}




}
