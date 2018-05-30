package models

import java.util.concurrent.atomic.AtomicInteger
import javax.inject.{Inject, Singleton}

import anorm.RowParser
import play.api.db.Database
import play.api.libs.json.{Format, Json}
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Task(var id: Option[Long], label: String, who: String, mytime: String, ready: Short)

object Task {
  implicit val taskJsonFormat: Format[Task] = Json.format[Task]

  val simple = RowParser[Task] {
    long("id") ~ str("label") ~ str("who") ~ str("mytime") ~ str("ready").map {
      case id ~ label ~ who ~ mytime ~ ready => new Task(id, label, who, mytime, ready)
    }
  }
}

@Singleton
class TaskDao @Inject()(db: Database) {

  import Task._

  def apply(label: String, who: String, mytime: String, ready: Short): Task =
    Task(Option.empty, label, who, mytime, ready)

  var tasks = Map[Long, Task]()
  val count = new AtomicInteger()

  def all(): List[Task] = tasks.values.toList.sorted(Ordering.by((task: Task) => task.id).reverse)

  import anorm._

  def allDb(): List[Task] = {
    db.withConnection { implicit connection =>
      SQL("select * from task").as(Task.simple *)
    }
  }

  def create(label: String, who: String, time: String): Unit = {
    //    val task = Task(label, who, time, 1)
    //    create(task)
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
