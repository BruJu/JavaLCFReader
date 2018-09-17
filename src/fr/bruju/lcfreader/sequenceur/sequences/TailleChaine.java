package fr.bruju.lcfreader.sequenceur.sequences;

/**
 * Lit une chaîne de la forme [nombre de caractères] [un octet par caractère]
 * 
 * @author Bruju
 *
 */
public class TailleChaine implements LecteurDeSequence<String> {
	/** Etat actuel */
	private Etat etat = new EtatNombre();
	/** Contient le résultat */
	private String resultat;

	@Override
	public boolean lireOctet(byte byteLu) {
		etat = etat.lireOctet(byteLu);
		return resultat == null;
	}

	@Override
	public String getResultat() {
		return resultat;
	}

	/** Etat lisant un nombre */
	private class EtatNombre implements Etat {
		/** Lecteur */
		private LecteurDeSequence<Integer> lecteur = new NombreBER();

		@Override
		public Etat lireOctet(byte octet) {
			return lecteur.lireOctet(octet) ? this : new EtatChaine(lecteur.getResultat());
		}
	}

	/** Etat lisant une chaîne d'un nombre d'octets donnés */
	private class EtatChaine implements Etat {
		/** Lecteur */
		private LecteurDeSequence<String> lecteur;

		/**
		 * Crée un état visant à lire une chaîne de la taille donnée. Si la taille est de 0, met la variable resultat à
		 * une chaîne vide
		 * 
		 * @param taille La taille à lire
		 */
		public EtatChaine(int taille) {
			lecteur = new Chaine(taille);

			if (taille == 0) {
				resultat = "";
			}
		}

		@Override
		public Etat lireOctet(byte octet) {
			if (lecteur.lireOctet(octet)) {
				return this;
			}

			resultat = lecteur.getResultat();
			return null;
		}
	}
}
