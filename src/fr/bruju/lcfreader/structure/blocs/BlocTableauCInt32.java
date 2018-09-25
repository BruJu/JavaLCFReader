package fr.bruju.lcfreader.structure.blocs;

import java.util.Arrays;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.XMLInsecticide;

/**
 * Un bloc qui correspond à un vecteur de nombres
 * 
 * @author Bruju
 *
 */
public class BlocTableauCInt32 extends Bloc<int[]> {
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */
	/**
	 * Bloc qui est un vecteur d'un type primitif c++ qui est converti ici en int
	 * 
	 * @param champ Les caractéristiques
	 * @param nomPrimitive Le nom du type
	 */
	public BlocTableauCInt32(Champ champ) {
		super(champ);
	}

	/* ====================
	 * PROPRIETES D'UN BLOC
	 * ==================== */

	@Override
	public String getNomType() {
		return "TableauC";
	}

	/* =====================
	 * CONSTRUIRE UNE VALEUR
	 * ===================== */


	@Override
	public int[] extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		// XMLInsecticide.balise("TableauC_" + this.nom);
		
		int[] nombres = new int[tailleLue / 4];
		
		byte octet;
		
		for (int i = 0 ; i != nombres.length ; i++) {
			int valeur = 0;
			
			for (int j = 0 ; j != 4 ; j++) {
				octet = desequenceur.suivant();
				// XMLInsecticide.ajouterXML(octet);
				
				valeur += Byte.toUnsignedInt(octet) << (j * 8);
			}
			
			// XMLInsecticide.xml(valeur, " | ");
		}
		
		// XMLInsecticide.fermer();
		
		return nombres;
	}
	

	

	/* ============================
	 * INTERACTION AVEC LES VALEURS
	 * ============================ */

	@Override
	public String convertirEnChaineUneValeur(int[] values) {
		return Arrays.toString(values);
	}
}
