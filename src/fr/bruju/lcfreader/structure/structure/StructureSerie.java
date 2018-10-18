package fr.bruju.lcfreader.structure.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.bruju.lcfreader.structure.bloc.Bloc;
import fr.bruju.lcfreader.structure.bloc.InstancieurDeBlocs;
import fr.bruju.lcfreader.structure.modele.Desequenceur;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;


/**
 * Structure lisant en série les blocs. Ceux-ci sont disposés côte à côte et chaque sous bloc est reponsable de savoir
 * quand il a terminé sa lecture.
 * 
 * @author Bruju
 *
 */
public class StructureSerie extends Structure {
	/**
	 * Crée une structure de lecture de données en série
	 * @param nom Le nom de la structure
	 */
	public StructureSerie(String nom) {
		super(nom);
	}

	/** Liste des blocs pour un décodage en série */
	private List<Bloc<?>> serie = new ArrayList<>();

	@Override
	public EnsembleDeDonnees extraireDonnee(Desequenceur desequenceur, int parametre) {
		EnsembleDeDonnees ensemble = new EnsembleDeDonnees(this);

		for (Bloc<?> bloc : obtenirTousLesBlocs()) {
			ensemble.ajouter(bloc.lireOctet(desequenceur, ensemble.getTaille(bloc)));
		}
		
		return ensemble;
	}

	@Override
	public void ajouterChamp(String[] donnees) {
		Bloc<?> bloc = InstancieurDeBlocs.instancier(donnees);
		serie.add(bloc);
	}

	@Override
	protected Collection<Bloc<?>> obtenirTousLesBlocs() {
		return serie;
	}
}