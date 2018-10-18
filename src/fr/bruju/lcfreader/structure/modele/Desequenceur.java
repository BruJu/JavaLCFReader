package fr.bruju.lcfreader.structure.modele;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


/**
 * Classe permettant de lire les octets d'un fichier.
 * <br>Un service est proposé pour extraire une sous section et détecter les dépassements. 
 * 
 * @author Bruju
 *
 */
public class Desequenceur {
	/* =========================
	 * ATTRIBUTS ET CONSTRUCTEUR
	 * ========================= */
	
	/** Encodage utilisé pour coder les chaînes */
	private static final Charset CHARSET = Charset.forName("ISO-8859-15");
	
	/** Nom du fichier lu */
	private final String fichier;
	/** Octets du fichier */
	private final byte[] octetsDuFichier;
	
	/** Position actuelle */
	private int position = 0;
	/** Début */
	private final int debut;
	/** Fin */
	private final int fin;
	
	// Constructeurs privés
	
	/**
	 * Crée un lecteur de fichiers qui utilse un flux
	 * 
	 * @param chemin Le chemin vers le fichier à lire
	 * @throws IOException Erreur à la lecture du fichier
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

	// Pseudo constructeurs public
	
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
	
	/**
	 * Extrait une sous séquence d'octets partant de la position actuelle et contenant le nombre d'octets demandés
	 * @param taille Le nombre d'octets à gérer avec cette sous séquence
	 */
	public Desequenceur sousSequencer(int taille) {
		return new Desequenceur(this, taille);
	}

	
	/* =========================
	 * LECTURE OCTETS PAR OCTETS
	 * ========================= */
	
	
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

	/**
	 * Extrait les nombreDOctets prochains octets et les restitue dans un tableau
	 * @param nombreDOctets Le nombre d'octets à extraire
	 * @return Un tableau représentant les nombreDOctets prochains octets qu'aurait renvoyé suivant()
	 */
	public byte[] extrairePortion(int nombreDOctets) {
		byte[] extrait = Arrays.copyOfRange(octetsDuFichier, position, position + nombreDOctets);
		position += nombreDOctets;
		return extrait;
	}

	/**
	 * Construit un ByteBuffer à partir des nombreDOctets prochains octets
	 * @param nombreDOctets Le nombre d'octets à mettre dans le ByteBuffer
	 * @return Un ByteBuffer contenant les nombreDOctets octets qu'aurait renvoyé suivant()
	 */
	public ByteBuffer wrapper(int nombreDOctets) {
		position += nombreDOctets;
		return ByteBuffer.wrap(octetsDuFichier, position - nombreDOctets, nombreDOctets);
	}


	/* =======================
	 * SERVICES DE HAUT NIVEAU
	 * ======================= */
	
	/**
	 * Lit des octets pour constituer un nombre encodé selon le principe utilisé par BER.
	 * <br>Plus précisémement, pour chaque octet, les 7 bits de poids faibles correspondent au nombre, et le bit de
	 * poids le plus fort représente si le prochain octet fait parti du nombre. Le nombre reconstitué est la
	 * concaténation des 7 bits de poids faibles de chaque octet composant le nombre, chaque octet ayant un 1 comme bit
	 * de poids fort sauf le dernier qui a un 0.
	 *  
	 * @return Le nombre lu
	 */
	public int $lireUnNombreBER() {
		int valeur = 0;
		int octetLu;
		
		do {
			octetLu = Byte.toUnsignedInt(suivant());
			valeur = (valeur * 0x80) + (octetLu & 0x7F);
		} while ((octetLu & 0x80) != 0);
		
		return valeur;
	}
	
	/**
	 * Lit les taille prochain octets et les restitue comme étant une chaîne
	 * @param taille La taille de la chaîne
	 * @return La chaîne lue
	 */
	public String $lireUneChaine(int taille) {
		String chaine = new String(octetsDuFichier, position, taille, CHARSET);
		position += taille;
		return chaine;
	}

	/* ==============================
	 * POSITION DE LA TETE DE LECTURE
	 * ============================== */
	
	/**
	 * Renvoie vrai si il reste des octets à lire
	 * @return Vrai si il reste des octets à lire
	 */
	public boolean nonVide() {
		return position != fin;
	}

	/**
	 * Renvoie le nombre d'octets restants à lire
	 * @return Le nombre d'octets restants à lire
	 */
	public int octetsRestants() {
		return fin - position;
	}
	
	/**
	 * Donne le numéro de l'octet actuel
	 * @return Le numéro de l'octet actuel
	 */
	public int getPosition() {
		return position;
	}

}
