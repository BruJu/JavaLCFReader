package fr.bruju.lcfreader.structure.types;

import fr.bruju.lcfreader.structure.MiniBloc;
import fr.bruju.lcfreader.structure.modele.Desequenceur;

/**
 * Classe permettant d'extraire des chaînes d'une séquence d'octets
 */
public abstract class MiniString implements MiniBloc<String> {
	/* ==============
	 * FAUX SINGLETON
	 * ============== */
	/**
	 * Donne une instance d'un mini bloc pour décoder des chaînes
	 * @param versionDeBaseVoulue Vrai si la chaîne est constitué de tous les octets restants, faux si il faut la lire
	 * @return Une instance de MiniString correspondant à la demande
	 */
	public static MiniBloc<?> getInstance(boolean versionDeBaseVoulue) {
		return versionDeBaseVoulue ? instance : instanceSequentielle;
	}
	
	/** Instance pour les chaînes de taille connue */
	private static MiniString instance = new Discontinu();
	/** Instance pour les chaînes de taille inconnue */
	private static MiniString instanceSequentielle = new Sequentiel();

	/* ===========
	 * MINI STRING
	 * =========== */
	
	@Override
	public String convertirDefaut(String defaut) {
		return defaut.substring(1, defaut.length() - 1);
	}
	
	/**
	 * Version où la chaîne est constituée de tous les octets
	 */
	public static class Discontinu extends MiniString {
		@Override
		public String extraireDonnee(Desequenceur desequenceur, int tailleLue) {
			return desequenceur.$lireUneChaine(desequenceur.octetsRestants());
		}
	}

	/**
	 * Version où la taille de la chaîne doit être lue
	 */
	public static class Sequentiel extends MiniString {
		@Override
		public String extraireDonnee(Desequenceur desequenceur, int tailleLue) {
			return desequenceur.$lireUneChaine(desequenceur.$lireUnNombreBER());
		}
	}
}
