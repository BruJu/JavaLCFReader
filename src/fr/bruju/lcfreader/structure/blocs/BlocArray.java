package fr.bruju.lcfreader.structure.blocs;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.structure.Structure;

/**
 * Un bloc de données correspondant à un tableau d'ensemble de données
 * 
 * @author Bruju
 *
 */
public class BlocArray extends Bloc<Map<Integer, EnsembleDeDonnees>> {
	
	
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */

	private Structure structure;
	
	/**
	 * Bloc constitué d'un tableau d'ensemble de données
	 * 
	 * @param champ Les caractéristiques
	 * @param structure La structure
	 */
	public BlocArray(Champ champ, Structure structure) {
		super(champ);
		this.structure = structure;
	}

	/* ====================
	 * PROPRIETES D'UN BLOC
	 * ==================== */

	@Override
	public String getNomType() {
		return "Tableau[" + structure.nom + "]";
	}

	/* ============================
	 * INTERACTION AVEC LES VALEURS
	 * ============================ */

	@Override
	public String convertirEnChaineUneValeur(Map<Integer, EnsembleDeDonnees> valeur) {
		String contenu = valeur
				.values()
				.stream()
				.map(d -> d.getRepresentationEnLigne())
				.collect(Collectors.joining(";"));

		return new StringBuilder()
				.append("[")
				.append(contenu)
				.append("]")
				.toString();
	}

	@Override
	public void afficherSousArchi(int niveau, Map<Integer, EnsembleDeDonnees> value) {
		value.values().forEach(data -> data.afficherArchitecture(niveau));
	}

	/* =====================
	 * CONSTRUIRE UNE VALEUR
	 * ===================== */
	
	@Override
	public Map<Integer, EnsembleDeDonnees> extraireDonnee(Desequenceur desequenceur, int tailleLue) {
		Desequenceur.balise("TABLEAU_" + structure.nom);

		
		Desequenceur.balise("NombreElements");
		int nombreElements = desequenceur.$lireUnNombreBER();
		Desequenceur.fermer();
		
		Map<Integer, EnsembleDeDonnees> carte = new LinkedHashMap<>();
		
		while (nombreElements != 0) {
			Desequenceur.balise("Element");
			
			Desequenceur.balise("id");
			int id = desequenceur.$lireUnNombreBER();
			Desequenceur.fermer();
			Desequenceur.balise("data");
			EnsembleDeDonnees ensemble = structure.lireOctet(desequenceur, -1);
			Desequenceur.fermer();
			Desequenceur.fermer();
			carte.put(id, ensemble);
			nombreElements--;
		}
		
		Desequenceur.fermer();
		
		return carte;
	}
}
