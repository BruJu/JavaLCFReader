package fr.bruju.lcfreader.rmobjets;

import fr.bruju.lcfreader.services.LCFPage;

import java.util.List;

/**
 * Représente une page d'un évènement dans RPG Maker
 * 
 * @author Bruju
 *
 */
public interface RMPage {
	/**
	 * Donne l'identifiant de la page
	 * @return L'identifiant de la page
	 */
	public int id();
	
	/**
	 * Donne la liste des instructions de la page
	 * @return La liste des instructions de la page
	 */
	public List<RMInstruction> instructions();

	/**
	 * Donne le numéro du premier interrupteur conditionnant l'utilisation de la page.
	 * @return Le numéro du premier interrupteur conditionnant l'utilisation de la page, ou -1 si désactivé
	 */
	public default int conditionInterrupteur1() {
		throw new UnsupportedOperationException("Instruction non supportée");
	}

	/**
	 * Donne le numéro du second interrupteur conditionnant l'utilisation de la page.
	 * @return Le numéro du second interrupteur conditionnant l'utilisation de la page, ou -1 si désactivé
	 */
	public default int conditionInterrupteur2() {
		throw new UnsupportedOperationException("Instruction non supportée");
	}

	/**
	 * Donne la condition sur une variable conditionnant l'utilisation de la page, null si il n'y en a pas.
	 * @return La condition sur une variable conditionnant l'utilisation de la page, null si il n'y en a pas.
	 */
	public default ConditionSurVariable conditionVariable() {
		throw new UnsupportedOperationException("Instruction non supportée");
	}

	public default int conditionObjet() {
		throw new UnsupportedOperationException("Instruction non supportée");
	}

	public default int conditionHeros() {
		throw new UnsupportedOperationException("Instruction non supportée");
	}

	public default int conditionChrono1() {
		throw new UnsupportedOperationException("Instruction non supportée");
	}

	public default int conditionChrono2() {
		throw new UnsupportedOperationException("Instruction non supportée");
	}
}
