package fr.bruju.lcfreader.modele;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import fr.bruju.lcfreader.Utilitaire;

public class XMLInsecticide {
	// Affichage des données reçues en xml (pour le debug)
	private static StringBuilder xml = new StringBuilder();
	private static boolean a = false;
	private static List<String> balises = new ArrayList<>();


	public static void vider() {
		xml.setLength(0);
		balises.clear();
		balise("Document");
	}
	
	public static void ajouterXML(byte octet) {
		if (a) {
			xml(" ");
		}
		
		xml(Utilitaire.toHex(octet));
		a = true;
	}
	private static String INFERIEUR = "<";
	private static String INFERIEURSLASH = "</";
	private static String SUPERIEUR = "<";
	private static String CROCHETOUVRANT = " [";
	private static String CROCHETFERMANT = "]";
	
	
	public static void balise(String nom) {
		xml.append(INFERIEUR);
		xml.append(nom);
		xml.append(SUPERIEUR);
		a = false;
		balises.add(nom);
	}
	
	public static void fermer() {
		String balise = balises.get(balises.size() - 1);
		xml.append(INFERIEURSLASH);
		xml.append(balise);
		xml.append(SUPERIEUR);
		a = false;
		balises.remove(balises.size() - 1);
	}

	public static void fermer(String string) {
		String balise = balises.get(balises.size() - 1);
		if (!balise.endsWith(string))
			throw new RuntimeException("Veut dépiler " + string + " mais a trouvé " + balise);

		xml.append(INFERIEURSLASH);
		xml.append(balise);
		xml.append(SUPERIEUR);
		a = false;
		balises.remove(balises.size() - 1);
	}

	
	public static void ecrireDebug() throws IOException {
		
		while (!balises.isEmpty()) {
			fermer();
		}
		
		PrintWriter pWriter = new PrintWriter(new FileWriter("../debug.xml", false));
        pWriter.print(xml);
        pWriter.close();
        
        vider();
	}

	public static void xml(String string) {
		xml.append(string);
	}
	
	public static void xml(String s1, String s2) {
		xml.append(s1);
		xml.append(s2);
	}

	public static void xml(String s1, String s2, String s3) {
		xml.append(s1);
		xml.append(s2);
		xml.append(s3);
	}
	
	public static void crocheter(String string) {
		xml.append(CROCHETOUVRANT);
		xml.append(string);
		xml.append(CROCHETFERMANT);
	}
	
	
	
}
