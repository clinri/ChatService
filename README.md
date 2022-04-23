# Мини-проект - Chat Service
## Data классы
>### Chat (свойства):
1. idChat: Int
2. users: List<Int>
3. messages: List<Message>
>### Message (свойства):
1. idMessage: Int
2. ownerId: Int
3. message: String
4. isRead: Boolean
## Сервис (реализация логики работы с чатами).
>### ChatsService (свойства)
- notes: MutableList<Note>
- chatsCount: Int
- messageCount: Int
>### ChatsService (методы CRUD для Чатов)
- getUnreadChatsCount(idUser: Int): Int
- getChats(idUser: Int): List<Chat>
- deleteChat(idUser: Int, idChat: Int): Boolean
>### ChatsServiсe (методы CRUD для Сообщений)
- sendMessage(userSenderId: Int, userReceiverId: Int, textMessage: String): Int
- getMessages(idUser: Int, idChat: Int, idOffset: Int = 0, count: Int): List<Message>
- deleteMessage(idUser: Int, idMessage: Int): Boolean
- updateMessage(idUser: Int, idMessage: Int, textMessage: String): Message
>### ChatsServiсe (прочие методы )
- removeAll()
- createChats()