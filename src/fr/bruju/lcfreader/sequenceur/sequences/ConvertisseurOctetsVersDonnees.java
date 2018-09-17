package fr.bruju.lcfreader.sequenceur.sequences;

import java.util.function.Function;

import fr.bruju.lcfreader.structure.Donnee;

/**
 * Interface permettant de convertir une série d'octets qui sont reçus à la suite en une donnée intelligible.
 * 
 * @author Bruju
 *
 * @param <T> Le type de données généré par le convertisseur
 */
public interface ConvertisseurOctetsVersDonnees<T> {
	/**
	 * Reçoit l'octet suivant composant la donnée sous forme binaire.
	 * 
	 * @param octet L'octet reçu
	 * @return null si la lecture n'est pas terminée (l'octet reçu n'est pas le dernier). L'objet construit si tous les
	 *         octets ont été reçus.
	 */
	public Donnee<T> accumuler(byte octet);

	/**
	 * Cette classe fournit un moyen générique de transformer un LecteurDeSequences en ConvertisseurOctetsVersDonnees
	 * 
	 * @author Bruju
	 *
	 * @param <S> Le type de données
	 */
	public static class ViaSequenceur<S> implements ConvertisseurOctetsVersDonnees<S> {
		/** Lecteur utilisé */
		private LecteurDeSequence<S> lecteur;
		/** Fonction de convertion du résultat en données */
		private Function<S, Donnee<S>> traiterResultat;

		/**
		 * Crée un ConvertisseurOctetsVersDonnees utilisant un lecteur de séquences
		 * 
		 * @param lecteur Le lecteur de séquences
		 * @param traiterResultat La fonction à appliquer pour transformer la valeur résultat en objet de type Donnée
		 */
		public ViaSequenceur(LecteurDeSequence<S> lecteur, Function<S, Donnee<S>> traiterResultat) {
			this.lecteur = lecteur;
			this.traiterResultat = traiterResultat;
		}

		@Override
		public Donnee<S> accumuler(byte octet) {
			return lecteur.lireOctet(octet) ? null : traiterResultat.apply(lecteur.getResultat());
		}
	}
}
