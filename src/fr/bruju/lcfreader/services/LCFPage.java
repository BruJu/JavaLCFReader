package fr.bruju.lcfreader.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.rmobjets.RMInstruction;
import fr.bruju.lcfreader.rmobjets.RMPage;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;

/**
 * Classe extrayant une page d'un évènement depuis l'ensemble de données lu dans un fichier MapXXXX.lmu
 * @author Bruju
 *
 */
public class LCFPage implements RMPage {
	/* =========
	 * ATTRIBUTS
	 * ========= */
	/** Id de la page */
	private final int id;
	/** Liste des instructions */
	private final List<RMInstruction> instructions;
	

	/**
	 * Crée une page
	 * @param entree L'entrée associant le numéro la page à l'ensemble de données contenant les données de la page
	 */
	@SuppressWarnings("unchecked")
	public LCFPage(Map.Entry<Integer, EnsembleDeDonnees> entree) {
		this.id = entree.getKey();
		
		List<EnsembleDeDonnees> ensemblesInstructions = entree.getValue().getDonnee("event_commands", List.class);
		instructions = ensemblesInstructions.stream()
				            .map(LCFInstruction::new)
							.filter(e -> e.code() != 0)
				            .collect(Collectors.toList());
	}
	

	/* ========================
	 * IMPLEMENTATION DE RMPAGE
	 * ======================== */
	
	@Override
	public int id() {
		return id;
	}

	@Override
	public List<RMInstruction> instructions() {
		return instructions;
	}
}