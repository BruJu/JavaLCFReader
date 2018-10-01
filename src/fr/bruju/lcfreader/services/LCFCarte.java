package fr.bruju.lcfreader.services;

import java.util.HashMap;
import java.util.Map;

import fr.bruju.lcfreader.rmobjets.RMEvenement;
import fr.bruju.lcfreader.rmobjets.RMMap;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;

/**
 * Classe permettant d'extraire les instructions d'une carte en se reposant sur le fichier MapXXXX.lmu
 * 
 * @author Bruju
 *
 */
public class LCFCarte implements RMMap {
	/* =========
	 * ATTRIBUTS
	 * ========= */
	
	/** ID de la carte */
	private int id;
	/** Nom de la carte */
	public final String nomDeLaMap;
	/** Evènements composant la carte (implémentation paresseuse) */
	private Map<Integer, RMEvenement> evenements = null;
	
	/** Chemin vers la carte (implémentation paresseuse) */
	private String nomComplet = null;
	/** ID du père de la carte dans l'arborescence */
	final int idDuPere;
	/** Lecteur de LCF rattaché à la carte */
	private LecteurDeLCF lecteurDeLCF;
	
	/**
	 * Crée une instance de la carte
	 * @param lecteurDeLCF Lecteur de LCF ayant généré la carte
	 * @param nomDeLaMap Nom de la carte dans le fichier RPG_RT.lmt
	 * @param id Le numéro de la carte
	 * @param idDuPere Le numéro du père de la carte
	 */
	public LCFCarte(LecteurDeLCF lecteurDeLCF, String nomDeLaMap, int id, int idDuPere) {
		this.lecteurDeLCF = lecteurDeLCF;
		this.nomDeLaMap = nomDeLaMap;
		this.idDuPere = idDuPere;
		this.id = id;
	}

	/* =======================
	 * IMPLEMENTATION DE RMMAP
	 * ======================= */
	
	@Override
	public int id() {
		return id;
	}

	@Override
	public String nom() {
		remplirNom();
		return nomComplet;
	}

	@Override
	public Map<Integer, RMEvenement> evenements() {
		remplirEvenements();
		return evenements;
	}

	/* =========================
	 * IMPLEMENTATION PARESSEUSE
	 * ========================= */
	
	/** Rempli le nom de la carte avec le chemin complet */
	private void remplirNom() {
		if (nomComplet == null) {
			StringBuilder sb = new StringBuilder();
			lecteurDeLCF.construireNom(sb, idDuPere);
			sb.append(nomDeLaMap);
			nomComplet = sb.toString();
		}
	}
	
	/** Rempli la liste des évènements en lisant le fichier MapXXXX.lmu */
	@SuppressWarnings("unchecked")
	private void remplirEvenements() {
		if (evenements == null) {
			String cheminComplet = lecteurDeLCF.racine + "Map" + String.format("%04d", id) + ".lmu";
			EnsembleDeDonnees map = EnsembleDeDonnees.lireFichier(cheminComplet);
			Map<Integer, EnsembleDeDonnees> events = map.getDonnee("events", Map.class);
			
			
			evenements = new HashMap<>();
			events.entrySet().forEach(entree -> evenements.put(entree.getKey(), new LCFEvenement(entree)));
		}
	}
}