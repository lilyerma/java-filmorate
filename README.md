# Sprint-10
Database schema
https://dbdiagram.io/d/629c8baf54ce26352760e6cb

__Get user by  ID__

`SELECT * FROM users
WHERE user_id = n;`

__Get film by ID__

`SELECT * FROM films
WHERE film_id = n;`

__Get films by rating__

`SELECT * FROM films
WHERE rating = ‘PG’;`

__Get films by genre__

`SELECT * FROM films
WHERE films_id IN (
SELECT film_id FROM genres
WHERE genre = ‘Триллер’);`

__Get somebody’s friends__

`SELECT * FROM users
WHERE user_id IN
(SELECT friend_two FROM friends
WHERE user_one = n AND status = ‘ACCEPTED’);`

__Get somebody’s mutual friends__

`SELECT * FROM users
WHERE user_id IN
(SELECT * FROM
(SELECT friend_two FROM friends
WHERE user_one = X AND status = ‘ACCEPTED’) AS set_one
INNER JOIN
(SELECT friend_two FROM friends
WHERE user_two = Y AND status = ‘ACCEPTED’) AS set_two
ON set_one.user_id = set_two.user_id);`



__Top 10 films by likes__

`SELECT * FROM films
WHERE film_id IN
(SELECT film_id as top_id
GROUP BY film_id
ORDER BY COUNT(likes) DESC
LIMIT 10);
`
