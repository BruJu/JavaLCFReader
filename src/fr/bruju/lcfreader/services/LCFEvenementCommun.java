package fr.bruju.lcfreader.services;

import java.util.List;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.rmobjets.RMEvenementCommun;
import fr.bruju.lcfreader.rmobjets.RMInstruction;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;

public class LCFEvenementCommun implements RMEvenementCommun {
	private final int id;
	private final String nom;
	private final List<RMInstruction> instructions;
	
	
	@SuppressWarnings("unchecked")
	public LCFEvenementCommun(Integer id, EnsembleDeDonnees ensemble) {
		this.id = id;
		this.nom = ensemble.getDonnee("name", String.class);
		
		List<EnsembleDeDonnees> instructions = ensemble.getDonnee("event_commands", List.class);
		this.instructions = instructions.stream()
						   .map(LCFInstruction::instancier)
						   .filter(e -> e != null)
						   .collect(Collectors.toList());
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
	public List<RMInstruction> instructions() {
		return instructions;
	}
}
