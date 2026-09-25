# Sekoya

## Présentation

* Plus d'informations sur le site de [Sekoya](https://sekoya.io)
* Les données utilisées sont issues de la [DRIAS](https://www.drias-climat.fr/)
* La méthodologique de l'application repose sur [OCARA pour PME](https://librairie.ademe.fr/changement-climatique/7870-ocara-pour-pme-analyser-la-resilience-climatique-et-developper-un-premier-plan-d-adaptation-a-l-echelle-de-l-entreprise.html) développée par l'ADEME, BPI France et Carbone4

## Prérequis

* Java 25
* Maven 3.9
* PostgreSQL 18 + Postgis 3.6
* Docker (pour le développement)


## Démarrage

Les commandes présentées sont adaptées à un environnement GNU/Linux.

Elles sont adaptables à tout environnement fournissant les outils docker,
java et maven.

```shell
mvn clean install -DskipTests -Dspring-boot.build-image.skip=false
docker compose --profile app -f docker/docker-compose.yml up
```

Le message suivant indique la fin du démarrage :

```
app-1       | [2026-09-18T10:00:30,441] INFO  - SekoyaApplicationMain      - userId: - host: -  - Started SekoyaApplicationMain in 43.676 seconds (process running for 44.155)
```

L'application est disponible sur http://localhost:8080 (identifiant/mot de passe : `admin/sekoya`).

Les mails envoyés par l'application sont disponibles sur http://localhost:2085

Autres commandes :

```shell
# Démarrage de la stack docker sans l'application embarquée
docker compose -f docker/docker-compose.yml up
# Initialisation d'un répertoire applicatif
sudo mkdir /data/services/sekoya
sudo chown $USER: /data/services/sekoya
# L'application peut ensuite être lancée via la classe main *SekoyaApplicationMain* (module sekoya-app)
# (via Eclipse, Intellij, vscode, ...)
# Exemple de lancement maven
mvn -pl sekoya-app spring-boot:run

# Arrêt de la stack docker
docker compose --profile app -f docker/docker-compose.yml down

# Suppression de l'ensemble de la stack docker, données comprises
docker compose --profile app -f docker/docker-compose.yml down -v

# Accès direct à la base de données
psql -U sekoya -p 2532 -h localhost
```