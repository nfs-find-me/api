# Authentication

## Inscription
Pour se créer un compte, se rendre sur l'endpoint suivant :

``POST - /api/auth/register``

```json
{
    "email": "email",
    "username": "username",
    "password": "password"
}
```

Attention, on ne peut pas se créer un autre compte avec la même adresse mail et le même nom d'utilisateur.

## Connexion
Pour se connecter à son compte, se rendre sur l'endpoint suivant :

``POST - /api/auth/login``

```json
{
    "email": "email",
    "username": "username",
    "password": "password"
}
```

On peut ne mettre que l'adresse mail ou le nom d'utilisateur pour se connecter avec le mot de passe