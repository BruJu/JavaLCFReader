package fr.bruju.lcfreader.structure.blocs;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.EnsembleDeDonnees;
import fr.bruju.lcfreader.structure.Structures;
import fr.bruju.lcfreader.structure.blocs.mini.MiniBER;
import fr.bruju.lcfreader.structure.blocs.mini.MiniBloc;
import fr.bruju.lcfreader.structure.blocs.mini.MiniInconnu;
import fr.bruju.lcfreader.structure.dispositions.Disposition;
import fr.bruju.lcfreader.structure.types.PrimitifCpp;

/**
 * Classe permettant d'instancier un bloc selon les données
 * 
 * @author Bruju
 *
 */
public class Blocs {

	/* ==============
	 * FONCTIONNEMENT
	 * ============== */

	private MiniBloc<?> getSequenceur(String nom, String type, int index) {
		
		switch (type) {
		case "Int32":
			return MiniBER.instance;
		case "String":
			if (index == 0)
				return (o, s) -> o.$lireUneChaine(o.$lireUnNombreBER());
			
			return (o, s) -> o.$lireUneChaine(s);
		}
		
		if (PrimitifCpp.map.containsKey(type)) {
			return (o, s) -> PrimitifCpp.map.get(type).lireOctet(o, s);
		}
		
		if (Structures.getInstance().get(type) != null) {
			return new MiniBloc<EnsembleDeDonnees>() {

				@Override
				public EnsembleDeDonnees extraireDonnee(Desequenceur o, int s) {
					return Structures.getInstance().get(type).lireOctet(o, s);
				}

				@Override
				public String convertirEnChaineUneValeur(EnsembleDeDonnees valeur) {
					return valeur.getRepresentationEnLigne();
				}
			};
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

		int index = 0;

		if (!donnees[5].equals(""))
			index = Integer.decode(donnees[5]);
		
		Champ champ = new Champ(index, nom, sized, donnees[3] + "_" + donnees[4]);
		
		if (sized) {
			return new BlocInt32(champ, donnees[6]);
		}
		
		Disposition dispo = Disposition.get(disposition, type);
		MiniBloc<?> sequenceur = getSequenceur(nom, type, index);
		
		
		
		return dispo.decorer(champ, sequenceur);
	}


	/* =========
	 * SINGLETON
	 * ========= */

	/** Instance de blocs */
	private static Blocs instance;

	/** On ne peut pas instancier Blocs de l'extérieur */
	private Blocs() {
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
