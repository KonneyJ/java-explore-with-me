# java-explore-with-me
Template repository for ExploreWithMe project.

#Ссылка на pull-request
https://github.com/KonneyJ/java-explore-with-me/pull/3

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

- Нельзя ставить лайк своему событию
- Лайк можно поставить только посещенному мероприятию
- Лайк можно поставить только через 3 часа после начала мероприятия
- Лайк можно поставить только опубликованному событию
- Удалять/обновлять лайки могут только пользователи, которые их поставили
- Нет ограничений на время редактирования лайка
- Лайк может посмотреть либо человек, который его поставил, либо владелец события
- Посмотреть все лайки может только владелец события
