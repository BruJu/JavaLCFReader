package fr.bruju.lcfreader.structure.blocs.mini;

import fr.bruju.lcfreader.modele.Desequenceur;

public class MiniBER implements MiniBloc<Integer> {
	public static final MiniBER instance = new MiniBER();
	
	@Override
	public Integer extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		return desequenceur.$lireUnNombreBER();
	}
}
