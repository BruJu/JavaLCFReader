package fr.bruju.lcfreader.structure.modele;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import fr.bruju.lcfreader.Utilitaire;


/**
 * Classe permettant de lire les octets d'un fichier.
 * <br>Un service est proposé pour extraire une sous section et détecter les dépassements. 
 * 
 * @author Bruju
 *
 */
public class Desequenceur {

	
	
	/** Nom du fichier lu */
	private final String fichier;
	/** Octets du fichier */
	private final byte[] octetsDuFichier;
	
	/** Position actuelle */
	private int position = 0;
	/** Début */
	public final int debut;
	/** Fin */
	public final int fin;
	
	
	public ByteBuffer wrapper(int nombreDOctets) {
		position += nombreDOctets;
		return ByteBuffer.wrap(octetsDuFichier, position - nombreDOctets, nombreDOctets);
	}
	
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
		debut = 0;
		this.fichier = chemin;
	}
	
	/**
	 * Extrait une sous séquence d'octets partant de la position actuelle et contenant le nombre d'octets demandés
	 * @param base La séquence de base
	 * @param nombreDOctetsPris Le nombre d'octets à gérer avec cette sous séquence
	 */
	private Desequenceur(Desequenceur base, int nombreDOctetsPris) {
		if (base.position + nombreDOctetsPris > base.fin) {
			throw new IndexOutOfBoundsException();
		}
		
		// Invariants
		this.fichier = base.fichier;
		this.octetsDuFichier = base.octetsDuFichier;
		// Curseur
		this.debut = base.position;
		this.position = base.position;
		this.fin = this.position + nombreDOctetsPris;
		// Modifier le curseur du père
		base.position += nombreDOctetsPris;	
	}
	

	/**
	 * Extrait une sous séquence d'octets partant de la position actuelle et contenant le nombre d'octets demandés
	 * @param nombreDOctetsPris Le nombre d'octets à gérer avec cette sous séquence
	 */
	public Desequenceur sousSequencer(int taille) {
		return new Desequenceur(this, taille);
	}
	
	/**
	 * Donne l'octet suivant
	 * @return L'octet suivant
	 */
	public byte suivant() {
		if (position >= fin) {
			throw new LectureIllegale(fichier, debut, position);
		}
		
		return octetsDuFichier[position++];
	}
	
	private static class LectureIllegale extends RuntimeException {
		/** Serial id */
		private static final long serialVersionUID = 760742337368045553L;

		/**
		 * Construit une exception de lecture illégale
		 * @param fichier Nom du fichier
		 * @param debut Position du premier octet
		 * @param position Position du curseur
		 */
		public LectureIllegale(String fichier, int debut, int position) {
			super("Lecture illégale dans "
					+ fichier
					+ ", segment commençant à "
					+ Utilitaire.toHex(debut)
					+ " avec un curseur à "
					+ Utilitaire.toHex(position));
			
				XMLInsecticide.xml("Crash");
				
				XMLInsecticide.ecrireDebug();
		}
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
			XMLInsecticide.ajouterXML((byte) octetLu);
			valeur = (valeur * 0x80) + (octetLu & 0x7F);
		} while ((octetLu & 0x80) != 0);
		
		XMLInsecticide.crocheter(valeur);
		
		return valeur;
	}
	
	public String $lireUneChaine(int taille) {
		char[] caracteres = new char[taille];
		
		for (int i = 0; i != taille; i++) {
			caracteres[i] = (char) suivant();
			XMLInsecticide.ajouterXML((byte) caracteres[i]);
		}
		
		XMLInsecticide.crocheter(String.valueOf(caracteres));

		return String.valueOf(caracteres);
	}

	public boolean nonVide() {
		return position != fin;
	}

	public int octetsRestants() {
		return fin - position;
	}
	
	public int getPosition() {
		return position;
	}

	public byte[] extrairePortion(int tailleLue) {
		byte[] extrait = Arrays.copyOfRange(octetsDuFichier, position, position + tailleLue);
		position += tailleLue;
		return extrait;
	}
	
}
