package fr.bruju.lcfreader.modele;

import java.util.List;

import fr.bruju.lcfreader.rmobjets.RMEvenement;
import fr.bruju.lcfreader.rmobjets.RMEvenementCommun;
import fr.bruju.lcfreader.rmobjets.RMFabrique;
import fr.bruju.lcfreader.rmobjets.RMMap;

public class FabriqueLCF implements RMFabrique {
	
	private String chemin;

	public FabriqueLCF(String chemin) {
		this.chemin = chemin;
	}

	@Override
	public RMMap map(int idCarte) {
		return Ensembles.map(chemin, idCarte);
	}

	@Override
	public RMEvenement evenement(int idCarte, int idEvenement) {
		return map(idCarte).evenements().stream().filter(ev -> ev.id() == idEvenement).findAny().get();
	}

	@Override
	public RMEvenementCommun evenementCommun(int idEvenementCommun) {
		throw new UnsupportedOperationException("Les évènements communs ne sont pas gérés");
	}

	@Override
	public List<RMMap> maps() {
		throw new UnsupportedOperationException("maps() n'est pas supporté");
	}

	@Override
	public List<RMEvenementCommun> evenementsCommuns() {
		throw new UnsupportedOperationException("Les évènements communs ne sont pas gérés");
	}

}
