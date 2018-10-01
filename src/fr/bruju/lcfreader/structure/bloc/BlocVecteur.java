package fr.bruju.lcfreader.structure.bloc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.structure.MiniBloc;
import fr.bruju.lcfreader.structure.modele.Desequenceur;

/**
 * Un bloc qui lit des données jusqu'à expiration des octets à lire
 * 
 * @author Bruju
 *
 * @param <T> Le type des éléments
 */
public class BlocVecteur<T> extends Bloc<List<T>> {
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */
	
	/** Mini bloc utilisé pour construire les éléments */
	private final MiniBloc<T> miniBloc;

	/**
	 * Crée un bloc vectuer
	 * @param index ID du champ
	 * @param nom Nom du champ
	 * @param type Type du champ indiqué
	 * @param miniBloc Mini bloc à utiliser pour instancier les éléments
	 */
	public BlocVecteur(int index, String nom, String type, MiniBloc<T> sequenceur) {
		super(index, nom, "Vecteur_" + type);
		this.miniBloc = sequenceur;
	}

	
	/* ====================
	 * PROPRIETES D'UN BLOC
	 * ==================== */

	@Override
	protected String getNomType() {
		return typeComplet;
	}

	
	/* =====================
	 * CONSTRUIRE UNE VALEUR
	 * ===================== */
	
	@Override
	public List<T> extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		if (tailleLue < 0) {
			throw new RuntimeException("Taille Lue = " + tailleLue);
		}
		
		List<T> liste = new ArrayList<>();
		
		while (desequenceur.nonVide()) {
			T element = miniBloc.extraireDonnee(desequenceur, -1);
			liste.add(element);
		}
		
		return liste;
	}

	
	/* =========
	 * AFFICHAGE
	 * ========= */
	
	@Override
	public final String convertirEnChaineUneValeur(List<T> valeur) {
		return new StringBuilder()
				  .append("[")
				  .append(valeur.stream()
				            	.map(v -> miniBloc.convertirEnChaineUneValeur(v))
					            .collect(Collectors.joining(", ")))
				  .append("]")
				  .toString();
	}

	@Override
	public final String afficherSousArchi(int niveau, List<T> valeurs) {
		StringBuilder sb = new StringBuilder();
		valeurs.forEach(valeur -> sb.append(miniBloc.afficherSousArchi(niveau, valeur)));
		return sb.toString();
	}
}