package fr.bruju.lcfreader.structure.types;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.structure.MiniBloc;
import fr.bruju.lcfreader.structure.modele.Desequenceur;

/**
 * Mini bloc extrayant les données de séquence d'octets dont le format de décryptage n'est pas connu
 * 
 * @author Bruju
 *
 */
public class MiniInconnu implements MiniBloc<byte[]> {
	/* =========
	 * SINGLETON
	 * ========= */

	/** Instance */
	private static MiniInconnu instance;

	/** Constructeur privé vide */
	private MiniInconnu() {
	}

	/**
	 * Donne l'instance du mini bloc décodant des séquences d'octets inconnues
	 * @return L'instance de MiniInconnu
	 */
	public static MiniInconnu getInstance() {
		if (null == instance) {
			instance = new MiniInconnu();
		}
		return instance;
	}

	/* =========
	 * MINI BLOC
	 * ========= */
	
	@Override
	public byte[] extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		if (tailleLue == -1) {
			throw new RuntimeException("Bloc inconnu de taille inconnue");
		}
		
		return desequenceur.extrairePortion(tailleLue);
	}
	
	@Override
	public String convertirEnChaineUneValeur(byte[] value) {
		return value.length + ">" + Utilitaire.bytesToString(value);
	}
}
