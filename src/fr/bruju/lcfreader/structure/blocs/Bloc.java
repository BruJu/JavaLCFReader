package fr.bruju.lcfreader.structure.blocs;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.structure.Donnee;
import fr.bruju.lcfreader.structure.Sequenceur;

/**
 * Un bloc est une manière de représenter un type de données à savoir comment l'afficher, comment le décrypter,
 * quel est le type des données contenues
 * 
 * @author Bruju
 *
 * @param <T> Le type de données utilisé pour représenter les données en Java
 */
public abstract class Bloc<T> implements Sequenceur<Donnee<T>> {
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */
	
	/** Index du bloc dans la structure */
	public final int index;
	/** Nom des données représentées par le bloc */
	public final String nom;
	/** Vrai si le champ est un champ de taille */
	protected final boolean sized;
	
	public final String vraiType;
	
	/**
	 * Construit le bloc
	 * @param champ Les données du bloc
	 */
	public Bloc(Champ champ) {
		this.index = champ.index;
		this.nom = champ.nom;
		this.sized = champ.sized;
		this.vraiType = champ.vraiType;
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

	@Override
	public final Donnee<T> lireOctet(Desequenceur desequenceur, int tailleLue) {
		return new Donnee<>(this, extraireDonnee(desequenceur, tailleLue));
	}
	
	abstract T extraireDonnee(Desequenceur desequenceur, int tailleLue);

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


	public Bloc<byte[]> inconnu() {
		return new BlocInconnu(new Champ(index, nom, sized, vraiType), vraiType);
	}
}