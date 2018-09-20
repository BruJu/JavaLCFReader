package fr.bruju.lcfreader.sequenceur.lecteurs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import fr.bruju.lcfreader.sequenceur.sequences.LecteurDeSequence;

/**
 * Lecteur de fichier qui lit les fichiers octets par octets en donnant les octets lus à un objet qui traite l'octet
 * (nommés séquenceurs). <br>
 * Cette classe ne jette pas d'exception. A la place elle se contente de renvoyer null en cas d'erreur.
 * 
 * @author Bruju
 *
 */
public class LecteurDeFichierOctetParOctet implements Desequenceur {
	private static final int TAILLE_BUFFER = 4096;
	
	/** Flux de données */
	private FileInputStream stream;
	
	private byte[] cache = new byte[TAILLE_BUFFER];
	
	private int positionCache = 0;
	private int tailleCache = 0;

	

	private int getProchainOctet() {
		return tailleCache == -1 ? -1 : Byte.toUnsignedInt(cache[positionCache++]);
	}

	private void cacheNonVide() throws IOException {
		if (positionCache == tailleCache) {
			tailleCache = stream.read(cache, 0, TAILLE_BUFFER);
			if (tailleCache == 0)
				throw new IOException("Lecture de 0 octet");
			
			positionCache = 0;
		}
	}
	
	/**
	 * Crée un lecteur de fichiers qui utilse un flux
	 * 
	 * @param stream Le flux
	 */
	private LecteurDeFichierOctetParOctet(FileInputStream stream) {
		this.stream = stream;
	}

	/**
	 * Instancie un lecteur de fichiers octets par octets à partir d'un nom de fichier
	 * 
	 * @param fichier Le nom du fichier
	 * @return Un lecteur de fichier octet par octet pour ce fichier, ou nul si le fichier n'existe pas
	 */
	public static LecteurDeFichierOctetParOctet instancier(String fichier) {
		try {
			File file = new File(fichier);
			FileInputStream stream = new FileInputStream(file);
			
			return new LecteurDeFichierOctetParOctet(stream);
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	/**
	 * Lance la lecteur d'octets en utilisant le sequenceur donné. La lecteur se fait jusqu'à que le sequenceur renvoie
	 * faux ou que l'on arrive à la fin du fichier. Dans ce dernier cas, le flux est fermé.
	 * 
	 * @param sequenceur Le sequenceur
	 * @return Le résultat du séquenceur, ou null si une erreur se produit
	 */
	@Override
	public <T> T sequencer(LecteurDeSequence<T> sequenceur) {
		int byteLu;

		while (stream != null) {
			try {
				cacheNonVide();
				byteLu = getProchainOctet();
				
				if (byteLu == -1) {
					// Fin de fichier
					stream.close();
					stream = null;
				} else {
					// Octet à soumettre
					boolean reponse = sequenceur.lireOctet((byte) byteLu);

					// Le lecteur ne souhaite plus d'octets supplémentaire
					if (!reponse) {
						break;
					}
				}

			} catch (IOException e) {
				// Erreur
				stream = null;
				return null;
			}
		}

		return sequenceur.getResultat();
	}


	/**
	 * Ferme le flux si il n'est pas déjà fermé.
	 */
	public void fermer() {
		if (stream == null)
			return;

		try {
			stream.close();
		} catch (IOException e) {
		}
		stream = null;
	}
}
