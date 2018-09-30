package fr.bruju.lcfreader.structure.blocs.mini;

import fr.bruju.lcfreader.modele.Desequenceur;

public class MiniString implements MiniBloc<String> {

	public static MiniBloc<?> getInstance(boolean versionDeBaseVoulue) {
		return versionDeBaseVoulue ? instance : instanceSequentielle;
	}
	
	private static MiniString instance = new MiniString();
	private static MiniString instanceSequentielle = new Sequentiel();
	
	@Override
	public String extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		return desequenceur.$lireUneChaine(desequenceur.octetsRestants());
	}

	@Override
	public String convertirDefaut(String defaut) {
		return defaut.substring(1, defaut.length() - 1);
	}
	
	public static class Sequentiel extends MiniString {
		@Override
		public String extraireDonnee(Desequenceur desequenceur, int tailleLue) {
			return desequenceur.$lireUneChaine(desequenceur.$lireUnNombreBER());
		}
	}
}
