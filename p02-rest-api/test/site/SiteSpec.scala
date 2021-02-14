package site

//package site

import org.scalatest.FunSuite
//import javax.inject._
//import scala.collection.mutable

class SiteSpec extends FunSuite {
  test("site.SiteSpec.extension") {
    val ext200 = Extension("200", "Extension 200", "Basic", "Basic")
    val ext201 = Extension("201", "Extension 201", "Basic", "Basic")
    println(ext200)

    val site1 = new Site( "001", "Office 1", "Madrid")
    site1.addExtension(ext200)
    site1.addExtension(ext201)
    val list1 = site1.getExtensionList()
    println(s"list1: $list1")
    site1.save()


    assert(27 === 27 )
  }
}
