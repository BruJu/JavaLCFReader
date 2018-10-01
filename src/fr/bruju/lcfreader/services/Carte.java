package fr.bruju.lcfreader.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.rmobjets.RMEvenement;
import fr.bruju.lcfreader.rmobjets.RMMap;
import fr.bruju.lcfreader.services.Ensembles.$Evenement;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;

public class Carte implements RMMap {
	public final String nomDeLaMap;
	public final int idDuPere;
	private int id;
	private Arborescence arborescence;
	
	private String nomComplet = null;

	private Map<Integer, RMEvenement> evenements = null;
	
	public Carte(Arborescence arborescence, String nomDeLaMap, int id, int idDuPere) {
		this.arborescence = arborescence;
		this.nomDeLaMap = nomDeLaMap;
		this.idDuPere = idDuPere;
		this.id = id;
	}

	@SuppressWarnings("unchecked")
	public void charger() {
		if (!estCharge()) {
			String cheminComplet = arborescence.racine + "Map" + String.format("%04d", id) + ".lmu";
			EnsembleDeDonnees map = EnsembleDeDonnees.lireFichier(cheminComplet);
			Map<Integer, EnsembleDeDonnees> events = map.getDonnee("events", Map.class);
			
			
			evenements = new HashMap<>();
			events.entrySet().forEach(entree -> evenements.put(entree.getKey(), new $Evenement(entree)));
		}
	}

	@Override
	public int id() {
		return id;
	}

	@Override
	public String nom() {
		if (nomComplet == null) {
			StringBuilder sb = new StringBuilder();
			arborescence.construireNom(sb, idDuPere);
			sb.append(nomDeLaMap);
			nomComplet = sb.toString();
		}
		
		return nomComplet;
	}

	@Override
	public Map<Integer, RMEvenement> evenements() {
		charger();
		
		return evenements;
	}

	public boolean estCharge() {
		return evenements != null;
	}
}