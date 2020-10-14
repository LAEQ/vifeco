Installation Vifeco

1. Télécharger la dernière version (zip)

https://github.com/LAEQ/vifeco/releases 

2. Unzip / Extraire le zip à l’endroit désiré (e.g: My Documents)

Vous pouvez garder plusieurs versions installées. Vous pouvez supprimer les répertoires dézippés mais pas le répertoire créé par l’application (voir point 4)

3. Se rendre dans le dossier vifeco-x.x.x/bin

- Windows: double cliquez sur vifeco.bat
- Linux: exécuter le script shell vifeco

4. Lors de la première exécution, le script va créer un répertoire pour l’application:

- Windows: C:\Users\<your_name>\vifeco
- Linux: ~/vifeco

Structure du dossier:
vifeco:
db: contient les fichiers de la bd MySQL
export: emplacement ou les fichiers json sont ‘exportés’
import: emplacement ou l’on ajoute les fichiers json pour faire une comparaison statistique
statistic: fichier généré pour faire les calculs stats entre
