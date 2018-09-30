package fr.bruju.lcfreader.structure.dispositions;

import java.util.LinkedHashMap;
import java.util.Map;

import fr.bruju.lcfreader.modele.Desequenceur;
import fr.bruju.lcfreader.modele.XMLInsecticide;
import fr.bruju.lcfreader.structure.Sequenceur;
import fr.bruju.lcfreader.structure.blocs.Bloc;
import fr.bruju.lcfreader.structure.blocs.Champ;
import fr.bruju.lcfreader.structure.blocs.MiniBloc;

public class DispositionTableau implements Disposition {

	@Override
	public <T> Bloc<Map<Integer, T>> decorer(Champ champ, MiniBloc<T> sequenceur) {
		return new BlocIndexeur<>(champ, sequenceur);
	}

	
	
	public static class BlocIndexeur<T> extends Bloc<Map<Integer, T>> {
		private final String nomBloc; 
		private final MiniBloc<T> sequenceur;

		public BlocIndexeur(Champ champ, MiniBloc<T> sequenceur) {
			super(champ);
			this.sequenceur = sequenceur;
			nomBloc = "TableauIndexe_" + champ.vraiType;
		}

		@Override
		protected String getNomType() {
			return nomBloc;
		}

		@Override
		public Map<Integer, T> extraireDonnee(Desequenceur desequenceur, int tailleLue) {
			XMLInsecticide.balise(nomBloc);
			
			XMLInsecticide.balise("NombreElements");
			int nombreElements = desequenceur.$lireUnNombreBER();
			XMLInsecticide.fermer();
			
			Map<Integer, T> carte = new LinkedHashMap<>();
			int id;
			T donnee;
			
			while (nombreElements != 0) {
				{
					XMLInsecticide.balise("Element");
					{
						XMLInsecticide.balise("id");
						id = desequenceur.$lireUnNombreBER();
						XMLInsecticide.fermer();
					} {
						donnee = sequenceur.extraireDonnee(desequenceur, -1);
					}
					XMLInsecticide.fermer();
				}
				
				carte.put(id, donnee);
				nombreElements--;
			}
			
			XMLInsecticide.fermer();
			
			return carte;
		}
	}
}
