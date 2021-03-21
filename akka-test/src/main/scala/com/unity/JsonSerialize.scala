package com.unity


import spray.json._

final case class SampleData(name: String, value: Int)
final case class Site(street: String, number: String, city: String)

object SampleDataSprayProtocol extends DefaultJsonProtocol {
  implicit val sampleDataProtocol: RootJsonFormat[SampleData] = jsonFormat2(SampleData)
  implicit val siteProtocol: RootJsonFormat[Site] = jsonFormat3(Site)
}
import SampleDataSprayProtocol._



object SerializeTest extends App {

  val s1 = SampleData("name1", 10)
  val s1Serial = s1.toJson
  println(s"s1Serial: ${s1Serial}")

  val s2Serial: String = """{"name":"name1", "value":10}"""
  val s2 = s2Serial.parseJson.convertTo[SampleData]
  println(s"s2: ${s2}")

  val s3 = Site("C/Castellaña","22bis","Madrid")
  val s3Serial = s3.toJson.compactPrint
  println(s"s3Serial: ${s3Serial}")

  // Errors
  //val s3b = s3Serial.parseJson.convertTo[SampleData]
  //println(s"s3b: ${s3b}")

  val s3c = s3Serial.parseJson

  print(s3c)

}