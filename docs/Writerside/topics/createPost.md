# A propos de la création de post API

<!--Writerside adds this topic when you create a new documentation project.
You can use it as a sandbox to play with Writerside features, and remove it from the TOC when you don't need it anymore.-->

## Créer le post
Créer un post sur le endpoint suivant :

``POST - /api/post``

Insérer en formData :
- Key "file" et en valeur l'image (jpg, jpeg, png)
- Key "data" et en valeur le json
```json
{
  "userId": "651fce18a3800637f1cecebc",
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