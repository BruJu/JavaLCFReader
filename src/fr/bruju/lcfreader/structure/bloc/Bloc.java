package fr.bruju.lcfreader.structure.bloc;

import fr.bruju.lcfreader.structure.Donnee;
import fr.bruju.lcfreader.structure.MiniBloc;
import fr.bruju.lcfreader.structure.modele.Desequenceur;

/**
 * Un bloc est une manière de représenter un type de données à savoir comment l'afficher, comment le décrypter,
 * quel est le type des données contenues
 * 
 * @author Bruju
 *
 * @param <T> Le type de données utilisé pour représenter les données en Java
 */
public abstract class Bloc<T> implements MiniBloc<T> {
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */
	
	/** Index du bloc dans la structure */
	public final int index;
	/** Nom des données représentées par le bloc */
	public final String nom;
	/** Nom du type complet pour le retrouver dans fields.csv */
	public final String typeComplet;
	
	/**
	 * Construit le bloc
	 */
	public Bloc(int index, String nom, String type) {
		this.index = index;
		this.nom = nom;
		this.typeComplet = type;
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
		return false;
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
	 * Lit les données pour en faire une donnée ajoutable dans un ensemble de données
	 * @param desequenceur Le tableau d'octets
	 * @param tailleLue Le nombre d'octets à lire (non fiable)
	 * @return Une donnée correspondant à ce bloc et à la valeur lue
	 */
	public final Donnee<T> lireOctet(Desequenceur desequenceur, int tailleLue) {
		return new Donnee<>(this, extraireDonnee(desequenceur, tailleLue));
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
		  .append(typeComplet)
		  .toString();
	}
}