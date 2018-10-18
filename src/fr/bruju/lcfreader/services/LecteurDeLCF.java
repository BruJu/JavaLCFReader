package fr.bruju.lcfreader.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.bruju.lcfreader.rmobjets.RMEvenementCommun;
import fr.bruju.lcfreader.rmobjets.RMFabrique;
import fr.bruju.lcfreader.rmobjets.RMMap;
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
	/* ==========
	 * CONSTANTES
	 * ========== */
	
	/** Liste des noms de catégories dont des noms sont extraits */
	private static final String[] NOMS_A_STOCKER = {"actors", "items", "switches", "variables"};
	
	/* ==============
	 * INITIALISATION
	 * ============== */
	

	/** Répertoire projet sur le disque */
	public final String racine;
	/** Liste des cartes connues */
	private Map<Integer, LCFCarte> cartesConnues = null;
	/** Liste des évènements communs */
	private Map<Integer, RMEvenementCommun> evenementsCommuns = null;
	/** Liste des noms stockes */
	private Map<String, List<String>> nomsStockes = null;
	/** Liste des sauvegardes */
	private Map<Integer, LCFSauvegarde> sauvegardes;
	
	/**
	 * Crée une instance de la classe
	 * @param racine Repertoire du projet sur le disque dur
	 */
	public LecteurDeLCF(String racine) {
		this.racine = racine;
	}
	
	/* ======================
	 * Services de RMFabrique
	 * ====================== */
	
	@Override
	public Map<Integer, RMMap> maps() {
		lireFichierLMT();
		return Collections.unmodifiableMap(cartesConnues);
	}
	
	@Override
	public LCFCarte map(int idCarte) {
		lireFichierLMT();
		return cartesConnues.get(idCarte);
	}

	@Override
	public Map<Integer, RMEvenementCommun> evenementsCommuns() {
		lireFichierLDB();
		return evenementsCommuns;
	}

	@Override
	public LCFSauvegarde lireSauvegarde(int numero) {
		chargerSauvegarde(numero);
		
		return sauvegardes.get(numero);
	}
	
	/**
	 * Lit une sauvegarde (sans la mettre en cache) dont le nom est donné
	 * @param chemin Le chemin vers le fichier de la sauvegarde
	 * @return La sauvegarde
	 */
	public static LCFSauvegarde lireSauvegarde(String chemin) {
		return new LCFSauvegarde(EnsembleDeDonnees.lireFichier(chemin));
	}
	
	/* ======================
	 * Service supplémentaire
	 * ====================== */
	
	/**
	 * Donne la liste de tous les identifiants d'une catégorie
	 * @param nomCategorie Une de ces chaînes : "actors", "items", "switches", "variables"
	 * @return La liste des noms
	 */
	public List<String> getListeDeNoms(String nomCategorie) {
		lireFichierLDB();
		return nomsStockes.get(nomCategorie);
	}
	
	
	/* ====================
	 * Lecture des fichiers
	 * ==================== */
	
	/**
	 * Lit le fichier RPG_RT.lmt afin de connaître la liste des cartes ainsi que leur nom 
	 */
	@SuppressWarnings("unchecked")
	private void lireFichierLMT() {
		if (cartesConnues != null) {
			return;
		}
		
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
		nomsStockes = new HashMap<>();
		
		EnsembleDeDonnees donnees = EnsembleDeDonnees.lireFichier(racine + "RPG_RT.ldb");
		
		@SuppressWarnings("unchecked")
		Map<Integer, EnsembleDeDonnees> evenements = donnees.getDonnee("commonevents", Map.class);
		
		for (Map.Entry<Integer, EnsembleDeDonnees> entree : evenements.entrySet()) {
			evenementsCommuns.put(entree.getKey(), new LCFEvenementCommun(entree.getKey(), entree.getValue()));
		}
		
		for (String nomCategorie : NOMS_A_STOCKER) {
			nomsStockes.put(nomCategorie, extraireListeDeNoms(donnees, nomCategorie));
		}
	}
	
	/**
	 * Extrait la liste des "name" pour le bloc demandé
	 * @param donnees L'ensemble de données contenant la donné à parcourir 
	 * @param nomBloc Le nom du bloc à parcourir (ce bloc doit être un tableau indexé d'ensembles)
	 * @return La liste tous les champs name
	 */
	private List<String> extraireListeDeNoms(EnsembleDeDonnees donnees, String nomBloc) {
		@SuppressWarnings("unchecked")
		Map<Integer, EnsembleDeDonnees> ensembles = donnees.getDonnee(nomBloc, Map.class);
		
		List<String> valeurs = new ArrayList<>();
		
		for (int i = 1 ; i <= ensembles.values().size() ; i++) {
			valeurs.add(ensembles.get(i).getDonnee("name", String.class));
		}
		
		return valeurs;
	}
	
	/**
	 * Charge dans la mémoire la sauvegarde demandée
	 * @param numero Le numéro de la sauvegarde
	 */
	private void chargerSauvegarde(int numero) {
		if (sauvegardes == null) {
			sauvegardes = new HashMap<>();
		}
		
		if (!sauvegardes.containsKey(numero)) {
			String nomFichier = racine + "Save" + String.format("%02d", numero) + ".lsd";
			EnsembleDeDonnees ensembleLSD = EnsembleDeDonnees.lireFichier(nomFichier);
			sauvegardes.put(numero, new LCFSauvegarde(ensembleLSD));
		}
	}

	
	/**
	 * Met dans le StringBuilder le nom de carte dont l'id est donné ainsi que de tous ses pères
	 * @param sb Le StringBuilder à remplir
	 * @param id L'id de la première carte dont il faut inscrire le nom
	 * @return void. sb est modifiée pour se voir ajouter de la chaîne "Père 1-Père 2-Père 3-"
	 */
	void construireNom(StringBuilder sb, int id) {
		if (id != 0) {
			LCFCarte carte = cartesConnues.get(id);
			construireNom(sb, carte.idDuPere);
			sb.append(carte.nomDeLaMap).append("-");
		}
	}
}
