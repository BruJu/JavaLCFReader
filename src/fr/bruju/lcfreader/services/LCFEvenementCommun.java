package fr.bruju.lcfreader.services;

import java.util.List;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.rmobjets.RMEvenementCommun;
import fr.bruju.lcfreader.rmobjets.RMInstruction;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;

/**
 * Classe extrayant un évènement depuis l'ensemble de données lu dans un fichier RPG_RT.ldb
 * @author Bruju
 *
 */
public class LCFEvenementCommun implements RMEvenementCommun {
	/* =========
	 * ATTRIBUTS
	 * ========= */
	/** ID de l'évènement commun */
	private final int id;
	/** Nom de l'évènement commun */
	private final String nom;
	/** Liste des instructions */
	private final List<RMInstruction> instructions;
	
	/**
	 * Instancie l'évènement commun depuis son id et l'ensemble de données extrait de RPG_RT.ldb
	 * @param id L'id de l'évènement
	 * @param ensemble L'ensemble de données représentant l'évènement commun
	 */
	@SuppressWarnings("unchecked")
	public LCFEvenementCommun(Integer id, EnsembleDeDonnees ensemble) {
		this.id = id;
		this.nom = ensemble.getDonnee("name", String.class);
		
		List<EnsembleDeDonnees> instructions = ensemble.getDonnee("event_commands", List.class);
		this.instructions = instructions.stream()
						   .map(LCFInstruction::new)
						   .filter(e -> e.code() != 0)
						   .collect(Collectors.toList());
	}

	/* ===================================
	 * IMPLEMENTATION DE RMEVENEMENTCOMMUN
	 * =================================== */
	
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
