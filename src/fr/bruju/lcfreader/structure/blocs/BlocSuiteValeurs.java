package fr.bruju.lcfreader.structure.blocs;

import java.util.Arrays;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.XMLInsecticide;
import fr.bruju.lcfreader.structure.types.PrimitifCpp;

/**
 * Un bloc qui correspond à un vecteur de nombres
 * 
 * @author Bruju
 *
 */
public class BlocSuiteValeurs extends Bloc<int[]> {
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */
	
	/** Type primitif c++ */
	public final PrimitifCpp primitif;
	
	public final int nombreDeValeurs;

	/**
	 * Bloc qui est un vecteur d'un type primitif c++ qui est converti ici en int
	 * 
	 * @param champ Les caractéristiques
	 * @param nomPrimitive Le nom du type
	 */
	public BlocSuiteValeurs(Champ champ, String nomPrimitive, int nombreDeValeurs) {
		super(champ);
		primitif = PrimitifCpp.map.get(nomPrimitive);
		this.nombreDeValeurs = nombreDeValeurs;
	}

	/* ====================
	 * PROPRIETES D'UN BLOC
	 * ==================== */

	@Override
	public String getNomType() {
		return "Vector<" + primitif.getNom() + ", " + nombreDeValeurs + ">";
	}

	/* =====================
	 * CONSTRUIRE UNE VALEUR
	 * ===================== */


	@Override
	public int[] extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		XMLInsecticide.balise("IntVector_" + nombreDeValeurs +"_" + this.nom + "_" + primitif.getNom());
		

		int[] nombres = new int[nombreDeValeurs];
		
		remplirPrimitives(desequenceur, nombres);

		XMLInsecticide.fermer();
		
		return nombres;
	}
	
	private void remplirPrimitives(Desequenceur desequenceur, int[] nombres) {
		for (int i = 0 ; i != nombreDeValeurs ; i++) {
			nombres[i] = primitif.lireOctet(desequenceur, 0);
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
