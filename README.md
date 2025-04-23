# java-explore-with-me
Template repository for ExploreWithMe project.

# Реализация дополнительной функциональности: likes
1) POST /users/{userId}/events/{eventId}/likes
- добавление лайка событию
2) DELETE /users/{userId}/events/{eventId}/likes/{likeId}
- удаление лайка
3) PATCH /users/{userId}/events/{eventId}/likes/{likeId}
- редактирование лайка
4) GET /users/{userId}/events/{eventId}/likes/{likeId}
- получение лайка по идентификатору
5) GET /users/{userId}/likes
- получение всех лайков пользователя
6) GET /users/{userId}/events/{eventId}/likes
- получение всех лайков события

## Интеграция

- Private API

## Логика

- Лайки можно показывать публично
- Нельзя ставить лайк своему событию
- Лайк можно поставить только посещенному мероприятию
- Лайк можно поставить только через 3 часа после начала мероприятия
- Лайк можно поставить только опубликованному событию
- Удалять лайки могут только пользователи, которые их поставили
- Нет ограничений на время редактирования лайка