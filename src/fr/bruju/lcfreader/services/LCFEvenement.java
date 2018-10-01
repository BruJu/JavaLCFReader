package fr.bruju.lcfreader.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.rmobjets.RMEvenement;
import fr.bruju.lcfreader.rmobjets.RMPage;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;

public class LCFEvenement implements RMEvenement {
	private final int id;
	private final String nom;
	private final int x;
	private final int y;
	
	private List<RMPage> pages;
	

	public LCFEvenement(Map.Entry<Integer,EnsembleDeDonnees> paire) {
		EnsembleDeDonnees ensemble = paire.getValue();
		
		id = paire.getKey();
		nom = ensemble.getDonnee("name", String.class);
		x = ensemble.getDonnee("x", Integer.class);
		y = ensemble.getDonnee("y", Integer.class);
		

		@SuppressWarnings("unchecked")
		Map<Integer, EnsembleDeDonnees> pages = ensemble.getDonnee("pages", Map.class);
		
		this.pages = pages.entrySet().stream().map(LCFPage::new).collect(Collectors.toList());
	}

	@Override
	public int id() {
		return id;
	}

	@Override
	public String nom() {
		return nom;
	}

	@Override
	public int x() {
		return x;
	}

	@Override
	public int y() {
		return y;
	}

	@Override
	public List<RMPage> pages() {
		return pages;
	}
}
