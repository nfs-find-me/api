# Endpoints

## Post
| Nom                 | Méthode | Url                          |
|---------------------|---------|------------------------------|
| FindAll             | GET     | /api/post                    |
| GetOne              | GET     | /api/post/{id}               |
| FindAll by UserId   | GET     | /api/post/user/{id}          |
| Create              | POST    | /api/post                    |
| PutOne              | PUT     | /api/post/{id}               |
| Filter Most Viewed  | GET     | /api/post/most-viewed        |
| Filter Most Liked   | GET     | /api/post/most-liked         |
| Filter Most Popular | GET     | /api/post/most-popular       |
| Filter Most Recent  | GET     | /api/post/most-recent        |
| Filter Oldest       | GET     | /api/post/oldest             |
| FindAll Following   | GET     | /api/post/following/{userId} |
| Search by username  | GET     | /api/post/search/{username}  |
| Like post           | POST    | /api/post/{id}/toggle-like/  |
| Delete post         | DELETE  | /api/post/{id}               |


## User

| Admin only | Nom              | Méthode | Url                   |
|------------|------------------|---------|-----------------------|
| ✔          | FindAll          | GET     | /api/user             |
|      ✔      | Create           | POST    | /api/user             |
|            | GetOne           | GET     | /api/user/{id}        |
|            | PutOne           | PUT     | /api/user/{id}        |
|            | DeleteOne        | DELETE  | /api/user/{id}        |
|            | Avatar           | POST    | /api/user/avatar/{id} |
|            | Follow           | GET     | /api/user/follow      |
|            | FindAll by score | GET     | /api/user/filterScore |
|    ✔        | Ban              | POST    | /api/user/ban/{id}    |
