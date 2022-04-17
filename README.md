# Мини-проект - Chat Service
## Data классы
>### Chat (свойства):
1. id: Int
2. noteId: Int
3. message: String
4. date: Long
5. isDelete: Boolean
>### Message (свойства):
1. id: Int
2. noteId: Int
3. message: String
4. date: Long
5. isDelete: Boolean
## Сервис (реализация логики работы с чатами).
>### ChatsService (свойства)
- notes: MutableList<Note>
- noteCount: Int
- coments: MutableList<Comment>
- commentCount: Int
>### ChatsService (методы CRUD для Чатов)
- add(note: Note): Int
- get(vararg idNotes: Int, userId: Int, offset: Int = 0, count: Int, sort: Int = 0): MutableList<Note>
- getById(id: Int, owner_id:Int): Note
- edit(note: Note): Boolean
- delete(noteIdDelete: Int): Boolean
>### NoteServiсe (методы CRUD для Сообщений)
- createComment(comment: Comment): Int
- getComments(idNote: Int, userId: Int, offset: Int = 0, count: Int, sort: Int = 0): MutableList<Comment>
- editComment(comment: Comment): Boolean
- deleteComment(commentId: Int, owner_id: Int) : Int
- restoreComment(commentId: Int, owner_id: Int): Int