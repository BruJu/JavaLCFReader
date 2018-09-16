package fr.bruju.lcfreader.sequenceur.sequences;

public class TailleChaine implements LecteurDeSequence<String> {
	private int etape = 0;
	
	private LecteurDeSequence<?> lecteurActuel = new NombreBER();
	
	@Override
	public boolean lireOctet(byte byteLu) {
		boolean reponse = lecteurActuel.lireOctet(byteLu);
		
		if (!reponse) {
			etape++;
			
			if (etape == 1) {
				int taille = ((Long) lecteurActuel.getResultat()).intValue();

				lecteurActuel = new Chaine(taille);
				
				if (taille == 0) {
					etape = 2;
				}
				
				
				
			}
		}
		
		return etape != 2;
	}

	@Override
	public String getResultat() {
		return lecteurActuel.getResultat().toString();
	}
}
