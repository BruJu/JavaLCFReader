package fr.bruju.lcfreader.structure.structure;

import java.util.Collection;
import java.util.Map;

import fr.bruju.lcfreader.structure.MiniBloc;
import fr.bruju.lcfreader.structure.bloc.Bloc;
import fr.bruju.lcfreader.structure.modele.Desequenceur;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;

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


	@Override
	public EnsembleDeDonnees extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		return null;
	}

}