package fr.bruju.lcfreader.structure.blocs;

import fr.bruju.lcfreader.sequenceur.sequences.ConvertisseurOctetsVersDonnees;

/**
 * Un bloc est une manière de représenter un type de données à savoir comment l'afficher, comment le décrypter,
 * quel est le type des données contenues
 * 
 * @author Bruju
 *
 * @param <T> Le type de données utilisé pour représenter les données en Java
 */
public abstract class Bloc<T> {
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */
	
	/** Index du bloc dans la structure */
	public final int index;
	/** Nom des données représentées par le bloc */
	public final String nom;
	/** Vrai si le champ est un champ de taille */
	protected final boolean sized;
	
	/**
	 * Construit le bloc
	 * @param champ Les données du bloc
	 */
	public Bloc(Champ champ) {
		this.index = champ.index;
		this.nom = champ.nom;
		this.sized = champ.sized;
	}
	
	
	/* ====================
	 * PROPRIETES D'UN BLOC
	 * ==================== */

	/**
	 * Donne le nom du type de bloc
	 * @return Le nom du type de bloc
	 */
	protected abstract String getNomType();
	
	/**
	 * Renvoi vrai si ce bloc concerne la taille d'un autre bloc
	 * @return Vrai si ce bloc représente la taille d'un autre bloc
	 */
	public boolean estUnChampIndiquantLaTaille() {
		return sized;
	}

	/**
	 * Donne la valeur par défaut
	 * @return La valeur par défaut
	 */
	public T valeurParDefaut() {
		return null;
	}
	

	/* =====================
	 * CONSTRUIRE UNE VALEUR
	 * ===================== */

	/**
	 * Donne un lecteur octet par octet 
	 * <br>
	 * Si tailleLue = -1, il faut lire soi même la taille.
	 * 
	 * @param tailleLue Le nombre d'octets à lire, -1 si inconnu
	 * @return Un sequenceur octet par octet pour obtenir un objet valeur du bloc
	 */
	public abstract ConvertisseurOctetsVersDonnees<T> getHandler(int tailleLue);
	

	/* ============================
	 * INTERACTION AVEC LES VALEURS
	 * ============================ */

	/**
	 * Converti la valeur en une chaîne comme si elle était issue du bloc
	 * @param valeur La valeur
	 * @return Une représentation en chaîne de la valeur
	 */
	public String convertirEnChaineUneValeur(T valeur) {
		return valeur.toString();
	}

	/**
	 * Affiche les ensembles de données contenus par la valeur passée
	 * @param niveau Le niveau d'indentation actuel
	 * @param value La valeur dont on souhaite connaître les sous ensembles de données
	 */
	public void afficherSousArchi(int niveau, T value) {
	}

	
	/* =========
	 * AFFICHAGE
	 * ========= */

	/**
	 * Donne une représentation du bloc de la forme "index nom type estUnChampTaille"
	 * @return Une représentation du bloc de la forme "index nom type estUnChampTaille"
	 */
	public final String getTypeEnString() {
		return new StringBuilder()
		  .append(String.format("%02X", index))
		  .append(" ")
		  .append(nom)
		  .append(" ")
		  .append(getNomType())
		  .append(" ")
		  .append(estUnChampIndiquantLaTaille())
		  .toString();
	}
	
}