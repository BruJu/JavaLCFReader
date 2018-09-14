package random;

import java.util.List;

import random.sequenceur.LecteurDeSequence;

public class LecteurDeBytes {
	private int position;
	private List<Byte> bytes;
	
	public LecteurDeBytes(List<Byte> bytes) {
		this.bytes = bytes;
		position = 0;
	}
	
	public <T> T sequencer(LecteurDeSequence<T> lecteur) {
		while (lecteur.lireOctet(bytes.get(position++))) {
		}
		
		return lecteur.getResultat();
	}
	
	public int getPosition() {
		return position;
	}
}
