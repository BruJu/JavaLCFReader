package random;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Codes {
	Map<String, Structure> structures;
	
	
	
	public void remplirStructures(String fichier) {
		File file = new File(fichier);
		
		structures = new HashMap<>();
		
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader buffer = new BufferedReader(fileReader);
			String line;
	
			while (true) {
				line = buffer.readLine();
	
				if (line == null) {
					break;
				}
				
				if (line.startsWith("#") || line.equals(""))
					continue;
				
				String[] donnees = line.split(",", -1);
				
				structures.putIfAbsent(donnees[0], new Structure());
				
				structures.get(donnees[0]).ajouterChamp(donnees);
			}
	
			buffer.close();
		} catch (IOException e) {
			
		}
	}
	
	
	public static class Structure {
		public List<Champ<?>> champs = new ArrayList<>();
		
		public List<Data<?>> getData() {
			return champs.stream().map(champ -> new Data<>(champ)).collect(Collectors.toList());
		}

		public void ajouterChamp(String[] donnees) {
			champs.add(Champ.instancier(donnees));
		}
	}
	
	
	
	
	public static class Champ<T> {
		public final int index;
		public final String nom;
		public final boolean sized;
		public final T valeurParDefaut;
		public final Function<byte[], T> methodeDeLecture;
		
		public Champ(int index, String nom, boolean sized, T valeurParDefaut, Function<byte[], T> methodeDeLecture) {
			this.index = index;
			this.nom = nom;
			this.sized = sized;
			this.valeurParDefaut = valeurParDefaut;
			this.methodeDeLecture = methodeDeLecture;
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public static <T> Champ<?> instancier(String[] donnees) {
			String nom = donnees[1];
			boolean sized = donnees[2].equals("t");
			String type = donnees[3];
			if (donnees[4].equals(""))
				return null;
			
			int index = Integer.decode(donnees[4]);
			
			//byte index = Byte.parseByte(donnees[4]);
			
			Bloc<?> bloc = Bloc.genererBloc(type, donnees[5]);
			
			if (bloc != null)
				return new Champ(index, nom, sized, bloc.defaut(), bloc.fonction());
			else
				return null;
		}
	}
	
	public static class Data<T> {
		public Champ<T> champ;
		public T value;
		
		public Data(Champ<T> champ) {
			this.champ = champ;
			value = null;
		}
	}
	
	
	public static interface Bloc<T> {

		static Bloc<?> genererBloc(String type, String defaut) {
			switch (type) {
			case "Int32":
				return new BlocInt32(defaut);
			
			
			default:
				return null;
			}
		}

		public Function<byte[], T> fonction();

		public T defaut();
		
		
		public static class BlocInt32 implements Bloc<Integer> {
			private Integer defaut;
			
			public BlocInt32(String defaut) {
				if (defaut.equals("")) {
					this.defaut = null;
				} else {
					if (defaut.contains("|")) {
						defaut = defaut.split("|")[1];
					}
					
					
					this.defaut = Integer.parseInt(defaut);
				}
			}

			@Override
			public Function<byte[], Integer> fonction() {
				return this::convertir;
			}
			
			
			public Integer convertir(byte[] bytes) {
				int valeur = 0;
				
				for (byte octet : bytes) {
					valeur = valeur * 0x80 + (octet & 0x7F);
				}
				
				return valeur;
			}

			@Override
			public Integer defaut() {
				return defaut;
			}
		}
		
	}
	
}
