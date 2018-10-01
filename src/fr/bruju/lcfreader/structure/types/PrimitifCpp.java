package fr.bruju.lcfreader.structure.types;

import java.nio.ByteOrder;

import fr.bruju.lcfreader.structure.MiniBloc;
import fr.bruju.lcfreader.structure.modele.Desequenceur;

/**
 * Minibloc encodant des nombres qui seront représentés par des integer.
 *  
 * @author Bruju
 *
 */
public interface PrimitifCpp extends MiniBloc<Integer> {
	/**
	 * Donne le nom du type primitif tel qu'inscrit dans le fichier fields.csv
	 * @return Le nom
	 */
	public String getNom();
	
	@Override
	public default Integer convertirDefaut(String defaut) {
		return Integer.parseInt(defaut);
	}
	
	/* ===============
	 * IMPLEMENTATIONS
	 * =============== */
	
	/**
	 * Int16 / Short (2 octets en little endian)
	 */
	public class Int16 implements PrimitifCpp {
		@Override
		public String getNom() {
			return "Int16";
		}

		@Override
		public Integer extraireDonnee(Desequenceur desequenceur, int parametre) {
			return (int) desequenceur.wrapper(2).order(ByteOrder.LITTLE_ENDIAN).getShort();
		}
	}
	

	/**
	 * Int32 / int (4 octets en little endian)
	 */
	public class Int32LittleEndian implements PrimitifCpp {
		@Override
		public String getNom() {
			return "Int32LittleEndian";
		}

		@Override
		public Integer extraireDonnee(Desequenceur desequenceur, int parametre) {
			return desequenceur.wrapper(4).order(ByteOrder.LITTLE_ENDIAN).getInt();
		}
	}

	/**
	 * Entier encodé en BER
	 */
	public class Int32 implements PrimitifCpp {
		@Override
		public String getNom() {
			return "Int32";
		}
		
		@Override
		public Integer extraireDonnee(Desequenceur desequenceur, int parametre) {
			return desequenceur.$lireUnNombreBER();
		}
	}
}
