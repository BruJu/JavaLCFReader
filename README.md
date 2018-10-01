# JavaLCFReader

Il s'agit d'une bibliothèque permettant de lire des fichiers générés par RPG Maker 2003, en particuleir d'en lire les
instructions.


## Présentation

RPG Maker 2003 est un logiciel de création de jeux vidéos développé par Enterbrain, ASCII et Kadokawa Corporation et
sorti en décembre 2002. Son objectif est de permettre à des personnes ne sachant pas developper de créer leurs propres
jeux vidéos.

Ce logiciel ayant été developpé avant l'avènement des formats XML et JSON, les fichiers générés sont encodés dans un
format binaire, peu lisible pour l'être humain ou d'autres logiciels non adaptés.

JavaLCFReader se donne pour objectif de proposer un moyen simple de déchiffrer ces fichiers et d'en exploiter certaines
ressources pour d'autres programmes developpés en Java.

Les fichiers lus par ce logiciel sont des fichiers au format :
* lmu (LCF Map Unit) contenant les données d'une carte de RPG Maker 2003 (différents évènements et instructions)
* lmt (LCF Map Tree) contenant l'arborescence des cartes ainsi que la liste des cartes gérées
* ldb (LCF Database) contenant la base de données (objets, héros, évènements communs, nom des variables …)
* lsd (LCF Save Data) contenant les sauvegardes des parties des joueurs. A noter qu'actuellement aucune classe
d'accès simple aux données n'est disponible pour les sauvegardes.

Cette bibliothèque ne se charge que de la lecture des fichiers. Ainsi, les instructions sont décryptés avec le code de
l'instruction, un argument sous forme de chaîne et une liste de nombres. Aucune sens particulier n'est donné à ces
instructions.



## Dépendances

Aucune. Le buildpath doit contenir les dossiers src (pour le code source) et ressources (pour la notice de lecture
des fichiers générés par RPG Maker).


## Utilisation

Supposons que le fichier .jar contenant la bibliothèque est dans le buildpath, et que l'on veuille compter le nombre
de fois où le message "Lama" est affiché dans les cartes (le nombre de lignes affichant uniquement "Lama").

Le projet RPG Maker est dans le dossier c:\MonSuperJeu\

Dans RPG Maker, on sait (empiriquement) que les affichages de ligne de message sont les instructions 10110 et 20110.


```
public int compterLama() {
	LecteurDeLCF lecteur = new LecteurDeLCF("c:\\MonSuperJeu\\");

	int nombre = 0;

	// On récupère toutes les cartes
	Map<Integer, RMMap> toutesLesCartesAvecLeursID = lecteur.maps();
	Collection<RMMap> cartes = toutesLesCartesAvecLeursID.values();

	for (RMMap carte : cartes) {
		// Pour chaque évènement de la carte
		Collection<RMEvenement> evenements = carte.evenements().values();
		
		for (RMEvenement evenement : evenements) {
			// Pour chaque page de l'évènement
			for (RMPage page : evenement.pages()) {
				for (RMInstruction instruction : page.instructions()) {
					// Pour chaque instruction
					if (afficheLama(instruction)) {
						nombre++;
					}
				}
			}
		}
	}

	return nombre;
}

public boolean afficheLama(RMInstruction instruction) {
	return (instruction.code() == 10110 || instruction.code() == 20110)
		&& instruction.argument().equals("Lama");
}

```



## Crédits

* **Développement du projet par Julian Bruyat** : J'ai réalisé ce projet durant le mois de Septembre 2018 afin de
permettre à mon projet [RMEventReader](https://github.com/BruJu/RMEventMonsterReader) de manipuler directement les
fichiers créés par RPG Maker, sans intermédiaire.


* **Utilisation d'une ressource produite par l'équipe LibLCF** : Le fichier fields.csv (fichier ressource pour connaître
les associations entre tags et données) provient du [projet liblcf](https://github.com/EasyRPG/liblcf), disponible sous
licence MIT et modifié par mes soins. 

