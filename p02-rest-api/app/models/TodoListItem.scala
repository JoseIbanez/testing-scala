package models

case class TodoListItem(id: Long, description: String, isItDone: Boolean)
case class NewTodoListItem(description: String, color: String)

case class RequestItem(
  id: Long,
  description: String,
  size: String,
  zone: String,
  state: String
)

case class NewRequestItem(
  description: String,
  size: Int,
  zone: String)