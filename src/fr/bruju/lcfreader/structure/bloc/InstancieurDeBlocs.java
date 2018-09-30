package fr.bruju.lcfreader.structure.bloc;

import java.util.HashMap;
import java.util.Map;

import fr.bruju.lcfreader.structure.blocs.mini.MiniBloc;
import fr.bruju.lcfreader.structure.blocs.mini.MiniInconnu;
import fr.bruju.lcfreader.structure.blocs.mini.MiniString;
import fr.bruju.lcfreader.structure.structure.Structures;
import fr.bruju.lcfreader.structure.types.PrimitifCpp;
import fr.bruju.lcfreader.structure.types.SequenceurIntATailleFixe;

/**
 * Classe permettant d'instancier un bloc selon les données
 * 
 * @author Bruju
 *
 */
public class InstancieurDeBlocs {
	private final Map<String, MiniBloc<?>> miniBlocsConnus = new HashMap<>();
	
	private void remplir() {
		PrimitifCpp.remplirHashMap(miniBlocsConnus, new PrimitifCpp[] {
					new PrimitifCpp.Int16(),
					new PrimitifCpp.Int32(),
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

	private MiniBloc<?> getSequenceur(String nom, String type, int index) {
		if ("String".equals(type)) {
			return MiniString.getInstance(index != 0);
		}
		
		MiniBloc<?> miniBloc = miniBlocsConnus.get(type);
		if (miniBloc != null) {
			return miniBloc;
		}
		
		System.out.println("Pas de séquenceur pour " + type + " " + nom);
		return MiniInconnu.instance;
	}

	/**
	 * Décrypte les paramètres du tableau de données et renvoie le bloc associé
	 * 
	 * @param donnees Les enregistrements du fichier fields.csv
	 * @return Le bloc permettant de traiter les données décrites par cet enregistrement du fichier fields.csv
	 */
	private Bloc<?> instancierBloc(String[] donnees) {
		String nom = donnees[2];
		boolean sized = donnees[3].equals("SizeField");
		String type = donnees[3];
		String disposition = donnees[4];
		String defaut = donnees[6];

		int index = 0;

		if (!donnees[5].equals(""))
			index = Integer.decode(donnees[5]);
		
		
		if (sized) {
			return new BlocDeTaille(index, nom, defaut);
		}
		
		MiniBloc<?> sequenceur = getSequenceur(nom, type, index);
		

		return instancier(disposition, type, index, nom, sequenceur, defaut);
	}


	public Bloc<?> instancier(String disposition, String type, int index, String nom, MiniBloc<?> miniBloc, String defaut) {
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
		
		throw new RuntimeException("Disposition inconnue : " + disposition + " (" + nom + ")");
	}

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
	 * Donne l'instance de Blocs
	 * 
	 * @return L'instance de Blocs
	 */
	public static InstancieurDeBlocs getInstance() {
		if (null == instance) {
			instance = new InstancieurDeBlocs();
		}
		return instance;
	}

	/**
	 * Décrypte les paramètres du tableau de données et renvoie le bloc associé
	 * 
	 * @param donnees Les enregistrements du fichier fields.csv
	 * @return Le bloc permettant de traiter les données décrites par cet enregistrement du fichier fields.csv
	 */
	public static Bloc<?> instancier(String[] donnees) {
		return getInstance().instancierBloc(donnees);
	}
}
