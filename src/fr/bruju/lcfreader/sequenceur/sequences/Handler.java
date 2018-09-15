package fr.bruju.lcfreader.sequenceur.sequences;

import fr.bruju.lcfreader.structure.Data;

public interface Handler<T> {
	public Data<T> traiter(byte octet);
}
