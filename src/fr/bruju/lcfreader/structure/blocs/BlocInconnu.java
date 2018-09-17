package fr.bruju.lcfreader.structure.blocs;

import fr.bruju.lcfreader.debug.BytePrinter;
import fr.bruju.lcfreader.sequenceur.sequences.ConvertisseurOctetsVersDonnees;
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

	@Override
	public ConvertisseurOctetsVersDonnees<byte[]> getHandler(int tailleLue) {
		return new BlocHandler(tailleLue);
	}

	/* ============================
	 * INTERACTION AVEC LES VALEURS
	 * ============================ */

	@Override
	public String convertirEnChaineUneValeur(byte[] value) {
		return value.length + ">" + BytePrinter.getTable(value);
	}

	/* =============
	 * CONVERTISSEUR
	 * ============= */

	/**
	 * Le convertisseur de bloc inconnu ! Se contente d'enregistrer tous les octets reçus.
	 * 
	 * @author Bruju
	 *
	 */
	public class BlocHandler implements ConvertisseurOctetsVersDonnees<byte[]> {
		/** Liste des octets de ce bloc mystérieux */
		private byte[] octets;
		/** Indice du prochain octet à remplir */
		private int i;

		/**
		 * Construit le convertisseur de bloc pour stocker la liste des octets
		 * 
		 * @param tailleLue Le nombre d'octets
		 */
		public BlocHandler(int tailleLue) {
			if (tailleLue == -1) {
				throw new RuntimeException("Bloc inconnu en lecture en série détecté : " + type);
			}

			octets = new byte[tailleLue];
			i = 0;
		}

		@Override
		public Donnee<byte[]> accumuler(byte octet) {
			octets[i++] = octet;

			return (i == octets.length) ? new Donnee<byte[]>(BlocInconnu.this, octets) : null;
		}
	}
}