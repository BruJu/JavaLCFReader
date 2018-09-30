package fr.bruju.lcfreader.structure.blocs.mini;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.modele.Desequenceur;

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
		
		byte[] octets = new byte[tailleLue];
		
		
		for (int i = 0 ; i != tailleLue ; i++) {
			octets[i] = desequenceur.suivant();
		}
		
		return octets;
	}

	@Override
	public String convertirEnChaineUneValeur(byte[] value) {
		return value.length + ">" + getTableauDOctetsEnChaine(value);
	}
	
	private String getTableauDOctetsEnChaine(byte[] valeurs) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		if (valeurs.length != 0) {
			sb.append(Utilitaire.toHex(valeurs[0]));
			
			for (int i = 1 ; i != valeurs.length ; i++) {
				sb.append(" ").append(Utilitaire.toHex(valeurs[i]));
			}
		}
		
		sb.append("]");
		
		return sb.toString();
	}
}
