Sprint-10
Database schema https://dbdiagram.io/d/629c8baf54ce26352760e6cb

Get user by ID

SELECT * FROM users WHERE user_id = n;

Get film by ID

SELECT * FROM films WHERE film_id = n;

Get films by rating

SELECT * FROM films WHERE rating = ‘PG’;

Get films by genre

SELECT * FROM films WHERE films_id IN ( SELECT film_id FROM genres WHERE genre = ‘Триллер’);

Get somebody’s friends

SELECT * FROM users WHERE user_id IN (SELECT friend_two FROM friends WHERE user_one = n AND status = ‘ACCEPTED’);

Get somebody’s mutual friends

SELECT * FROM users WHERE user_id IN (SELECT * FROM (SELECT friend_two FROM friends WHERE user_one = X AND status = ‘ACCEPTED’) AS set_one INNER JOIN (SELECT friend_two FROM friends WHERE user_two = Y AND status = ‘ACCEPTED’) AS set_two ON set_one.user_id = set_two.user_id);

Top 10 films by likes

SELECT * FROM films WHERE film_id IN (SELECT film_id as top_id GROUP BY film_id ORDER BY COUNT(likes) DESC LIMIT 10);
