package fr.bruju.lcfreader.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.Main;
import fr.bruju.lcfreader.rmobjets.RMEvenementCommun;
import fr.bruju.lcfreader.rmobjets.RMFabrique;
import fr.bruju.lcfreader.rmobjets.RMMap;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;

public class Arborescence implements RMFabrique {
	/* ======================
	 * Services de RMFabrique
	 * ====================== */

	private List<RMMap> maps = null;
	
	@Override
	public List<RMMap> maps() {
		if (maps == null) {
			maps = cartesConnues.values().stream().collect(Collectors.toList());
		}
		
		return maps;
	}
	
	
	@Override
	public Carte map(int idCarte) {
		return cartesConnues.get(idCarte);
	}
	
	

	/* ======================
	 * Misc.
	 * ====================== */

	
	public final String racine;
	
	
	private String nomDuProjet;
	
	private Map<Integer, Carte> cartesConnues;
	
	
	
	public void afficher() {
		System.out.println(nomDuProjet);
		cartesConnues.forEach(this::afficher);
	}
	
	
	
	
	
	
	
	
	private void afficher(Integer id, Carte s) {
		System.out.print(id + " : " + s.nom() + " " + s.id());

		System.out.println();
		
		if (s.estCharge()) {
			Main.afficherRMMap(s);
			System.out.println();
		}
	}








	public void charger(int id) {
		cartesConnues.get(id).charger();
	}
	
	
	
	
	
	public Arborescence(String racine) {
		this.racine = racine;
		lireFichierLMT();
	}
	
	
	@SuppressWarnings("unchecked")
	private void lireFichierLMT() {
		EnsembleDeDonnees donnees = EnsembleDeDonnees.lireFichier(racine + "RPG_RT.lmt");
		
		cartesConnues = new HashMap<>();
		
		Map<Integer, EnsembleDeDonnees> maps = donnees.getDonnee("maps", Map.class);
		
		for (Map.Entry<Integer, EnsembleDeDonnees> entree : maps.entrySet()) {
			EnsembleDeDonnees carte = entree.getValue();
			
			String nomDeLaMap = carte.getDonnee("name", String.class);
			
			if (entree.getKey() == 0) {
				nomDuProjet = nomDeLaMap;
			} else {
				int type = carte.getDonnee("type", Integer.class);
				
				if (type == 1) { // type 0 = racine, 1 = carte, 2 = zone
					int idDuPere = carte.getDonnee("parent_map", Integer.class);
					cartesConnues.put(entree.getKey(), new Carte(this, nomDeLaMap, entree.getKey(), idDuPere));
				}
			}
		}
	}








	public void construireNom(StringBuilder sb, int idDuPere) {
		while (idDuPere != 0) {
			Carte carte = cartesConnues.get(idDuPere);
			sb.append(carte.nomDeLaMap).append(" - ");
			idDuPere = carte.idDuPere;
		}
	}


	
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	
	private Map<Integer, RMEvenementCommun> evenementsCommuns = null;

	
	@SuppressWarnings("unchecked")
	private void lireFichierLDB() {
		if (evenementsCommuns != null)
			return;
		
		evenementsCommuns = new HashMap<>();
		
		
		EnsembleDeDonnees donnees = EnsembleDeDonnees.lireFichier(racine + "RPG_RT.ldb");
		
		
		Map<Integer, EnsembleDeDonnees> evenements = donnees.getDonnee("commonevents", Map.class);
		
		for (Map.Entry<Integer, EnsembleDeDonnees> entree : evenements.entrySet()) {
			evenementsCommuns.put(entree.getKey(), new LCFEvenementCommun(entree.getKey(), entree.getValue()));
		}
	}








	@Override
	public RMEvenementCommun evenementCommun(int idEvenementCommun) {
		lireFichierLDB();
		
		return evenementsCommuns.get(idEvenementCommun);
	}







	@Override
	public Map<Integer, RMEvenementCommun> evenementsCommuns() {
		lireFichierLDB();

		return evenementsCommuns;
	}


}
