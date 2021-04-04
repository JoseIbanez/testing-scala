package poc


case class SampleDataB(name: String, value: Int)
case class SiteB(street: String, number: String, city: String)

import com.fasterxml.jackson.databind.json.JsonMapper


/*
object SampleDataSprayProtocol extends DefaultJsonProtocol {
  implicit val sampleDataProtocol: RootJsonFormat[SampleData] = jsonFormat2(SampleData)
  implicit val siteProtocol: RootJsonFormat[Site] = jsonFormat3(Site)
}
*/

//Spray
//import SampleDataSprayProtocol._

//Jacson
import org.apache.kafka.common.serialization.Deserializer
import org.json4s.jackson.Serialization.read
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

// Project
import com.vodafone.ucc.middleware.appdirectwebhookingester.messages.AppDirectEvent.{AppDirectSubscriptionOrderEvent, MarketplaceInformation, CompanyAddress, CompanyLocation, CompanyInformation, ContactDetails, OpcoInformation, OrderDetails}



// from protobuf to json: https://scalapb.github.io/docs/json/
//import scalapb.json4s.JsonFormat



object SerializeTest extends App {
  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)
  //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  // verion >= 2.10
  val mapper1 = JsonMapper.builder()
    .addModule(DefaultScalaModule)
    .build()

  val s1 = SampleData("name1", 10)
  val j1 = mapper1.writeValueAsString(s1)
  println(j1)

  val j2: String = """{"name":"name1", "value":10}"""
  val s2 = mapper1.readValue(j2,classOf[SampleData])
  println(s2)

  val s3 = Site("C/Castellana","22bis","Madrid")

  // appDirectEvent class test
  val address1 = CompanyAddress("Av. America 115", "", "Madrid","Spain","28042")
  val location1 = CompanyLocation("Vodafone Plaza",Option(address1))
  val contact1 = ContactDetails("Jose", "Ibanez", "jose.ibanez@vodafone.com","+34666777666")
  val company1 = CompanyInformation("VF-ES",Option(location1))
  val opcoId1 = OpcoInformation("VF-ES","opcoCustomerID:1")
  val order11 = OrderDetails(1, "skuID:33", "opcoCustomerID:1")
  val marketPlace = MarketplaceInformation(partnerName = "VF-ES", marketplaceBaseURL = "http://locahost")
  val ev = AppDirectSubscriptionOrderEvent("partner1", Option(marketPlace), Option(company1),"externalAcountId",Option(contact1),Option(order11),Option(opcoId1))

  val j3 = mapper1.writeValueAsString(ev)
  println(j3)


}

