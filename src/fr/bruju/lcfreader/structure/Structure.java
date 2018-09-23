package fr.bruju.lcfreader.structure;

import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.structure.blocs.Bloc;

public abstract class Structure implements Sequenceur<EnsembleDeDonnees> {
	
	public final String nom;
	
	public Structure(String nom) {
		this.nom = nom;
	}
	
	
	/** Ajoute un champ */
	public abstract void ajouterChamp(String[] donnees);

	public abstract Bloc<?> getBloc(String nomBloc);

}