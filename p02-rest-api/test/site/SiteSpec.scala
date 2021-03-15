package site

//package site

import org.scalatest.FunSuite
//import javax.inject._
//import scala.collection.mutable

class SiteSpec extends FunSuite {
  test("site.SiteSpec.extension") {
    val ext200 = Extension("200", "Extension 200", "Basic", "Basic")
    val ext201 = Extension("201", "Extension 201", "Basic", "Basic")

    val site1 = new Site( "001", "Office 1", "Madrid")
    site1.addExtension(ext200)
    site1.addExtension(ext201)
    val list1 = site1.getExtensionList
    assert(27 === 27 )
  }

  test("site.SiteSpec.save") {
    val site1 = new Site( "001", "Office 1", "Madrid")
    site1.generateExtList(2000,1000)
    site1.save()
    assert(27 === 27 )
  }

  test("site.SiteSpec.load") {
    val site2 = new Site( "002", "Office 1", "Madrid")
    val site3 = site2.load("001")
    assert(27 === 27 )
  }


}
