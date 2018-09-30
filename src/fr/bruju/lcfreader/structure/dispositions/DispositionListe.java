package fr.bruju.lcfreader.structure.dispositions;

import java.util.ArrayList;
import java.util.List;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.XMLInsecticide;
import fr.bruju.lcfreader.structure.Sequenceur;
import fr.bruju.lcfreader.structure.blocs.Bloc;
import fr.bruju.lcfreader.structure.blocs.Champ;
import fr.bruju.lcfreader.structure.blocs.MiniBloc;

public class DispositionListe implements Disposition {
	@Override
	public <T> Bloc<List<T>> decorer(Champ champ, MiniBloc<T> miniBloc) {
		return new BlocListe<>(champ, miniBloc);
	}
	
	
	public static class BlocListe<T> extends Bloc<List<T>> {
		private MiniBloc<T> miniBloc;
		private String nomChamp;

		public BlocListe(Champ champ, MiniBloc<T> miniBloc) {
			super(champ);
			this.miniBloc = miniBloc;
			this.nomChamp = "Liste_" + champ.vraiType + "_" + this.nom;
		}

		@Override
		protected String getNomType() {
			return nomChamp;
		}

		@Override
		public List<T> extraireDonnee(Desequenceur desequenceur, int tailleLue) {
			XMLInsecticide.balise(nomChamp);
			
			XMLInsecticide.balise("nbElem");
			int nombreDElements = desequenceur.$lireUnNombreBER();
			XMLInsecticide.fermer();
			
			List<T> liste = new ArrayList<>(nombreDElements);
			
			while (nombreDElements != 0) {
				miniBloc.extraireDonnee(desequenceur, -1);
				nombreDElements --;
			}
			
			XMLInsecticide.fermer();
			
			return liste;
		}
	}
}
