package models

import java.util.concurrent.atomic.AtomicInteger

import play.api.libs.json.Json

case class Task(var id: Option[Long], label: String, who: String, mytime: String, ready: Short) {
}

object Task {
  implicit val taskJsonFormat = Json.format[Task]

  def apply(label: String, who: String, mytime: String, ready: Short) : Task =
    Task(Option.empty, label, who, mytime, ready)

  var tasks = Map[Long, Task]()
  val count = new AtomicInteger()

  def all(): List[Task] = tasks.values.toList.sorted(Ordering.by((task: Task) => task.id).reverse)

  def create(label: String, who: String, time: String): Unit = {
    val task = Task(label, who, time, 1)
    create(task)
  }

  def create(task: Task): Unit = {
    val id = count.addAndGet(1).longValue()
    tasks += id -> task
    task.id = Option(id)
  }

  def delete(id: Long) {
    tasks -= id
  }

  def complete(id: Long) {}

}
