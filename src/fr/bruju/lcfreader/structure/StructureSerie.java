package fr.bruju.lcfreader.structure;

import java.util.ArrayList;
import java.util.List;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.structure.blocs.Bloc;
import fr.bruju.lcfreader.structure.blocs.Blocs;

/**
 * L'ensemble des associations index - champs pour un type de données
 * 
 * @author Bruju
 *
 */
public class StructureSerie extends Structure {

	public StructureSerie(String nom) {
		super(nom);
	}

	/** Liste des blocs pour un décodage en série */
	protected List<Bloc<?>> serie = new ArrayList<>();

	@Override
	public EnsembleDeDonnees lireOctet(Desequenceur desequenceur, int parametre) {
		// XMLInsecticide.balise("dataDisc");

		EnsembleDeDonnees ensemble = new EnsembleDeDonnees(this);

		for (Bloc<?> bloc : getSerie()) {
			// XMLInsecticide.balise("bloc");
			ensemble.push(bloc.lireOctet(desequenceur, -1));
			// XMLInsecticide.fermer();
		}

		// XMLInsecticide.fermer();

		return ensemble;
	}

	@Override
	public void ajouterChamp(String[] donnees) {
		Bloc<?> bloc = Blocs.instancier(donnees);
		serie.add(bloc);
	}

	/**
	 * Donne la liste des blocs à lire pour cette structure
	 * 
	 * @return La liste des blocs indiquant comment décrypter cette structure
	 */
	public List<Bloc<?>> getSerie() {
		return serie;
	}

	@Override
	public Bloc<?> getBloc(String nomBloc) {
		for (Bloc<?> bloc : serie) {
			if (bloc.nom.equals(nomBloc) && !bloc.estUnChampIndiquantLaTaille()) {
				return bloc;
			}
		}

		return null;
	}

}