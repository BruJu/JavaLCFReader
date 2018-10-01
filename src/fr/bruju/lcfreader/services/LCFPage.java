package fr.bruju.lcfreader.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.rmobjets.RMInstruction;
import fr.bruju.lcfreader.rmobjets.RMPage;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;

public class LCFPage implements RMPage {
	//private EnsembleDeDonnees ensemble;
	private final int id;
	private final List<RMInstruction> instructions;
	

	@SuppressWarnings("unchecked")
	public LCFPage(Map.Entry<Integer, EnsembleDeDonnees> paire) {
		this.id = paire.getKey();
		
		List<EnsembleDeDonnees> ensemblesInstructions = paire.getValue().getDonnee("event_commands", List.class);
		instructions = ensemblesInstructions.stream()
				            .map(ensemble -> LCFInstruction.instancier(ensemble))
				            .filter(e -> e != null)
				            .collect(Collectors.toList());
	}
	
	@Override
	public int id() {
		return id;
	}

	@Override
	public List<RMInstruction> instructions() {
		return instructions;
	}
}