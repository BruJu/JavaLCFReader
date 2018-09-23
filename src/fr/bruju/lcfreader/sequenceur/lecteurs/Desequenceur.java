package fr.bruju.lcfreader.sequenceur.lecteurs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import fr.bruju.lcfreader.sequenceur.sequences.LecteurDeSequence;


/**
 * == DESEQUENCEUR ==
 * Un déséquenceur est une interface permettant à des lecteurs de séquence de lire des octets. <br>
 * Par exemple, si il on veut lire dans un fichier, le déséquenceur s'occupe de la partie "extraire les octets du
 * fichier". Lorsque la méthode séquencer est appelée, il va lire des octets depuis le fichier, et transmettre ces
 * octets au sequenceur via sequenceur.lireOctet(octet) jusqu'à que cette méthode renvoie faux. <br>
 * <br>
 * L'objectif de cette interface est donc de séparer la lecture du fichier et l'interprétation des données de ce
 * fichier.
 * 
 * == LECTEUR DE FICHIER OCTETS PAR OCTETS ==
 * Lecteur de fichier qui lit les fichiers octets par octets en donnant les octets lus à un objet qui traite l'octet
 * (nommés séquenceurs). <br>
 * Cette classe ne jette pas d'exception. A la place elle se contente de renvoyer null en cas d'erreur.
 * 
 * @author Bruju
 *
 */
public class Desequenceur {
	
	private final byte[] octetsDuFichier;
	
	private int position = 0;
	
	public final int fin;
	
	/**
	 * Crée un lecteur de fichiers qui utilse un flux
	 * 
	 * @param stream Le flux
	 * @throws IOException 
	 */
	private Desequenceur(String chemin) throws IOException {
		Path fichier = Paths.get(chemin);
		octetsDuFichier = Files.readAllBytes(fichier);
		fin = octetsDuFichier.length;
	}
	
	private Desequenceur(Desequenceur base, int nombreDOctetsPris) {
		if (base.position + nombreDOctetsPris > base.fin) {
			throw new IndexOutOfBoundsException();
		}
		
		this.octetsDuFichier = base.octetsDuFichier;
		this.position = base.position;
		this.fin = this.position + nombreDOctetsPris;
		base.position += nombreDOctetsPris;
	}
	
	public Desequenceur sousSequencer(int taille) {
		return new Desequenceur(this, taille);
	}
	
	
	/**
	 * Lance la lecteur d'octets en utilisant le sequenceur donné. La lecteur se fait jusqu'à que le sequenceur renvoie
	 * faux ou que l'on arrive à la fin du fichier. Dans ce dernier cas, le flux est fermé.
	 * 
	 * @param sequenceur Le sequenceur
	 * @return Le résultat du séquenceur, ou null si une erreur se produit
	 */
	public <T> T sequencer(LecteurDeSequence<T> sequenceur) {
		int byteLu;

		while (nonVide()) {
			byteLu = suivant();
			
			// Octet à soumettre
			boolean reponse = sequenceur.lireOctet((byte) byteLu);

			// Le lecteur ne souhaite plus d'octets supplémentaire
			if (!reponse) {
				break;
			}
		}

		return sequenceur.getResultat();
	}
	
	public byte suivant() {
		return octetsDuFichier[position++];
	}
	
	
	/**
	 * Instancie un lecteur de fichiers octets par octets à partir d'un nom de fichier
	 * 
	 * @param fichier Le nom du fichier
	 * @return Un lecteur de fichier octet par octet pour ce fichier, ou nul si le fichier n'existe pas
	 */
	public static Desequenceur instancier(String fichier) {
		try {
			return new Desequenceur(fichier);
		} catch (IOException e) {
			return null;
		}
	}
	
	// Services proposés
	
	public int $lireUnNombreBER() {
		int valeur = 0;
		int octetLu;
		
		do {
			octetLu = Byte.toUnsignedInt(suivant());
			valeur = (valeur * 0x80) + (octetLu & 0x7F);
		} while ((octetLu & 0x80) != 0);
		
		return valeur;
	}
	
	public String $lireUneChaine(int taille) {
		char[] caracteres = new char[taille];

		for (int i = 0; i != taille; i++) {
			caracteres[i] = (char) suivant();
		}

		return String.valueOf(caracteres);
	}

	public boolean nonVide() {
		return position != fin;
	}
	
	
	
	
}
