package fr.bruju.lcfreader.modele;

import java.util.List;

import fr.bruju.lcfreader.rmobjets.RMEvenement;
import fr.bruju.lcfreader.rmobjets.RMMap;

public class MapLCF implements RMMap {

	@Override
	public int id() {
		return 0;
	}

	@Override
	public String nom() {
		return null;
	}

	@Override
	public List<RMEvenement> evenements() {
		return null;
	}

}
