package fr.bruju.lcfreader.structure.blocs;

import java.util.Arrays;

import fr.bruju.lcfreader.sequenceur.sequences.ConvertisseurOctetsVersDonnees;
import fr.bruju.lcfreader.sequenceur.sequences.NombreBER;
import fr.bruju.lcfreader.structure.Donnee;
import fr.bruju.lcfreader.structure.types.PrimitifCpp;

public class BlocIntVector extends Bloc<int[]> {
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */
	
	/** Nom du type primitif C++ */
	public final String nomPrimitive;
	
	/**
	 * Bloc qui est un vecteur d'un type primitif c++ qui est converti ici en int
	 * @param champ Les caractéristiques
	 * @param nomPrimitive Le nom du type
	 */
	public BlocIntVector(Champ champ, String nomPrimitive) {
		super(champ);
		this.nomPrimitive = nomPrimitive;
	}

	
	/* ====================
	 * PROPRIETES D'UN BLOC
	 * ==================== */
	
	@Override
	public String getNomType() {
		return "Vector<"+nomPrimitive+">";
	}

	/* =====================
	 * CONSTRUIRE UNE VALEUR
	 * ===================== */

	@Override
	public ConvertisseurOctetsVersDonnees<int[]> getHandler(int tailleLue) {
		return new H(tailleLue);
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
	
	// TODO : il est surement possible d'utiliser un lecteur de séquence intermédiaire
	
	public class H implements ConvertisseurOctetsVersDonnees<int[]> {
		PrimitifCpp primitif = PrimitifCpp.map.get(nomPrimitive);
		
		private NombreBER decodageTaille;
		
		private int[] nombresLus;
		private int indiceNombreEnCours = 0;
		
		private byte[] octetsEnCoursDeLecture;
		private int indiceOctetCourant;

		public H(int tailleLue) {
			if (tailleLue == -1) {
				decodageTaille = new NombreBER();
			} else {
				tailleConnue(tailleLue);
			}
		}
		
		private void tailleConnue(int taille) {
			this.octetsEnCoursDeLecture = new byte[primitif.getNombreDOctets()];
			this.indiceOctetCourant = 0;
			
			this.nombresLus = new int[taille / primitif.getNombreDOctets()];
		}
		
		public Donnee<int[]> accumulerTableau(byte octetRecu) {
			octetsEnCoursDeLecture[indiceOctetCourant++] = octetRecu;
			
			if (indiceOctetCourant == octetsEnCoursDeLecture.length) {
				indiceOctetCourant = 0;
				
				nombresLus[indiceNombreEnCours++] = primitif.convertir(octetsEnCoursDeLecture);
				
				if (indiceNombreEnCours == nombresLus.length) {
					return new Donnee<int[]>(BlocIntVector.this, nombresLus);
				}
			}
			
			return null;
		}

		@Override
		public Donnee<int[]> accumuler(byte octetRecu) {
			if (decodageTaille != null) {
				
				if (!decodageTaille.lireOctet(octetRecu)) {
					tailleConnue(decodageTaille.getResultat().intValue());
					decodageTaille = null;
					
					if (nombresLus.length == 0) {
						return new Donnee<int[]>(BlocIntVector.this, nombresLus);
					}
				}
				
				return null;
			} else {
				return accumulerTableau(octetRecu);
			}
			
		}
	}
}
