package site

import scala.collection.mutable

case class Extension(ext: String, display:String, model:String, template:String)

class Site(name:String, city:String) {

  private val extensionList = new mutable.ListBuffer[Extension]()

  def addExtension(display: String, template: String)

  def generateExtList(range: Int, size: Int): List[Extension] = {
    List.range(range, range + size).map( x => Extension( s"$x", s"Extension $x", "Basic", "Basic" ))
  }

}
