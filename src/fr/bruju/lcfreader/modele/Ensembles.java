package fr.bruju.lcfreader.modele;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.rmobjets.RMEvenement;
import fr.bruju.lcfreader.rmobjets.RMInstruction;
import fr.bruju.lcfreader.rmobjets.RMMap;
import fr.bruju.lcfreader.rmobjets.RMPage;
import fr.bruju.lcfreader.structure.structure.Structures;

public class Ensembles {
	public static RMMap map(String cheminProjet, int idCarte) {
		Structures.initialiser("..\\LectureDeLCF\\ressources\\liblcf\\fields.csv");
		
		String cheminCarte = cheminProjet + "\\Map" + String.format("%04d", idCarte) + ".lmu";

		EnsembleDeDonnees map =  EnsembleDeDonnees.lireFichier(cheminCarte); // -> 35 secondes
		
		
		if (!map.nomStruct.equals("Map"))
			return null;
		
		return new $Map(map, idCarte);
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
