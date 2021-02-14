package site

import play.api.libs.json.{Json, _}
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

case class Extension(ext: String, display:String, model:String, template:String)
case class ExportSite(id:String, name: String, city:String, extensionList: ListBuffer[Extension])

class Site( id:String, name:String, city:String) {

  private val extensionList = new mutable.ListBuffer[Extension]()

  implicit val extensionJson: OFormat[Extension] = Json.format[Extension]
  implicit val exportSiteJson: OFormat[ExportSite] = Json.format[ExportSite]
  //implicit val todoListJson = Json.format[TodoListItem]


  def addExtension(extension: Extension): ListBuffer[Extension] = {
    extensionList += extension
  }

  def getExtensionList: ListBuffer[Extension] = {
    extensionList
  }

  def generateExtList(range: Int, size: Int): List[Extension] = {
    List.range(range, range + size).map( x => Extension( s"$x", s"Extension $x", "Basic", "Basic" ))
  }

  def save(): Unit = {
    val export1 = ExportSite(id, name, city, extensionList)
    val exportStr = Json.toJson(export1)
    println(s"exportStr: $exportStr")
  }

}
