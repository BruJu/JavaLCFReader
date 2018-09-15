package fr.bruju.lcfreader.sequenceur;

public class TailleChaine implements LecteurDeSequence<String> {
	private int etape = 0;
	
	private LecteurDeSequence<?> lecteurActuel = new NombreBER();
	
	@Override
	public boolean lireOctet(byte byteLu) {
		boolean reponse = lecteurActuel.lireOctet(byteLu);
		
		if (!reponse) {
			etape++;
			
			if (etape == 1) {
				lecteurActuel = new Chaine(((Long) lecteurActuel.getResultat()).intValue());
			}
		}
		
		return etape != 2;
	}

	@Override
	public String getResultat() {
		return lecteurActuel.getResultat().toString();
	}
}
