package fr.bruju.lcfreader.structure.types;

import java.util.HashMap;
import java.util.Map;

import fr.bruju.lcfreader.sequenceur.lecteurs.Desequenceur;
import fr.bruju.lcfreader.sequenceur.sequences.LecteurDeSequence;
import fr.bruju.lcfreader.sequenceur.sequences.NombreBER;
import fr.bruju.lcfreader.sequenceur.sequences.Sequenceur;

public interface PrimitifCpp extends Sequenceur<Integer> {
	static abstract class LecteurAOctetsFixe implements LecteurDeSequence<Integer> {
		protected byte[] accumulateur;
		private int indice = 0;

		public LecteurAOctetsFixe(int nombreDOctets) {
			accumulateur = new byte[nombreDOctets];
		}

		@Override
		public boolean lireOctet(byte octet) {
			accumulateur[indice++] = octet;
			return indice != accumulateur.length;
		}

		@Override
		public abstract Integer getResultat();
	}
	
	

	
	
	public String getNom();
	
	public LecteurDeSequence<Integer> getLecteur();
	

	public static Map<String, PrimitifCpp> map = remplirHashMap(
			new PrimitifCpp[] {
					new Int16(),
					new Int32(),
					new SequenceurIntATailleFixe.UInt8(),
					new SequenceurIntATailleFixe.UInt16(),
					new SequenceurIntATailleFixe.UInt32(),
					new SequenceurIntATailleFixe.Boolean(),
				});

	public static Map<String, PrimitifCpp> remplirHashMap(PrimitifCpp[] primitifCpps) {
		HashMap<String, PrimitifCpp> map = new HashMap<>();

		for (PrimitifCpp primitif : primitifCpps) {
			map.put(primitif.getNom(), primitif);
		}

		return map;
	}

	public class Int16 implements PrimitifCpp {
		@Override
		public String getNom() {
			return "Int16";
		}

		@Override
		public LecteurDeSequence<Integer> getLecteur() {
			return new LecteurAOctetsFixe(2) {
				@Override
				public Integer getResultat() {
					return (Byte.toUnsignedInt(accumulateur[0]) + Byte.toUnsignedInt(accumulateur[1]) * 0x100);
				}
			};
		}

		@Override
		public Integer lireOctet(Desequenceur desequenceur, int parametre) {
			return (Byte.toUnsignedInt(desequenceur.suivant()) + Byte.toUnsignedInt(desequenceur.suivant()) * 0x100);
		}
	}

	public class Int32 implements PrimitifCpp {
		@Override
		public String getNom() {
			return "Int32";
		}

		@Override
		public LecteurDeSequence<Integer> getLecteur() {
			return new NombreBER();
		}

		@Override
		public Integer lireOctet(Desequenceur desequenceur, int parametre) {
			return desequenceur.$lireUnNombreBER();
		}
	}





}
