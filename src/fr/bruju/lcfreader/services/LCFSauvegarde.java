package fr.bruju.lcfreader.services;

import java.util.List;

import fr.bruju.lcfreader.rmobjets.RMSauvegarde;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;

/**
 * Une sauvegarde se reposant sur la lecture d'un fichier lsd
 *  
 * @author Bruju
 *
 */
public class LCFSauvegarde implements RMSauvegarde {
	/** Nom du leader */
	private final String nomDuLeader;
	/** Liste des variables */
	private final List<Integer> variables;
	/** Liste des interrupteurs */
	private final List<Integer> interrupteurs;
	
	@SuppressWarnings("unchecked")
	public LCFSauvegarde(EnsembleDeDonnees fichierLSD) {
		EnsembleDeDonnees title = fichierLSD.getDonnee("title", EnsembleDeDonnees.class);
		nomDuLeader = title.getDonnee("hero_name", String.class);
		
		EnsembleDeDonnees system = fichierLSD.getDonnee("system", EnsembleDeDonnees.class);
		variables = system.getDonnee("variables", List.class);
		interrupteurs = system.getDonnee("switches", List.class);
	}
	
	@Override
	public String getNomDuLeader() {
		return nomDuLeader;
	}

	@Override
	public boolean getInterrupteur(int numero) {
		return interrupteurs.get(numero - 1) == 1;
	}

	@Override
	public int getVariable(int numero) {
		return variables.get(numero - 1);
	}
}
