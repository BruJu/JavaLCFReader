package fr.bruju.lcfreader.modele;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.rmobjets.RMEvenement;
import fr.bruju.lcfreader.rmobjets.RMEvenementCommun;
import fr.bruju.lcfreader.rmobjets.RMInstruction;
import fr.bruju.lcfreader.rmobjets.RMMap;
import fr.bruju.lcfreader.rmobjets.RMPage;

public class Ensembles {
	private static Map<Integer, RMMap> cartes = new HashMap<>();
	
	static Map<Integer, RMEvenementCommun> evenements = null;
	
	@SuppressWarnings("unchecked")
	public static RMEvenementCommun ec(String cheminProjet, int idEvenement) {
		if (evenements == null) {
			evenements = new HashMap<>();
			
			String chemin = cheminProjet + "\\RPG_RT.ldb";
			EnsembleDeDonnees bdd =  EnsembleDeDonnees.lireFichier(chemin);
			if (bdd == null) {
				throw new RuntimeException("Fichier bdd illisible");
			}
			
			
			Map<Integer, EnsembleDeDonnees> evenements = bdd.getDonnee("commonevents", Map.class);
			
			for (Map.Entry<Integer, EnsembleDeDonnees> entree : evenements.entrySet()) {
				Ensembles.evenements.put(entree.getKey(), new $EC(entree.getKey(), entree.getValue()));
			}
		}
		
		return evenements.get(idEvenement);
	}
	
	public static class $EC implements RMEvenementCommun {
		private final int id;
		private final EnsembleDeDonnees ensemble;
		
		
		public $EC(Integer id, EnsembleDeDonnees ensemble) {
			this.id = id;
			this.ensemble = ensemble;
		}

		@Override
		public int id() {
			return id;
		}

		@Override
		public String nom() {
			return ensemble.getDonnee("name", String.class);
		}

		@Override
		public List<RMInstruction> instructions() {
			@SuppressWarnings("unchecked")
			List<EnsembleDeDonnees> instructions = ensemble.getDonnee("event_commands", List.class);
			return instructions.stream()
							   .map($Instruction::instancier)
							   .filter(e -> e != null)
							   .collect(Collectors.toList());
		}
		
		
		
	}
	
	
	
	public static RMMap map(String cheminProjet, int idCarte) {
		if (cartes.get(idCarte) == null) {
			String cheminCarte = cheminProjet + "\\Map" + String.format("%04d", idCarte) + ".lmu";

			EnsembleDeDonnees map =  EnsembleDeDonnees.lireFichier(cheminCarte); // -> 35 secondes
			
			
			if (!map.nomStruct.equals("Map"))
				return null;
			
			cartes.put(idCarte, new $Map(map, idCarte));
		}
		
		return cartes.get(idCarte);
	}

	
	private static class $Map implements RMMap {
		private EnsembleDeDonnees map;
		private int idCarte;

		public $Map(EnsembleDeDonnees map, int idCarte) {
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
		public List<RMEvenement> evenements() {
			@SuppressWarnings("unchecked")
			Map<Integer, EnsembleDeDonnees> events = map.getDonnee("events", Map.class);
			
			return events.entrySet().stream().map($Evenement::new).collect(Collectors.toList());
		}
	}
	
	private static class $Evenement implements RMEvenement {
		private EnsembleDeDonnees ensemble;
		private int id;

		private $Evenement(Map.Entry<Integer,EnsembleDeDonnees> paire) {
			this.id = paire.getKey();
			this.ensemble = paire.getValue();
		}

		@Override
		public int id() {
			return id;
		}

		@Override
		public String nom() {
			return ensemble.getDonnee("name", String.class);
		}

		@Override
		public int x() {
			return ensemble.getDonnee("x", Integer.class);
		}

		@Override
		public int y() {
			return ensemble.getDonnee("y", Integer.class);
		}

		@Override
		public List<RMPage> pages() {
			@SuppressWarnings("unchecked")
			Map<Integer, EnsembleDeDonnees> pages = ensemble.getDonnee("pages", Map.class);
			
			return pages.entrySet().stream().map($Page::new).collect(Collectors.toList());
		}
	}
	
	private static class $Page implements RMPage {
		private EnsembleDeDonnees ensemble;
		private int id;

		private $Page(Map.Entry<Integer,EnsembleDeDonnees> paire) {
			this.id = paire.getKey();
			this.ensemble = paire.getValue();
		}
		
		@Override
		public int id() {
			return id;
		}

		@Override
		public List<RMInstruction> instructions() {
			@SuppressWarnings("unchecked")
			List<EnsembleDeDonnees> instructions = ensemble.getDonnee("event_commands", List.class);
			return instructions.stream()
							   .map($Instruction::instancier)
							   .filter(e -> e != null)
							   .collect(Collectors.toList());
		}
	}
	
	
	private static class $Instruction implements RMInstruction {
		private EnsembleDeDonnees ensemble;
		private int[] parametres = null;
		

		private $Instruction(EnsembleDeDonnees ensemble) {
			this.ensemble = ensemble;
		}
		
		private static $Instruction instancier(EnsembleDeDonnees ensemble) {
			$Instruction instruction = new $Instruction(ensemble);
			
			if (instruction.code() == 0) {
				instruction = null;
			}
			
			return instruction;
		}

		@Override
		public int code() {
			return ensemble.getDonnee("code", Integer.class);
		}

		@Override
		public String argument() {
			return ensemble.getDonnee("string", String.class);
		}

		@SuppressWarnings("unchecked")
		@Override
		public int[] parametres() {
			if (parametres == null) {
				parametres = Utilitaire.transformerTableau(ensemble.getDonnee("parameters", List.class));
			}
			
			return parametres;
		}
	}
	
}
