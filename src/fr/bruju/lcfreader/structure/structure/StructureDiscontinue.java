package fr.bruju.lcfreader.structure.structure;

import java.util.LinkedHashMap;
import java.util.Map;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.structure.bloc.Bloc;
import fr.bruju.lcfreader.structure.bloc.InstancieurDeBlocs;
import fr.bruju.lcfreader.structure.modele.Desequenceur;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.structure.modele.XMLInsecticide;

public class StructureDiscontinue extends Structure {

	/** Liste des champs connus */
	private Map<Integer, Bloc<?>> champs = new LinkedHashMap<>();
	
	
	public StructureDiscontinue(String nom) {
		super(nom);
	}

	@Override
	public EnsembleDeDonnees extraireDonnee(Desequenceur desequenceur, int parametre) {
		EnsembleDeDonnees ensembleConstruit = new EnsembleDeDonnees(this);
		
		XMLInsecticide.balise("data");
		
		Integer numeroDeBloc;
		int taille;
		
		while (desequenceur.nonVide()) {
			XMLInsecticide.balise("Bloc");
			
			numeroDeBloc = desequenceur.$lireUnNombreBER();
			
			if (numeroDeBloc == 0) {
				XMLInsecticide.fermer();
				XMLInsecticide.fermer();
				return ensembleConstruit;
			}
			
			Bloc<?> bloc = getBloc(numeroDeBloc);
			
			if (bloc == null) {

				XMLInsecticide.xml("Crash");
				
				XMLInsecticide.ecrireDebug();
				
				throw new RuntimeException("Pas de bloc numéro " + Utilitaire.toHex(numeroDeBloc) + " dans " + nom + Utilitaire.toHex(desequenceur.getPosition()));
			}
			
			XMLInsecticide.xml( " ", bloc.nom, " | ");

			taille = desequenceur.$lireUnNombreBER();
			XMLInsecticide.xml(" | ");
			
			if (taille != 0) {
				Desequenceur sousDesequenceur = desequenceur.sousSequencer(taille);
				
				ensembleConstruit.push(bloc.lireOctet(sousDesequenceur, taille));
			}
			XMLInsecticide.fermer();
		}

		XMLInsecticide.fermer();
		
		return ensembleConstruit;
	}


	public Bloc<?> getBloc(Integer numero) {
		return champs.get(numero);
	}
	
	@Override
	public void ajouterChamp(String[] donnees) {
		Bloc<?> bloc = InstancieurDeBlocs.instancier(donnees);
		champs.put(bloc.index, bloc);
	}

	@Override
	public Bloc<?> getBloc(String nomBloc) {
		for (Bloc<?> bloc : champs.values()) {
			if (bloc.nom.equals(nomBloc) && !bloc.estUnChampIndiquantLaTaille()) {
				return bloc;
			}
		}
		
		return null;
	}

}
