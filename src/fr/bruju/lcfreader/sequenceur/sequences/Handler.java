package fr.bruju.lcfreader.sequenceur.sequences;

import fr.bruju.lcfreader.structure.Data;

public interface Handler {
	public Data<?> traiter(byte octet);
}
