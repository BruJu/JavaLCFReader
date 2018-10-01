package fr.bruju.lcfreader.structure.types;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.structure.MiniBloc;
import fr.bruju.lcfreader.structure.modele.Desequenceur;

public class MiniInconnu implements MiniBloc<byte[]> {
	/** Singleton simplifié */
	// On part du principe qu'une instance sera forcément utilisée
	public static final MiniInconnu instance = new MiniInconnu();
	
	private MiniInconnu() {}
	

	@Override
	public byte[] extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		if (tailleLue == -1) {
			throw new RuntimeException("Bloc inconnu en lecture en série détecté pour bloc inconnu");
		}
		
		return desequenceur.extrairePortion(tailleLue);
	}

	@Override
	public String convertirEnChaineUneValeur(byte[] value) {
		return value.length + ">" + Utilitaire.bytesToString(value);
	}
}
