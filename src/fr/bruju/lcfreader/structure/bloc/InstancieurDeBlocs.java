package fr.bruju.lcfreader.structure.bloc;

import java.util.HashMap;
import java.util.Map;

import fr.bruju.lcfreader.structure.MiniBloc;
import fr.bruju.lcfreader.structure.structure.Structures;
import fr.bruju.lcfreader.structure.types.MiniInconnu;
import fr.bruju.lcfreader.structure.types.MiniString;
import fr.bruju.lcfreader.structure.types.PrimitifCpp;
import fr.bruju.lcfreader.structure.types.SequenceurIntATailleFixe;

/**
 * Classe permettant d'instancier un bloc selon les données
 * 
 * @author Bruju
 *
 */
public class InstancieurDeBlocs {
	/* =========
	 * SINGLETON
	 * ========= */

	/** Instance de blocs */
	private static InstancieurDeBlocs instance;

	/** On ne peut pas instancier Blocs de l'extérieur */
	private InstancieurDeBlocs() {
		remplir();
	}
	
	/**
	 * Décrypte les paramètres du tableau de données et renvoie le bloc associé
	 * 
	 * @param donnees Les enregistrements du fichier fields.csv
	 * @return Le bloc permettant de traiter les données décrites par cet enregistrement du fichier fields.csv
	 */
	public static Bloc<?> instancier(String[] donnees) {
		if (null == instance) {
			instance = new InstancieurDeBlocs();
		}
		
		return instance.instancierBloc(donnees);
	}
	
	/* ==========
	 * MINI BLOCS
	 * ========== */
	
	/** Liste des mini blocs connus */
	private final Map<String, MiniBloc<?>> miniBlocsConnus = new HashMap<>();
	
	/**
	 * Rempli la liste des mini blocs connus avec des primitives sur des nombres et les sous types connus
	 */
	private void remplir() {
		PrimitifCpp.remplirHashMap(miniBlocsConnus, new PrimitifCpp[] {
					new PrimitifCpp.Int16(),
					new PrimitifCpp.Int32(),
					new PrimitifCpp.Int32LittleEndian(),
					new SequenceurIntATailleFixe.UInt8(),
					new SequenceurIntATailleFixe.UInt16(),
					new SequenceurIntATailleFixe.UInt32(),
					new SequenceurIntATailleFixe.Boolean(),
				});
		
		Structures.getInstance().injecter(miniBlocsConnus);
	}
	

	/* ==============
	 * FONCTIONNEMENT
	 * ============== */

	/**
	 * Décrypte les paramètres du tableau de données et renvoie le bloc associé
	 * 
	 * @param donnees Les enregistrements du fichier fields.csv
	 * @return Le bloc permettant de traiter les données décrites par cet enregistrement du fichier fields.csv
	 */
	private Bloc<?> instancierBloc(String[] donnees) {
		String nom = donnees[2];
		String type = donnees[3];
		boolean champDeTaille = type.equals("SizeField");
		String defaut = donnees[6];

		String indexEnString = donnees[5];
		int index = (indexEnString.equals("")) ? 0 : Integer.decode(indexEnString);
		
		if (champDeTaille) {
			return new BlocDeTaille(index, nom, defaut);
		}

		String disposition = donnees[4];
		
		MiniBloc<?> miniBloc = getMiniBloc(nom, type, index);
		
		return composer(disposition, type, index, nom, miniBloc, defaut);
	}
	
	/**
	 * Donne le mini bloc pour le type demandé
	 * @param nom Le nom du mini bloc
	 * @param type Le nom du type déclaré dans le fichier fields.csv
	 * @param index L'index du bloc (utile pour obtenir le bon mini bloc de chaîne)
	 * @return Le minibloc permettant de lire les données du type donné
	 */
	private MiniBloc<?> getMiniBloc(String nom, String type, int index) {
		if ("String".equals(type)) {
			return MiniString.getInstance(index != 0);
		}
		
		MiniBloc<?> miniBloc = miniBlocsConnus.get(type);
		if (miniBloc != null) {
			return miniBloc;
		}
		
		// System.out.println("Pas de séquenceur pour " + type + " " + nom);
		return MiniInconnu.instance;
	}
	
	/**
	 * Compose le mini bloc (lecture de données) pour le décorer du bloc (lecture de la disposition des données)
	 * permettant de déchiffrer les données de manière conforme 
	 * @param disposition Disposition des données
	 * @param type Le type des données
	 * @param index L'index
	 * @param nom Le nom du champ
	 * @param miniBloc Le mini bloc permettant de lire une donnée concrète
	 * @param defaut La chaîne représentant la valeur par défaut
	 * @return Le bloc permettant de lire une série d'octets pour le champ décrit
	 */
	private Bloc<?> composer(String disposition, String type, int index, String nom, MiniBloc<?> miniBloc,
			String defaut) {
		switch (disposition) {
		case "":
			return new BlocSimple<>(index, nom, type, miniBloc, defaut);
		case "List":
			return new BlocListe<>(index, nom, type, miniBloc);
		case "Vector":
			return new BlocVecteur<>(index, nom, type, miniBloc);
		case "Array":
			return new BlocIndexeur<>(index, nom, type, miniBloc);
		}
		
		if (disposition.startsWith("Tuple_")) {
			int nombreDElements = Integer.parseInt(disposition.substring(6));
			return new BlocTuple<>(index, nom, type, nombreDElements, miniBloc);
		}
		
		throw new RuntimeException("Disposition inconnue : " + disposition + " (" + nom + ")");
	}

}
