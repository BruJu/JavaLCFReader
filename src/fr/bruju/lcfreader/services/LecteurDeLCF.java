package fr.bruju.lcfreader.services;

import java.util.HashMap;
import java.util.Map;

import fr.bruju.lcfreader.rmobjets.RMEvenementCommun;
import fr.bruju.lcfreader.rmobjets.RMFabrique;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;

/**
 * Une classe permettant de lire les fichiers LCF de RPG Maker 2003 et de fournir les objets permettant d'en extraire
 * les instructions. Cette classe se repose sur les fichiers RPG_RT.lmt pour l'arborescence, des différents fichiers
 * MapXXXX.lmu pour les cartes et de RPG_RT.ldb pour la base de données (évènements communs). <br>
 * La lecture des fichiers se fait à la volée, c'est à dire qu'initialement, seul le fichier .lmt est lu. Les autres
 * fichiers ne sont lus que si une méthode exige de connaître les données inscrites dedans (fonctionnement paresseux). 
 * 
 * @author Bruju
 *
 */
public class LecteurDeLCF implements RMFabrique {
	/* ==============
	 * INITIALISATION
	 * ============== */
	
	/** Répertoire projet sur le disque */
	public final String racine;
	/** Liste des cartes connues */
	private Map<Integer, LCFCarte> cartesConnues;
	/** Liste des évènements communs */
	private Map<Integer, RMEvenementCommun> evenementsCommuns = null;
	
	/**
	 * Crée une instance de la classe
	 * @param racine Repertoire du projet sur le disque dur
	 */
	public LecteurDeLCF(String racine) {
		this.racine = racine;
		lireFichierLMT();
	}
	
	/* ======================
	 * Services de RMFabrique
	 * ====================== */
	
	@Override
	public Map<Integer, LCFCarte> maps() {
		return cartesConnues;
	}
	
	@Override
	public LCFCarte map(int idCarte) {
		return cartesConnues.get(idCarte);
	}

	@Override
	public Map<Integer, RMEvenementCommun> evenementsCommuns() {
		lireFichierLDB();
		return evenementsCommuns;
	}
	
	
	/* ====================
	 * Lecture des fichiers
	 * ==================== */
	
	/**
	 * Lit le fichier RPG_RT.lmt afin de connaître la liste des cartes ainsi que leur nom 
	 */
	@SuppressWarnings("unchecked")
	private void lireFichierLMT() {
		EnsembleDeDonnees donnees = EnsembleDeDonnees.lireFichier(racine + "RPG_RT.lmt");
		
		cartesConnues = new HashMap<>();
		
		Map<Integer, EnsembleDeDonnees> maps = donnees.getDonnee("maps", Map.class);
		
		for (Map.Entry<Integer, EnsembleDeDonnees> entree : maps.entrySet()) {
			EnsembleDeDonnees carte = entree.getValue();
			
			String nomDeLaMap = carte.getDonnee("name", String.class);
			
			if (entree.getKey() != 0) {
				int type = carte.getDonnee("type", Integer.class);
				
				if (type == 1) { // type 0 = racine, 1 = carte, 2 = zone
					int idDuPere = carte.getDonnee("parent_map", Integer.class);
					cartesConnues.put(entree.getKey(), new LCFCarte(this, nomDeLaMap, entree.getKey(), idDuPere));
				}
			}
		}
	}

	/**
	 * Lit le fichier RPG_RT.LDB afin de connaître tous les évènements communs
	 */
	private void lireFichierLDB() {
		if (evenementsCommuns != null)
			return;
		
		evenementsCommuns = new HashMap<>();
		
		EnsembleDeDonnees donnees = EnsembleDeDonnees.lireFichier(racine + "RPG_RT.ldb");
		
		@SuppressWarnings("unchecked")
		Map<Integer, EnsembleDeDonnees> evenements = donnees.getDonnee("commonevents", Map.class);
		
		for (Map.Entry<Integer, EnsembleDeDonnees> entree : evenements.entrySet()) {
			evenementsCommuns.put(entree.getKey(), new LCFEvenementCommun(entree.getKey(), entree.getValue()));
		}
	}
	
	/**
	 * Met dans le StringBuilder le nom de carte dont l'id est donné ainsi que de tous ses pères
	 * @param sb Le StringBuilder à remplir
	 * @param id L'id de la première carte dont il faut inscrire le nom
	 * @return void. sb est modifiée pour se voir ajouter de la chaîne "Père 1-Père 2-Père 3-"
	 */
	void construireNom(StringBuilder sb, int id) {
		while (id != 0) {
			LCFCarte carte = cartesConnues.get(id);
			sb.append(carte.nomDeLaMap).append("-");
			id = carte.idDuPere;
		}
	}
}
