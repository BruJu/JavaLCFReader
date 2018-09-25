package fr.bruju.lcfreader.modele;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import fr.bruju.lcfreader.Utilitaire;

public class XMLInsecticide {
	// Affichage des données reçues en xml (pour le debug)
	private static String xml = "";
	private static boolean a = false;
	private static List<String> balises = new ArrayList<>();


	public static void vider() {
		xml = "";
		balises.clear();
		balise("Document");
	}
	
	public static void ajouterXML(byte octet) {
		if (a) {
			xml( " ");
		}
		
		xml( Utilitaire.toHex(octet));
		a = true;
	}
	public static void balise(String nom) {
		xml("<" + nom + ">");
		a = false;
		balises.add(nom);
	}
	
	public static void fermer() {
		String balise = balises.get(balises.size() - 1);
		xml("</" + balise + ">");
		a = false;
		balises.remove(balises.size() - 1);
	}

	public static void fermer(String string) {
		String balise = balises.get(balises.size() - 1);
		if (!balise.endsWith(string))
			throw new RuntimeException("Veut dépiler " + string + " mais a trouvé " + balise);
		
		xml( "</" + balise + ">");
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
        
        xml = "";
	}

	public static void xml(String string) {
		xml += string;
	}
}
