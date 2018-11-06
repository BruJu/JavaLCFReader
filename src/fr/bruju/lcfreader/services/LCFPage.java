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
		if ((flags | decalage) == decalage) {
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

	/**
	 * Donne le numéro du premier interrupteur conditionnant l'utilisation de la page.
	 * @return Le numéro du premier interrupteur conditionnant l'utilisation de la page, ou -1 si désactivé
	 */
	public int conditionInterrupteur1() {
		return conditionsSurPage[0];
	}

	/**
	 * Donne le numéro du second interrupteur conditionnant l'utilisation de la page.
	 * @return Le numéro du second interrupteur conditionnant l'utilisation de la page, ou -1 si désactivé
	 */
	public int conditionInterrupteur2() {
		return conditionsSurPage[1];
	}

	/**
	 * Donne la condition sur une variable conditionnant l'utilisation de la page, null si il n'y en a pas.
	 * @return La condition sur une variable conditionnant l'utilisation de la page, null si il n'y en a pas.
	 */
	public ConditionSurVariable conditionVariable() {
		if (conditionsSurPage[2] == 0) {
			return null;
		}

		return new ConditionSurVariable(conditionsSurPage[2],
				Signe.values()[conditionsSurPage[4]] , conditionsSurPage[3]);
	}

	public int conditionObjet() {
		return conditionsSurPage[5];
	}

	public int conditionHeros() {
		return conditionsSurPage[6];
	}

	public int conditionChrono1() {
		return conditionsSurPage[7];
	}

	public int conditionChrono2() {
		return conditionsSurPage[8];
	}

	public static final class ConditionSurVariable {
		public final int idVariable;
		public final Signe symbole;
		public final int valeur;

		public ConditionSurVariable(int idVariable, Signe symbole, int valeur) {
			this.idVariable = idVariable;
			this.symbole = symbole;
			this.valeur = valeur;
		}
	}

	public enum Signe {
		EGAL, SUPEGAL, INFEGAL, SUP, INF, DIFFERENT
	}
}