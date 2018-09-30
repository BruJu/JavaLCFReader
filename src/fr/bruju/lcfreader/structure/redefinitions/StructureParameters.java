package fr.bruju.lcfreader.structure.redefinitions;

import java.util.Arrays;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.XMLInsecticide;
import fr.bruju.lcfreader.structure.StructureSerie;
import fr.bruju.lcfreader.structure.blocs.Bloc;
import fr.bruju.lcfreader.structure.blocs.Champ;
import fr.bruju.lcfreader.structure.blocs.mini.MiniBloc;
import fr.bruju.lcfreader.structure.types.PrimitifCpp;

public class StructureParameters extends StructureSerie {
	
	// Lit 99 x 6 short
	
	public StructureParameters() {
		super("Parameters");
		
		String[] champs = {"maxhp", "maxsp", "attack", "defense", "spirit", "agility"};
		
		for (String nomChamp : champs) {
			serie.add(new BlocSuiteValeurs(new Champ(0, nomChamp, false, "Int16"), new PrimitifCpp.Int16(), 99));
		}
	}

	@Override
	public void ajouterChamp(String[] donnees) {
		// Ne pas accepter les champs du fichier fields
	}
	
	/**
	 * Un bloc qui correspond à un vecteur de nombres
	 * 
	 * @author Bruju
	 *
	 */
	private static class BlocSuiteValeurs extends Bloc<int[]> {
		/* =========================
		 * ATTRIBUTS ET CONSTRUCTEUR
		 * ========================= */
		
		/** Type primitif c++ */
		public final MiniBloc<Integer> primitif;
		
		public final int nombreDeValeurs;

		/**
		 * Bloc qui est un vecteur d'un type primitif c++ qui est converti ici en int
		 * 
		 * @param champ Les caractéristiques
		 * @param nomPrimitive Le nom du type
		 */
		public BlocSuiteValeurs(Champ champ, MiniBloc<Integer> miniBloc, int nombreDeValeurs) {
			super(champ);
			primitif = miniBloc;
			this.nombreDeValeurs = nombreDeValeurs;
		}

		/* ====================
		 * PROPRIETES D'UN BLOC
		 * ==================== */

		@Override
		public String getNomType() {
			return "Vector<" + ", " + nombreDeValeurs + ">";
		}

		/* =====================
		 * CONSTRUIRE UNE VALEUR
		 * ===================== */


		@Override
		public int[] extraireDonnee(Desequenceur desequenceur, int tailleLue) {
			XMLInsecticide.balise("IntVector_" + nombreDeValeurs +"_" + this.nom + "_");
			

			int[] nombres = new int[nombreDeValeurs];
			
			remplirPrimitives(desequenceur, nombres);

			XMLInsecticide.fermer();
			
			return nombres;
		}
		
		private void remplirPrimitives(Desequenceur desequenceur, int[] nombres) {
			for (int i = 0 ; i != nombreDeValeurs ; i++) {
				nombres[i] = primitif.extraireDonnee(desequenceur, 0);
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
}
