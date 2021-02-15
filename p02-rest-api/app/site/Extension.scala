package site

import play.api.libs.json.{Json, _}
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import os.{write, pwd}

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

  def generateExtList(range: Int, size: Int): ListBuffer[Extension] = {
    extensionList ++= List.range(range, range + size).map( x => Extension( s"$x", s"Extension $x", "Basic", "Basic" ))
  }

  def save(): Unit = {
    val export1 = ExportSite(id, name, city, extensionList)
    val exportJson = Json.toJson(export1)
    os.write.over(os.pwd/"tmp"/s"$id.json",exportJson.toString())
  }

  def load(id: String): ExportSite = {
    val importStr = os.read(os.pwd/"tmp"/s"$id.json")
    val importJson = Some(Json.parse(importStr))
    //val import1: ExportSite = Json.fromJson(importJson)
    val import1 : Option[ExportSite] = importJson.flatMap(Json.fromJson[ExportSite](_).asOpt)
    import1 match {
      case Some(import2) =>
        import2
      case None =>
        val extensionList2 = new mutable.ListBuffer[Extension]()
        ExportSite(id,"","", extensionList2)
    }

  }


}
