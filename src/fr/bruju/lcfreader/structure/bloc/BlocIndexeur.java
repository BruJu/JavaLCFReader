package fr.bruju.lcfreader.structure.bloc;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.structure.MiniBloc;
import fr.bruju.lcfreader.structure.modele.Desequenceur;

/**
 * Un bloc qui construit une map associant id et élément
 * 
 * @author Bruju
 *
 * @param <T> Type des éléments
 */
public class BlocIndexeur<T> extends Bloc<Map<Integer, T>> {
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */
	/** Mini bloc utilisé pour construire les éléments */
	private final MiniBloc<T> miniBloc;

	/**
	 * Crée un bloc indexeur
	 * @param index ID du champ
	 * @param nom Nom du champ
	 * @param type Type du champ indiqué
	 * @param miniBloc Mini bloc à utiliser pour instancier les éléments
	 */
	public BlocIndexeur(int index, String nom, String type, MiniBloc<T> miniBloc) {
		super(index, nom, "TableauIndexe_" + type);
		this.miniBloc = miniBloc;
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
	public Map<Integer, T> extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		int nombreElements = desequenceur.$lireUnNombreBER();
		
		Map<Integer, T> carte = new LinkedHashMap<>();
		int id;
		T donnee;
		
		while (nombreElements != 0) {
			id = desequenceur.$lireUnNombreBER();
			donnee = miniBloc.extraireDonnee(desequenceur, -1);
			
			carte.put(id, donnee);
			nombreElements--;
		}
		
		return carte;
	}
	
	
	/* =========
	 * AFFICHAGE
	 * ========= */
	
	@Override
	public String convertirEnChaineUneValeur(Map<Integer, T> valeur) {
		return new StringBuilder()
		  .append("[")
		  .append(valeur.entrySet().stream()
			            .map(this::convertirEntree)
			            .collect(Collectors.joining(", ")))
		  .append("]")
		  .toString();
	}

	/**
	 * Donne la chaîne pour afficher l'entrée donnée
	 * @param entree L'entrée à afficher
	 * @return Une chaîne de la forme "[id] données"
	 */
	private String convertirEntree(Map.Entry<Integer, T> entree) {
		return "[" + entree.getKey() + "] " + miniBloc.convertirEnChaineUneValeur(entree.getValue());
	}
	
	@Override
	public String afficherSousArchi(int niveau, Map<Integer, T> valeurs) {
		StringBuilder sb = new StringBuilder();
		valeurs.values().forEach(valeur -> sb.append(miniBloc.afficherSousArchi(niveau, valeur)));
		return sb.toString();
	}
}