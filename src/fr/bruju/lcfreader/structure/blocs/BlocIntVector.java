package fr.bruju.lcfreader.structure.blocs;

import java.util.Arrays;

import fr.bruju.lcfreader.sequenceur.sequences.ConvertisseurOctetsVersDonnees;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;
import fr.bruju.lcfreader.structure.Donnee;
import fr.bruju.lcfreader.structure.types.PrimitifCpp;

public class BlocIntVector extends Bloc<int[]> {

	public static Bloc<?> essayer(String type) {
		if (!type.startsWith("Vector<") || !type.endsWith(">"))
			return null;
		
		// Extraction de la chaîne entre Vector< et >
		String sousType = type.substring(7, type.length() - 1);
		
		if (PrimitifCpp.map.get(sousType) == null) {
			
			if (BaseDeDonneesDesStructures.getInstance().get(sousType) != null) {
				System.out.println("woot " + sousType);
				// return new BlocEnsembleVector(sousType);
			} else {
				System.out.println("null and void " + sousType + " @ ");
			}
			
			
			return null;
		} else {
			return new BlocIntVector(sousType);
		}
	}
	
	
	public final String nomPrimitive;
	
	public BlocIntVector(String nomPrimitive) {
		this.nomPrimitive = nomPrimitive;
	}

	@Override
	public String getRepresentation() {
		return "Vector<"+nomPrimitive+">";
	}


	@Override
	public String valueToString(int[] values) {
		return Arrays.toString(values);
	}

	@Override
	public ConvertisseurOctetsVersDonnees<int[]> getHandler(int tailleLue) {
		return new H(tailleLue);
	}
	
	
	public class H implements ConvertisseurOctetsVersDonnees<int[]> {
		PrimitifCpp primitif = PrimitifCpp.map.get(nomPrimitive);
		
		private int[] nombresLus;
		private int indiceNombreEnCours = 0;
		
		private byte[] octetsEnCoursDeLecture;
		private int indiceOctetCourant;

		public H(int tailleLue) {
			this.octetsEnCoursDeLecture = new byte[primitif.getNombreDOctets()];
			this.indiceOctetCourant = 0;
			
			this.nombresLus = new int[tailleLue / primitif.getNombreDOctets()];
		}

		@Override
		public Donnee<int[]> accumuler(byte octetRecu) {
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

	}
}
