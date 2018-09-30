package fr.bruju.lcfreader.structure.structure;

import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.structure.bloc.Bloc;
import fr.bruju.lcfreader.structure.blocs.mini.MiniBloc;

public abstract class Structure implements MiniBloc<EnsembleDeDonnees> {
	
	public final String nom;
	
	public Structure(String nom) {
		this.nom = nom;
	}
	
	
	/** Ajoute un champ */
	public abstract void ajouterChamp(String[] donnees);

	public abstract Bloc<?> getBloc(String nomBloc);


	@Override
	public String convertirEnChaineUneValeur(EnsembleDeDonnees valeur) {
		return valeur.getRepresentationEnLigne();
	}
}