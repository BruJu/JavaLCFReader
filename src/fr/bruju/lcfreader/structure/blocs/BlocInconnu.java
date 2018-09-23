package fr.bruju.lcfreader.structure.blocs;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.sequenceur.lecteurs.Desequenceur;
import fr.bruju.lcfreader.structure.Donnee;

/**
 * Un bloc dont les données ne peuvent être décryptées.
 * 
 * @author Bruju
 *
 */
public class BlocInconnu extends Bloc<byte[]> {
	
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */

	/** Le nom du type dans fields.csv */
	public final String type;

	/**
	 * Crée un bloc indécryptable
	 * 
	 * @param champ Les caractéristiques
	 * @param type Le nom du type déclaré
	 */
	public BlocInconnu(Champ champ, String type) {
		super(champ);
		this.type = type;
	}

	/* ====================
	 * PROPRIETES D'UN BLOC
	 * ==================== */

	@Override
	public String getNomType() {
		return "Inconnu {" + type + "}";
	}

	/* =====================
	 * CONSTRUIRE UNE VALEUR
	 * ===================== */

	/* ============================
	 * INTERACTION AVEC LES VALEURS
	 * ============================ */

	@Override
	public String convertirEnChaineUneValeur(byte[] value) {
		return value.length + ">" + getTableauDOctetsEnChaine(value);
	}
	
	private String getTableauDOctetsEnChaine(byte[] valeurs) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		if (valeurs.length != 0) {
			sb.append(Utilitaire.toHex(valeurs[0]));
			
			for (int i = 1 ; i != valeurs.length ; i++) {
				sb.append(" ").append(Utilitaire.toHex(valeurs[i]));
			}
		}
		
		sb.append("]");
		
		return sb.toString();
	}

	/* =============
	 * CONVERTISSEUR
	 * ============= */


	@Override
	public byte[] extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		if (tailleLue == -1) {
			throw new RuntimeException("Bloc inconnu en lecture en série détecté : " + type);
		}
		
		byte[] octets = new byte[tailleLue];
		
		for (int i = 0 ; i != tailleLue ; i++) {
			octets[i] = desequenceur.suivant();
		}
		
		return octets;
	}
}