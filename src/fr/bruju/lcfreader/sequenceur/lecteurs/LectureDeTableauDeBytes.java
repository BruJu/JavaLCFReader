package fr.bruju.lcfreader.sequenceur.lecteurs;

import fr.bruju.lcfreader.sequenceur.sequences.LecteurDeSequence;

public class LectureDeTableauDeBytes implements Desequenceur {
	private int position;
	private byte[] tableau;
	
	public LectureDeTableauDeBytes(int position, byte[] tableau) {
		this.position = position;
		this.tableau = tableau;
	}

	@Override
	public <T> T sequencer(LecteurDeSequence<T> sequenceur) {
		while (true) {
			if (tableau.length == position)
				break;
			
			if (sequenceur.lireOctet(tableau[position++])) {
				break;
			}
		}
		
		return sequenceur.getResultat();
	}
}
