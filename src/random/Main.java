package random;

import java.util.List;

import random.sequenceur.DecodageBER;
import random.sequenceur.NombreBER;
import random.sequenceur.TailleChaine;

public class Main {
	
	public static void afficherByte(Byte octet) {
		System.out.print(String.format("%02X", octet)+" ");
	}

	
	public static void main(String[] args) {
		
		Lecture lecture = new Lecture();
		
		List<Byte> map01 = lecture.mapToBytes("A:\\Users\\SquonK\\Downloads\\Aedemphia\\ressources\\Map0452.lmu");
		afficherBytes(map01, 50);

		afficherByte(map01.get(10));
		afficherByte(map01.get(11));
		afficherByte(map01.get(12));
		afficherByte(map01.get(13));
		System.out.println();
		
		afficherBitsDesMaps(11, new String[] {"Map0001.lmu", "Map0002.lmu", "Map0003.lmu", "Map0004.lmu", "Map0005.lmu", "Map0006.lmu"});
		
		LecteurDeBytes lecteur = new LecteurDeBytes(map01);
		String type = lecteur.sequencer(new TailleChaine());
		
		System.out.println(type);
		System.out.println(lecteur.getPosition());
		afficherByte(map01.get(lecteur.getPosition()));
		System.out.println();
		
		for (int i = 0 ; i != 50 ; i++)
			System.out.println(lecteur.sequencer(new DecodageBER()));
		
		
		
		//LecteurDeBytes lecteur = new LecteurDeBytes(map01);
		
		/*
		String type = lecteur.sequencer(new TailleChaine());
		
		System.out.println(type);
		*/
		
		/*
		List<Byte> map02 = lecture.mapToBytes("A:\\Dev\\AA\\Map0002.lmu");
		
		Map map01J = construireMap(map01.toArray(new Byte[0]));
		
		if (map01J == null) {
			System.out.println("null");
			return;
		}
		
		afficherDifferences(map01, map02);
		
		afficherByte(map01.get(12));
		*/
		
	}

	private static void afficherBitsDesMaps(int i, String[] strings) {
		Lecture lecture = new Lecture();
		
		for (String chemin : strings) {
			List<Byte> map = lecture.mapToBytes("A:\\Dev\\AA\\" + chemin);
			
			System.out.print(chemin + " : ");
			afficherByte(map.get(i));
			System.out.println();
		}

		System.out.println();
	}


	private static Map construireMap(Byte[] bytes) {
		// Header
		if (bytes[0] != 0x0A) {
			return null;
		}
		
		// LCFMapUnit
		if (!"LcfMapUnit".equals(extraireChaine(bytes, 1, 10))) {
			return null;
		}
		
		// continuer
		
		
		return new Map(bytes);
	}

	private static String extraireChaine(Byte[] bytes, int debut, int fin) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = debut ; i <= fin ; i++) {
			sb.append((char) (Byte.toUnsignedInt(bytes[i])));
		}
		
		return sb.toString();
	}

	private static void afficherDifferences(List<Byte> map01, List<Byte> map02) {
		if (map01.size() != map02.size()) {
			System.out.println("Tailles différentes : " + map01.size() + " / " + map02.size());
		}
		
		int nbDeDifferences = 20;
		int min = Math.min(map01.size(), map02.size());
		
		for (int i = 0 ; i != min ; i++) {
			if (nbDeDifferences == 0) {
				System.out.println("...");
				break;
			}
			
			if (map01.get(i) != map02.get(i)) {
				nbDeDifferences--;
				System.out.println("Byte " + i + " : " + String.format("%02X", map01.get(i)) + " " + String.format("%02X", map02.get(i)));
				
			}
			
			
			
			
		}
		
		
		
	}

	private static void afficherBytes(List<Byte> bytes, int nbDeLignes) {
		
		for (int i = 0 ; i != 16; i++) {
			System.out.print(String.format("%02X ", i));
		}
		System.out.println();
		for (int i = 0 ; i != 16 ; i++) {
			System.out.print("---");
		}
		System.out.println();
		
		for (int i = 0 ; i != bytes.size() ; i++) {
			System.out.print(String.format("%02X", bytes.get(i))+ " ");
			
			if (i % 16 == 15) {
				System.out.println();
				
				nbDeLignes --;
				
				if (nbDeLignes == 0)
					return;
			}
			
		}
	}
	
	
	public static class Map {

		public Map(Byte[] bytes) {
		}
		
		
	}

}
