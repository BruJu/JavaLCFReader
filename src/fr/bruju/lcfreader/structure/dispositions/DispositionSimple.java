package fr.bruju.lcfreader.structure.dispositions;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.XMLInsecticide;
import fr.bruju.lcfreader.structure.Sequenceur;
import fr.bruju.lcfreader.structure.blocs.Bloc;
import fr.bruju.lcfreader.structure.blocs.Champ;
import fr.bruju.lcfreader.structure.blocs.MiniBloc;

public class DispositionSimple implements Disposition {

	@Override
	public <T> Bloc<T> decorer(Champ champ, MiniBloc<T> desequenceur) {
		return new BlocSimple<>(champ, desequenceur);
	}
	
	
	
	public static class BlocSimple<T> extends Bloc<T> {
		private final String nom;
		private MiniBloc<T>  sequenceur;
		
		public BlocSimple(Champ champ, MiniBloc<T> sequenceur) {
			super(champ);
			nom = "Simple_" + champ.vraiType;
			this.sequenceur = sequenceur;
		}

		@Override
		protected String getNomType() {
			return nom;
		}

		@Override
		public T extraireDonnee(Desequenceur desequenceur, int tailleLue) {
			XMLInsecticide.balise(nom);
			T objet = sequenceur.extraireDonnee(desequenceur, tailleLue);
			XMLInsecticide.fermer();
			return objet;
		}

		@Override
		public String convertirEnChaineUneValeur(T valeur) {
			return sequenceur.convertirEnChaineUneValeur(valeur);
		}

		@Override
		public void afficherSousArchi(int niveau, T value) {
			sequenceur.afficherSousArchi(niveau, value);
		}
	}


}
