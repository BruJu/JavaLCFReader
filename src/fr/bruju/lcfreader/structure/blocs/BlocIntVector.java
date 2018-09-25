package fr.bruju.lcfreader.structure.blocs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.XMLInsecticide;
import fr.bruju.lcfreader.structure.types.PrimitifCpp;

/**
 * Un bloc qui correspond à un vecteur de nombres
 * 
 * @author Bruju
 *
 */
public class BlocIntVector extends Bloc<int[]> {
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */

	/** Nom du type primitif C++ */
	public final String nomPrimitive;
	
	/** Type primitif c++ */
	public final PrimitifCpp primitif;

	/**
	 * Bloc qui est un vecteur d'un type primitif c++ qui est converti ici en int
	 * 
	 * @param champ Les caractéristiques
	 * @param nomPrimitive Le nom du type
	 */
	public BlocIntVector(Champ champ, String nomPrimitive) {
		super(champ);
		this.nomPrimitive = nomPrimitive;
		primitif = PrimitifCpp.map.get(nomPrimitive);
	}

	/* ====================
	 * PROPRIETES D'UN BLOC
	 * ==================== */

	@Override
	public String getNomType() {
		return "Vector<" + nomPrimitive + ">";
	}

	/* =====================
	 * CONSTRUIRE UNE VALEUR
	 * ===================== */


	@Override
	public int[] extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		// XMLInsecticide.balise("IntVector_" + this.nom + "_" + nomPrimitive);
		
		if (tailleLue == -1) {
			// XMLInsecticide.balise("Taille");
			tailleLue = desequenceur.$lireUnNombreBER();
			// XMLInsecticide.fermer();
		}
		
		List<Integer> nombres = new ArrayList<>();
		
		if (nomPrimitive.equals("Int32")) {
			remplirBER(desequenceur, nombres, tailleLue);
		} else {
			remplirPrimitives(desequenceur, nombres, tailleLue);
		}

		// XMLInsecticide.fermer();
		
		return convertirEnTableau(nombres);
	}

	private int[] convertirEnTableau(List<Integer> nombres) {
		int[] tableau = new int[nombres.size()];
		for (int i = 0 ; i != tableau.length ; i++) {
			tableau[i] = nombres.get(i);
		}
		return tableau;
	}

	
	private void remplirPrimitives(Desequenceur desequenceur, List<Integer> nombres, int tailleLue) {
		desequenceur = desequenceur.sousSequencer(tailleLue);
		
		while (desequenceur.nonVide()) {
			nombres.add(primitif.lireOctet(desequenceur, 0));
		}
	}

	private void remplirBER(Desequenceur desequenceur, List<Integer> nombres, int tailleLue) {
		for (int i = 0 ; i != tailleLue ; i++) {
			nombres.add(desequenceur.$lireUnNombreBER());
		}
	}

	

	/* ============================
	 * INTERACTION AVEC LES VALEURS
	 * ============================ */

	@Override
	public String convertirEnChaineUneValeur(int[] values) {
		return Arrays.toString(values);
	}
	

	/* =============
	 * CONVERTISSEUR
	 * ============= */


}
