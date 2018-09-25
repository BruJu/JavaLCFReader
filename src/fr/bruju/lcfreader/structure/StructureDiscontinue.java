package fr.bruju.lcfreader.structure;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.structure.blocs.Bloc;
import fr.bruju.lcfreader.structure.blocs.BlocInconnu;
import fr.bruju.lcfreader.structure.blocs.Blocs;
import fr.bruju.lcfreader.structure.blocs.Champ;

public class StructureDiscontinue extends Structure {

	/** Liste des champs connus */
	private Map<Integer, Bloc<?>> champs = new LinkedHashMap<>();
	
	
	public StructureDiscontinue(String nom) {
		super(nom);
	}

	@Override
	public EnsembleDeDonnees lireOctet(Desequenceur desequenceur, int parametre) {
		EnsembleDeDonnees ensembleConstruit = new EnsembleDeDonnees(this);
		
		// XMLInsecticide.balise("data");
		
		Integer numeroDeBloc;
		int taille;
		
		while (desequenceur.nonVide()) {
			// XMLInsecticide.balise("Bloc");
			
			byte octet = desequenceur.suivant();
			numeroDeBloc = Byte.toUnsignedInt(octet);
			// XMLInsecticide.xml(Utilitaire.toHex(octet));
			
			if (numeroDeBloc == 0) {
				// XMLInsecticide.fermer();
				// XMLInsecticide.fermer();
				return ensembleConstruit;
			}
			
			Bloc<?> bloc = getBloc(numeroDeBloc);
			
			if (bloc == null) {
				bloc = new BlocInconnu(new Champ(81, "Bloc 81", false, "Bloc 81"), "Bloc 81");
				/*
				try {
					// XMLInsecticide.ecrireDebug();
				} catch (IOException e) {
					e.printStackTrace();
				}
				throw new RuntimeException("Bloc inconnu");
				*/
			}
			
			// XMLInsecticide.xml( " " +bloc.nom + " | ");

			taille = desequenceur.$lireUnNombreBER();
			// XMLInsecticide.xml(" | ");
			
			if (taille != 0) {
				Desequenceur sousDesequenceur = desequenceur.sousSequencer(taille);
				
				ensembleConstruit.push(bloc.lireOctet(sousDesequenceur, taille));
				/*
				if (parametre != -1 && sousDesequenceur.nonVide()) {
					try {
						// XMLInsecticide.ecrireDebug();
					} catch (IOException e) {
						e.printStackTrace();
					}
					throw new RuntimeException("Lecture d'un bloc non terminé " + sousDesequenceur.octetsRestants());
				}
				*/
			}
			// XMLInsecticide.fermer();
		}

		// XMLInsecticide.fermer();
		
		return ensembleConstruit;
	}


	public Bloc<?> getBloc(Integer numero) {
		return champs.get(numero);
	}
	
	@Override
	public void ajouterChamp(String[] donnees) {
		Bloc<?> bloc = Blocs.instancier(donnees);
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
