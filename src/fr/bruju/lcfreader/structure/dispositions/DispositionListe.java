package fr.bruju.lcfreader.structure.dispositions;

import java.util.ArrayList;
import java.util.List;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.XMLInsecticide;
import fr.bruju.lcfreader.structure.Sequenceur;
import fr.bruju.lcfreader.structure.blocs.Bloc;
import fr.bruju.lcfreader.structure.blocs.Champ;

public class DispositionListe implements Disposition {
	@Override
	public <T> Bloc<List<T>> decorer(Champ champ, Sequenceur<T> sequenceur) {
		return new BlocListe<>(champ, sequenceur);
	}
	
	
	public static class BlocListe<T> extends Bloc<List<T>> {
		private Sequenceur<T> sequenceur;
		private String nomChamp;

		public BlocListe(Champ champ, Sequenceur<T> sequenceur) {
			super(champ);
			this.sequenceur = sequenceur;
			this.nomChamp = "Liste_" + champ.vraiType;
		}

		@Override
		protected String getNomType() {
			return nomChamp;
		}

		@Override
		protected List<T> extraireDonnee(Desequenceur desequenceur, int tailleLue) {
			if (tailleLue < 0) {
				throw new RuntimeException("Taille Lue = " + tailleLue);
			}
			
			XMLInsecticide.balise(nomChamp);
			
			XMLInsecticide.balise("nbElem");
			int nombreDElements = desequenceur.$lireUnNombreBER();
			XMLInsecticide.fermer();
			
			List<T> liste = new ArrayList<>(nombreDElements);
			
			while (nombreDElements != 0) {
				XMLInsecticide.balise("data");
				sequenceur.lireOctet(desequenceur, -1);
				XMLInsecticide.fermer();
				nombreDElements --;
			}
			
			XMLInsecticide.fermer();
			
			return liste;
		}
	}
}
