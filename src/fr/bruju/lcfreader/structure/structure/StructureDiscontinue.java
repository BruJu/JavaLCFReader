package fr.bruju.lcfreader.structure.structure;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import fr.bruju.lcfreader.Utilitaire;
import fr.bruju.lcfreader.structure.bloc.Bloc;
import fr.bruju.lcfreader.structure.bloc.InstancieurDeBlocs;
import fr.bruju.lcfreader.structure.modele.Desequenceur;
import fr.bruju.lcfreader.structure.modele.EnsembleDeDonnees;

/**
 * Structure de donnée discontinue. Chaque donnée est encodé sous la forme tag - taille - valeur. Le tag dépend de la
 * structure et cette classe se charge donc de faire la correspondance entre tag et format.
 * <br>La lecture s'arrête quand tous les octets sont lu, où que le tag 0 est trouvé
 * 
 * @author Bruju
 *
 */
public class StructureDiscontinue extends Structure {
	/** Liste des champs connus */
	private Map<Integer, Bloc<?>> champs = new LinkedHashMap<>();
	
	/**
	 * Crée une structure de lecture de champs discontinus
	 * @param nom Le nom de la structure
	 */
	public StructureDiscontinue(String nom) {
		super(nom);
	}

	@Override
	public EnsembleDeDonnees extraireDonnee(Desequenceur desequenceur, int parametre) {
		EnsembleDeDonnees ensembleConstruit = new EnsembleDeDonnees(this);
		
		int numeroDeBloc;
		int taille;
		
		while (desequenceur.nonVide() && (numeroDeBloc = desequenceur.$lireUnNombreBER()) != 0) {
			Bloc<?> bloc = champs.get(numeroDeBloc);
			
			if (bloc == null) {
				throw new RuntimeException("Pas de bloc numéro " + Utilitaire.toHex(numeroDeBloc) +
						" dans " + nom + " à la position " + Utilitaire.toHex(desequenceur.getPosition()));
			}
			
			taille = desequenceur.$lireUnNombreBER();
			
			if (taille != 0) {
				Desequenceur sousDesequenceur = desequenceur.sousSequencer(taille);
				ensembleConstruit.ajouter(bloc.lireOctet(sousDesequenceur, taille));
			}
		}
		
		return ensembleConstruit;
	}
	
	@Override
	public void ajouterChamp(String[] donnees) {
		Bloc<?> bloc = InstancieurDeBlocs.instancier(donnees);
		champs.put(bloc.index, bloc);
	}

	@Override
	protected Collection<Bloc<?>> obtenirTousLesBlocs() {
		return champs.values();
	}
}
