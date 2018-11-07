package fr.bruju.lcfreader.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.rmobjets.ConditionSurVariable;
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
	/** Conditions sur page */
	private int[] conditionsSurPage;

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

		remplirConditionsDePage(entree.getValue().getDonnee("condition", EnsembleDeDonnees.class));
	}



	private void remplirConditionsDePage(EnsembleDeDonnees condition) {
		int flags = condition.getDonnee("flags", Integer.class);

		conditionsSurPage = new int[9];

		remplirConditions(condition, flags, 0, 1, "switch_a_id");
		remplirConditions(condition, flags, 1, 2, "switch_b_id");
		remplirConditions(condition, flags, 2, 4, "variable_id");
		remplirConditions(condition, flags, 3, 4, "variable_value");
		remplirConditions(condition, flags, 4, 4, "compare_operator");
		remplirConditions(condition, flags, 5, 8, "item_id");
		remplirConditions(condition, flags, 6, 16, "actor_id");
		remplirConditions(condition, flags, 7, 32, "timer_sec");
		remplirConditions(condition, flags, 8, 64, "timer2_sec");
	}

	private void remplirConditions(EnsembleDeDonnees condition, int flags, int position, int decalage, String nom) {
		if ((flags & decalage) == decalage) {
			conditionsSurPage[position] = condition.getDonnee(nom, Integer.class);
		} else {
			conditionsSurPage[position] = -1;
		}
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

	/* ===============================
	 * FONCTIONNALITES SUPPLEMENTAIRES
	 * =============================== */

	@Override
	public int conditionInterrupteur1() {
		return conditionsSurPage[0];
	}

	@Override
	public int conditionInterrupteur2() {
		return conditionsSurPage[1];
	}

	@Override
	public ConditionSurVariable conditionVariable() {
		if (conditionsSurPage[2] == -1) {
			return null;
		}

		return new ConditionSurVariable(conditionsSurPage[2],
				ConditionSurVariable.Signe.values()[conditionsSurPage[4]] , conditionsSurPage[3]);
	}

	@Override
	public int conditionObjet() {
		return conditionsSurPage[5];
	}

	@Override
	public int conditionHeros() {
		return conditionsSurPage[6];
	}

	@Override
	public int conditionChrono1() {
		return conditionsSurPage[7];
	}

	@Override
	public int conditionChrono2() {
		return conditionsSurPage[8];
	}
}