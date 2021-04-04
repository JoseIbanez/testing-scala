package poc


import com.vodafone.ucc.middleware.appdirectwebhookingester.messages.AppDirectEvent.MarketplaceInformation
import spray.json._

case class SampleData(name: String, value: Int)
case class Site(street: String, number: String, city: String)

import com.vodafone.ucc.middleware.appdirectwebhookingester.messages.AppDirectEvent.{AppDirectSubscriptionOrderEvent, CompanyAddress, CompanyLocation, CompanyInformation, ContactDetails, OpcoInformation, OrderDetails}

object SampleDataSprayProtocol extends DefaultJsonProtocol {
  implicit val sampleDataProtocol: RootJsonFormat[SampleData] = jsonFormat2(SampleData)
  implicit val siteProtocol: RootJsonFormat[Site] = jsonFormat3(Site)
}


import SampleDataSprayProtocol._

// from protobuf to json: https://scalapb.github.io/docs/json/
import scalapb.json4s.JsonFormat


object JacksonSerializeTest extends App {

  val s1 = SampleData("name1", 10)
  val s1Serial = s1.toJson
  println(s"s1Serial: ${s1Serial}")

  val s2Serial: String = """{"name":"name1", "value":10}"""
  val s2 = s2Serial.parseJson.convertTo[SampleData]
  println(s"s2: ${s2}")

  val s3 = Site("C/Castellana","22bis","Madrid")
  val s3Serial = s3.toJson.compactPrint
  println(s"s3Serial: ${s3Serial}")

  // Errors
  //val s3b = s3Serial.parseJson.convertTo[SampleData]
  //println(s"s3b: ${s3b}")

  val s3c = s3Serial.parseJson

  println(s3c)

  // Testing proto clases to json serialize
  val order1 = OrderDetails(10, "skuID:31", "opcoCustomerID:1")
  val order2 = OrderDetails(5, "skuID:32", "opcoCustomerID:2")

  val j1: String = JsonFormat.toJsonString(order1)
  println(s"j1: ${j1}")

  val order3: OrderDetails = JsonFormat.fromJsonString[OrderDetails](j1)
  println(s"order3: ${order3}")


  // appDirectEvent class test

  val address1 = CompanyAddress("Av. America 115", "", "Madrid","Spain","28042")
  val location1 = CompanyLocation("Vodafone Plaza",Option(address1))
  val contact1 = ContactDetails("Jose", "Ibanez", "jose.ibanez@vodafone.com","+34666777666")
  val company1 = CompanyInformation("VF-ES",Option(location1))
  val opcoId1 = OpcoInformation("VF-ES","opcoCustomerID:1")
  val order11 = OrderDetails(1, "skuID:33", "opcoCustomerID:1")
  val marketPlace = MarketplaceInformation(partnerName = "VF-ES", marketplaceBaseURL = "http://locahost")
  val ev = AppDirectSubscriptionOrderEvent("partner1", Option(marketPlace), Option(company1),"externalAcountId",Option(contact1),Option(order11),Option(opcoId1))

  println(JsonFormat.toJsonString(ev))


}

