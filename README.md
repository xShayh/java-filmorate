# java-filmorate
## Диаграмма базы данных
```mermaid
erDiagram
    FILMS {
        bigint film_id PK
        text name
        text description
        timestamp release_date
        int duration
        bigint mpa_id
    }

    FILMS_GENRES {
        bigint film_id PK
        bigint genre_id PK
    }

    FILMS }o--o{ FILMS_GENRES: connects
    FILMS_GENRES ||--o{ GENRES: contains
    GENRES {
        bigint genre_id PK
        text name
    }

    FILMS ||--o| MPA: contains
    MPA {
        bigint mpa_id PK
        text name
    }

    USERS {
        bigint user_id PK
        text email
        text login
        text name
        timestamp birthday
    }

    USERS ||--o{ LIKES: make
    FILMS ||--o{ LIKES: has
    LIKES {
        bigint film_id PK
        bigint user_id PK
    }

    USERS ||--o{ FRIENDS: has
    FRIENDS {
        bigint user_id PK
        bigint friend_id PK
    }
```
## Примеры запросов
### Получить список всех пользователей
```
SELECT 
    user_id, 
    name, 
    email, 
    login, 
    birthday
FROM 
    users;
```

### Получить список фильмов с указанием жанра
```
SELECT 
    f.film_id, 
    f.name AS film_name, 
    g.name AS genre_name
FROM 
    films f
JOIN 
    films_genres fg ON f.film_id = fg.film_id
JOIN 
    genres g ON fg.genre_id = g.genre_id;
```
