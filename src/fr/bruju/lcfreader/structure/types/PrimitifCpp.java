package fr.bruju.lcfreader.structure.types;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.XMLInsecticide;
import fr.bruju.lcfreader.structure.blocs.mini.MiniBloc;

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
			byte octet1 = desequenceur.suivant();
			byte octet2 = desequenceur.suivant();
			int valeur = Byte.toUnsignedInt(octet1) + Byte.toUnsignedInt(octet2) * 0x100;
			
			XMLInsecticide.ajouterXML(octet1);
			XMLInsecticide.ajouterXML(octet2);
			XMLInsecticide.crocheter(valeur);
			
			return valeur;
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
			byte[] octets = {
					desequenceur.suivant(), desequenceur.suivant(), desequenceur.suivant(), desequenceur.suivant()
			};
			
			int valeur = 0;
			
			valeur = ByteBuffer.wrap(octets).order(ByteOrder.LITTLE_ENDIAN).getInt();
			
			for (int i = 0 ; i != 4 ; i++) {
				XMLInsecticide.ajouterXML(octets[i]);
			}
			
			XMLInsecticide.crocheter(valeur);
			
			return valeur;
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
