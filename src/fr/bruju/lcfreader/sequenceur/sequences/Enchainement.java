package fr.bruju.lcfreader.sequenceur.sequences;

import java.util.function.Function;
import java.util.function.Supplier;

public class Enchainement<T, R> implements LecteurDeSequence<R> {

	private LecteurDeSequence<T> premierLecteur;
	private Function<T, LecteurDeSequence<R>> fonction;
	private LecteurDeSequence<R> secondLecteur = null;
	private Supplier<R> defaut;

	public Enchainement(LecteurDeSequence<T> premierLecteur,
			Function<T, LecteurDeSequence<R>> secondLecteur,
			Supplier<R> resultatSiVide) {
		this.premierLecteur = premierLecteur;
		this.fonction = secondLecteur;
		this.defaut = resultatSiVide;
	}

	@Override
	public boolean lireOctet(byte octet) {
		if (premierLecteur != null) {
			if (!premierLecteur.lireOctet(octet)) {
				T resultat = premierLecteur.getResultat();
				
				premierLecteur = null;
				
				if (resultat != null) {
					secondLecteur = fonction.apply(resultat);
				} else {
					return false;
				}
			}
			
			return true;
		} else {
			return secondLecteur.lireOctet(octet);
		}
	}

	@Override
	public R getResultat() {
		return secondLecteur == null ? defaut.get() : secondLecteur.getResultat();
	}
	
	
}
