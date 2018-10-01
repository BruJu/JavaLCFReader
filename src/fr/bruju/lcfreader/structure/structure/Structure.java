package fr.bruju.lcfreader.structure.structure;

import java.util.Collection;

import fr.bruju.lcfreader.structure.MiniBloc;
import fr.bruju.lcfreader.structure.bloc.Bloc;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;

/**
 * Lecture de données composé d'autres données
 * 
 * @author Bruju
 *
 */
public abstract class Structure implements MiniBloc<EnsembleDeDonnees> {
	/** Nom de la structure */
	public final String nom;
	
	/**
	 * Crée la structure ayant le nom donné
	 * @param nom Le nom de la structure
	 */
	public Structure(String nom) {
		this.nom = nom;
	}
	
	/**
	 * Ajoute un champ à la structure
	 * @param donnees Un tableau représentant les différentes informations lues dans le fichier fields.csv
	 */
	public abstract void ajouterChamp(String[] donnees);

	/**
	 * Donne le bloc possédant le nom demandé et ne portant pas sur la taille (permet de récupérer les valeurs par
	 * défaut)
	 * @param nomBloc Le nom du bloc
	 * @return Le bloc demandé
	 */
	public final Bloc<?> getBloc(String nomBloc) {
		for (Bloc<?> bloc : obtenirTousLesBlocs()) {
			if (bloc.nom.equals(nomBloc) && !bloc.estUnChampIndiquantLaTaille()) {
				return bloc;
			}
		}

		return null;
	}
	
	/**
	 * Fourni la liste de tous les blocs de la structure
	 * @return La liste de tous les blocs
	 */
	protected abstract Collection<Bloc<?>> obtenirTousLesBlocs();

	@Override
	public final String convertirEnChaineUneValeur(EnsembleDeDonnees valeur) {
		return valeur.getRepresentationEnLigne();
	}
}