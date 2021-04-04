package poc


import java.util.{Base64, Properties}

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import poc.SerializeTest.ev
import scalapb.json4s.JsonFormat
import com.vodafone.ucc.middleware.appdirectwebhookingester.messages.AppDirectEvent.{AppDirectSubscriptionOrderEvent, CompanyAddress, CompanyInformation, CompanyLocation, ContactDetails, MarketplaceInformation, OpcoInformation, OrderDetails}


class TestProducer {

  val props = new Properties()
  props.put("bootstrap.servers", "kafka-server:9092")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  val producer = new KafkaProducer[String, String](props)

  def write(topic: String, value: String): Unit = {
    val record = new ProducerRecord[String, String](topic, "key", value)
    producer.send(record)
  }

  def close(): Unit = {
    producer.close()
  }

}

object KafkaProducerTest extends App {

  val p1 = new TestProducer

  p1.write("license", "---starting---")

  val order1 = OrderDetails(10, "skuID:31", "opcoCustomerID:1")
  val order2 = OrderDetails(5, "skuID:32", "opcoCustomerID:2")

  val ss1 = Base64.getEncoder.encodeToString(order1.toByteArray)
  val ss2 = Base64.getEncoder.encodeToString(order2.toByteArray)

  println(s"${order1} -> ${ss1}")
  println(s"${order2} -> ${ss2}")

  p1.write("license", ss1)
  p1.write("license", ss2)


  val addressList = List(
    CompanyAddress("Vodafone Plaza","Av. America 115","Madrid","Spain","28042"),
    CompanyAddress("Vodafone Group Services GmbH","Rehhecke 50","Ratingen","Germany","40885"),
    CompanyAddress("Vodafone HQ Newbury","The Connection","Newbury","United Kingdom","RG14 2FN"),
    CompanyAddress("Vodafone Italy", "Via Lorenteggio, 240", "Milano MI", "Italy",  "20147")
  )
  val comp1 = CompanyLocation("location1",Option(addressList(1)))

  println(s"comp1: ${comp1}")



  ///////////////

  p1.write("order", "---starting---")
  p1.write("location", "---starting---")

  val r1 = Range(100,111)
  r1.map(id => {

    // OrderDetails
    val order = OrderDetails(id%10, s"skuID:${id%5}", s"opcoCustomerID:${id}")
    println(order)
    val orderEncoded = Base64.getEncoder.encodeToString(order.toByteArray)
    p1.write("order", orderEncoded)


    // CompanyLocation
    val location = CompanyLocation(s"opcoCustomerID:${id}", Option(addressList(id%4)))
    println(location)
    val locationEncoded = Base64.getEncoder.encodeToString(location.toByteArray)
    p1.write("location", locationEncoded)


    Thread.sleep(100)
  })


  /////
  val address1 = CompanyAddress("Av. America 115", "", "Madrid","Spain","28042")
  val location1 = CompanyLocation("Vodafone Plaza",Option(address1))
  val contact1 = ContactDetails("Jose", "Ibanez", "jose.ibanez@vodafone.com","+34666777666")
  val company1 = CompanyInformation("VF-ES",Option(location1))
  val opcoId1 = OpcoInformation("VF-ES","opcoCustomerID:1")
  val order11 = OrderDetails(1, "skuID:33", "opcoCustomerID:1")
  val marketPlace = MarketplaceInformation(partnerName = "VF-ES", marketplaceBaseURL = "http://locahost")
  val ev = AppDirectSubscriptionOrderEvent("partner1", Option(marketPlace), Option(company1),"externalAcountId",Option(contact1),Option(order11),Option(opcoId1))


  p1.write("appDirectEvent", JsonFormat.toJsonString(ev))


}