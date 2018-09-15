package fr.bruju.lcfreader;

public class Utilitaire {

	public static String toHex(int octet) {
		return String.format("%02X", octet);
	}
	
	public static String toHex(byte octet) {
		return String.format("%02X", octet);
	}
}
