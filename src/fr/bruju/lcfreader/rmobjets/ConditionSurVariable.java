package fr.bruju.lcfreader.rmobjets;

public final class ConditionSurVariable {
	public final int idVariable;
	public final Signe symbole;
	public final int valeur;

	public ConditionSurVariable(int idVariable, Signe symbole, int valeur) {
		this.idVariable = idVariable;
		this.symbole = symbole;
		this.valeur = valeur;
	}

	public enum Signe {
		EGAL, SUPEGAL, INFEGAL, SUP, INF, DIFFERENT
	}
}

