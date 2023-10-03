# FindMe API

## Cloner le projet 
``git clone https://github.com/nfs-find-me/api.git``

## Installer les dépendances
``mvn install``

## Configurer la base de données
Créer ce fichier s'il n'existe pas
``src/main/resources/application.properties``
Créer un champs ``spring.data.mongodb.uri`` avec la valeur de l'url de la base de données en ligne Mongo
Créer un champs ``spring.data.mongodb.database`` avec la valeur de le nom de la base de données
Créer un champs ``server.port`` avec le port sur lequel le serveur va tourner
Créer un champs ``jwt.secret`` avec la clé secrète pour le token JWT

## Lancer le projet
``mvn spring-boot:run``

## Lancer les tests
``mvn test``