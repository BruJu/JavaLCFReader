package fr.bruju.lcfreader.structure.blocs;

import java.util.HashMap;
import java.util.Map;

import fr.bruju.lcfreader.structure.Structures;
import fr.bruju.lcfreader.structure.bloc.BlocIndexeur;
import fr.bruju.lcfreader.structure.bloc.BlocListe;
import fr.bruju.lcfreader.structure.bloc.BlocSimple;
import fr.bruju.lcfreader.structure.bloc.BlocVecteur;
import fr.bruju.lcfreader.structure.blocs.mini.MiniBloc;
import fr.bruju.lcfreader.structure.blocs.mini.MiniInconnu;
import fr.bruju.lcfreader.structure.types.PrimitifCpp;
import fr.bruju.lcfreader.structure.types.SequenceurIntATailleFixe;

/**
 * Classe permettant d'instancier un bloc selon les données
 * 
 * @author Bruju
 *
 */
public class Blocs {
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
		
		
		
		switch (type) {
		case "String":
			if (index == 0)
				return (o, s) -> o.$lireUneChaine(o.$lireUnNombreBER());
			
			return (o, s) -> o.$lireUneChaine(s);
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
		
		Champ champ = new Champ(index, nom, sized, donnees[3] + "_" + donnees[4]);
		
		if (sized) {
			return new BlocInt32(champ, defaut);
		}
		
		MiniBloc<?> sequenceur = getSequenceur(nom, type, index);
		

		return instancier(disposition, champ, sequenceur, defaut);
	}


	public Bloc<?> instancier(String disposition, Champ champ, MiniBloc<?> miniBloc, String defaut) {
		switch (disposition) {
		case "":
			return new BlocSimple<>(champ, miniBloc, defaut);
		case "List":
			return new BlocListe<>(champ, miniBloc);
		case "Vector":
			return new BlocVecteur<>(champ, miniBloc);
		case "Array":
			return new BlocIndexeur<>(champ, miniBloc);
		}
		
		throw new RuntimeException("Disposition inconnue : " + disposition);
	}

	/* =========
	 * SINGLETON
	 * ========= */

	/** Instance de blocs */
	private static Blocs instance;

	/** On ne peut pas instancier Blocs de l'extérieur */
	private Blocs() {
		remplir();
	}

	/**
	 * Donne l'instance de Blocs
	 * 
	 * @return L'instance de Blocs
	 */
	public static Blocs getInstance() {
		if (null == instance) {
			instance = new Blocs();
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
