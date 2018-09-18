package fr.bruju.lcfreader.modele;

import java.util.List;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.rmobjets.RMEvenement;
import fr.bruju.lcfreader.rmobjets.RMMap;
import fr.bruju.lcfreader.structure.BaseDeDonneesDesStructures;

public class Ensembles {

	public static RMMap map(int idCarte) {
		BaseDeDonneesDesStructures.initialiser("ressources\\liblcf\\fields.csv");
		
		String cheminCarte = "ressources\\cartes\\Map" + String.format("%04d", idCarte) + ".lmu";
		
		EnsembleDeDonnees map = EnsembleDeDonnees.lireFichier(cheminCarte);
		
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
			List<EnsembleDeDonnees> events = map.getDonnee("events", List.class);
			
			return events.stream().map(Ensembles::adapterEvent).collect(Collectors.toList());
		}
	}
	
	private static RMEvenement adapterEvent(EnsembleDeDonnees evenementEnsemble) {
		
		
		return null;
	}
}
