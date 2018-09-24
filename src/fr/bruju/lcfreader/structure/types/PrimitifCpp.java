package fr.bruju.lcfreader.structure.types;

import java.util.HashMap;
import java.util.Map;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.structure.Sequenceur;

public interface PrimitifCpp extends Sequenceur<Integer> {

	
	public String getNom();
	
	

	public static Map<String, PrimitifCpp> map = remplirHashMap(
			new PrimitifCpp[] {
					new Int16(),
					new Int32(),
					new SequenceurIntATailleFixe.UInt8(),
					new SequenceurIntATailleFixe.UInt16(),
					new SequenceurIntATailleFixe.UInt32(),
					new SequenceurIntATailleFixe.Boolean(),
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
		public Integer lireOctet(Desequenceur desequenceur, int parametre) {
			byte octet1 = desequenceur.suivant();
			byte octet2 = desequenceur.suivant();
			int valeur = Byte.toUnsignedInt(octet1) + Byte.toUnsignedInt(octet2) * 0x100;
			
			Desequenceur.ajouterXML(octet1);
			Desequenceur.ajouterXML(octet2);
			Desequenceur.xml += " [" + valeur + "]";
			
			return valeur;
		}
	}

	public class Int32 implements PrimitifCpp {
		@Override
		public String getNom() {
			return "Int32";
		}
		
		@Override
		public Integer lireOctet(Desequenceur desequenceur, int parametre) {
			return desequenceur.$lireUnNombreBER();
		}
	}





}
