import models.NewRequestItem
import org.scalatest.FunSuite

//import javax.inject._
import models.{NewTodoListItem, TodoListItem}
import models.NewRequestItem
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
//import scala.collection.mutable

class JsonSpec extends FunSuite {
  test("CubeCalculator.cube") {
    assert(27 === 27)
  }

  test( "ListTest") {

    val aList: List[NewRequestItem] = List(
      NewRequestItem("Office 233", 3, "AZ1"),
      NewRequestItem("Office 333", 10, "AZ2"),
      NewRequestItem("Lobby 22", 2, "AZ1"))
    println(aList)

    val az1List = aList.filter(item => item.zone == "AZ1")
    println(az1List)


  }


  test("BasicJsonTest") {
    implicit val newTodoListJson = Json.format[NewTodoListItem]

    val body: String = """{"color":"red", "description": "some new item"}"""
    val jsonObject = Some(Json.parse(body))

    val newItem = jsonObject.flatMap(Json.fromJson[NewTodoListItem](_).asOpt)
    println(newItem)
    assert(newItem.get.color === "red")
  }

}
