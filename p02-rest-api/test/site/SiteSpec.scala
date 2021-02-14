package site

//package site

import org.scalatest.FunSuite
//import javax.inject._
//import scala.collection.mutable

class SiteSpec extends FunSuite {
  test("site.SiteSpec.extension") {
    val ext200 = Extension("200", "Extension 200", "Basic", "Basic")
    println(ext200)

    val site1 = new Site();
    val list1 = site1.generateExtList(200,20)
    println(list1)
    assert(27 === 27)
  }
}
