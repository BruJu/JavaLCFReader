package fr.bruju.lcfreader.structure.types;

import java.nio.ByteOrder;
import java.util.Map;

import fr.bruju.lcfreader.structure.MiniBloc;
import fr.bruju.lcfreader.structure.modele.Desequenceur;

public interface PrimitifCpp extends MiniBloc<Integer> {

	
	public String getNom();

	public static void remplirHashMap(Map<String, MiniBloc<?>> map, PrimitifCpp[] primitifCpps) {
		for (PrimitifCpp primitif : primitifCpps) {
			map.put(primitif.getNom(), primitif);
		}
	}

	public class Int16 implements PrimitifCpp {
		@Override
		public String getNom() {
			return "Int16";
		}

		@Override
		public Integer extraireDonnee(Desequenceur desequenceur, int parametre) {
			return (int) desequenceur.wrapper(2).order(ByteOrder.LITTLE_ENDIAN).getShort();
		}

		@Override
		public Integer convertirDefaut(String defaut) {
			return Integer.parseInt(defaut);
		}
	}
	


	public class Int32LittleEndian implements PrimitifCpp {
		@Override
		public String getNom() {
			return "Int32LittleEndian";
		}

		@Override
		public Integer extraireDonnee(Desequenceur desequenceur, int parametre) {
			return desequenceur.wrapper(4).order(ByteOrder.LITTLE_ENDIAN).getInt();
		}

		@Override
		public Integer convertirDefaut(String defaut) {
			return Integer.parseInt(defaut);
		}
	}

	public class Int32 implements PrimitifCpp {
		@Override
		public String getNom() {
			return "Int32";
		}
		
		@Override
		public Integer extraireDonnee(Desequenceur desequenceur, int parametre) {
			return desequenceur.$lireUnNombreBER();
		}

		@Override
		public Integer convertirDefaut(String defaut) {
			return Integer.parseInt(defaut);
		}
	}





}
