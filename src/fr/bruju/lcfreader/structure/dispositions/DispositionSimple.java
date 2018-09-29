package fr.bruju.lcfreader.structure.dispositions;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.XMLInsecticide;
import fr.bruju.lcfreader.structure.Sequenceur;
import fr.bruju.lcfreader.structure.blocs.Bloc;
import fr.bruju.lcfreader.structure.blocs.Champ;

public class DispositionSimple implements Disposition {

	@Override
	public <T> Bloc<T> decorer(Champ champ, Sequenceur<T> desequenceur) {
		return new BlocSimple<>(champ, desequenceur);
	}
	
	
	
	public static class BlocSimple<T> extends Bloc<T> {
		private final String nom;
		private Sequenceur<T> sequenceur;
		
		public BlocSimple(Champ champ, Sequenceur<T> sequenceur) {
			super(champ);
			nom = "Simple_" + champ.vraiType;
			this.sequenceur = sequenceur;
		}

		@Override
		protected String getNomType() {
			return nom;
		}

		@Override
		protected T extraireDonnee(Desequenceur desequenceur, int tailleLue) {
			XMLInsecticide.balise(nom);
			T objet = sequenceur.lireOctet(desequenceur, tailleLue);
			XMLInsecticide.fermer();
			return objet;
		}
		
		
		
		
	}


}
