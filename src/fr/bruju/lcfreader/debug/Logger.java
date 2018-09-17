package fr.bruju.lcfreader.debug;

import java.util.ArrayList;
import java.util.List;

import fr.bruju.lcfreader.Utilitaire;

public class Logger {
	private static List<String> enregistrements = new ArrayList<>();
	private static int niveau = 0;
	private static StringBuilder string = new StringBuilder();
	
	
	private static void ln() {
		enregistrements.add(string.toString());
		string = new StringBuilder();
	}
	
	private static void tab() {
		for (int i = 0 ; i != niveau ; i++) {
			string.append("  ");
		}
	}
	
	public static void nouveauNiveau(String nom) {
		niveau++;
		marquer(nom);
	}
	
	public static void marquer(String nom) {
		ln();
		tab();
		string.append(nom);
	}
	
	public static void parent() {
		ln();
		niveau--;
	}
	
	public static void octet(byte octet) {
		string.append(" ").append(Utilitaire.toHex(octet));
	}
	
	
}
