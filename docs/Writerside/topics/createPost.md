# A propos de la création de post API

<!--Writerside adds this topic when you create a new documentation project.
You can use it as a sandbox to play with Writerside features, and remove it from the TOC when you don't need it anymore.-->

## 1) Ajouter l'image
Ajouter une image en premier lieu sur le endpoint suivant :

``POST - /api/post/image``

Insérer en formData :
- Key "file" et en valeur l'image (jpg, jpeg, png)

Garder le body de la réponse pour la deuxième étape

## 2) Créer le post
Créer un post sur le endpoint suivant :

``POST - /api/post``

Insérer un body similaire à celui-ci :

```json
{
    "userId": "651fce18a3800637f1cecebc",
    "picture": {
        "thumbnail_url": "url",
        "url": "url"
    },
    "geolocation": {
        "posX": 42.2,
        "posY": 42.2,
        "zip": 76100,
        "city": "Rouen",
        "country": "France",
        "address": "address"
    }
}
```