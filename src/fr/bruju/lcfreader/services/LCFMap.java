package fr.bruju.lcfreader.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.rmobjets.RMEvenement;
import fr.bruju.lcfreader.rmobjets.RMMap;
import fr.bruju.lcfreader.services.Ensembles.$Evenement;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;

public class LCFMap implements RMMap {
	private EnsembleDeDonnees map;
	private int idCarte;

	public LCFMap(EnsembleDeDonnees map, int idCarte) {
		this.map = map;
		this.idCarte = idCarte;
	}

	@Override
	public int id() {
		return idCarte;
	}

	@Override
	public String nom() {
		return "Map" + String.format("%04d", idCarte);
	}

	@Override
	public Map<Integer, RMEvenement> evenements() {
		@SuppressWarnings("unchecked")
		Map<Integer, EnsembleDeDonnees> events = map.getDonnee("events", Map.class);
		
		Map<Integer, RMEvenement> carte = new HashMap<>();
		
		events.entrySet().forEach(entree -> carte.put(entree.getKey(), new $Evenement(entree)));
		
		return carte;
	}
}