package fr.bruju.lcfreader.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.rmobjets.RMEvenement;
import fr.bruju.lcfreader.rmobjets.RMPage;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;

/**
 * Classe extrayant un évènement depuis l'ensemble de données lu dans un fichier MapXXXX.lmu
 * @author Bruju
 *
 */
public class LCFEvenement implements RMEvenement {
	/* =========
	 * ATTRIBUTS
	 * ========= */
	/** ID de l'évènement */
	private final int id;
	/** Nom de l'évènement */
	private final String nom;
	/** Position en X de l'évènement */
	private final int x;
	/** Position en Y de l'évènement */
	private final int y;
	/** Liste des pages de l'évènement */
	private final List<RMPage> pages;
	
	/**
	 * Crée un évènement
	 * @param entree associant l'id de l'évènement et l'ensemble de données le représentant
	 */
	public LCFEvenement(Map.Entry<Integer,EnsembleDeDonnees> entree) {
		EnsembleDeDonnees ensemble = entree.getValue();
		
		id = entree.getKey();
		nom = ensemble.getDonnee("name", String.class);
		x = ensemble.getDonnee("x", Integer.class);
		y = ensemble.getDonnee("y", Integer.class);
		
		@SuppressWarnings("unchecked")
		Map<Integer, EnsembleDeDonnees> pages = ensemble.getDonnee("pages", Map.class);
		this.pages = pages.entrySet().stream().map(LCFPage::new).collect(Collectors.toList());
	}

	/* =============================
	 * IMPLEMENTATION DE RMEVENEMENT
	 * ============================= */
	
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
