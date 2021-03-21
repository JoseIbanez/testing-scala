package com.unity

import java.util.Base64
import java.io.{FileInputStream, FileNotFoundException, FileOutputStream}

import scala.util.Try
import scala.io.StdIn
import scala.util.Using
//import appdirectWebhookIngester.Messages.AppDirectEvent
import appdirectWebhookIngester.Messages.AppDirectEvent.{AppDirectEvent, CompanyAddress, CompanyInformation, CompanyLocation, ContactDetails, OpcoInformation, OrderDetails}


object AppDirectProtoTest extends App {

  val ca1 = CompanyAddress("Av. America 115", "", "Madrid","Spain","28042")
  val cl1 = CompanyLocation("Vodafone Plaza",Option(ca1))
  val cd1 = ContactDetails("Jose", "Ibanez", "jose.ibanez@vodafone.com","+34666777666")
  val ci1 = CompanyInformation("VF-ES",Option(cl1))
  val oi1 = OpcoInformation("VF-ES","opcoCustomerID:1")
  val order = OrderDetails(1, "skuID:33", "opcoCustomerID:1")

  val ev = AppDirectEvent("partner1",Option(ci1),"externalAcountId",Option(cd1),Option(order),Option(oi1))





  println(ev)

  val s1 =   ev.toByteArray

  val ss1 = Base64.getEncoder().encodeToString(s1)
  println(ss1)

  val s2 = Base64.getDecoder().decode(ss1)

  val o2 = AppDirectEvent.parseFrom(s2)
  println(o2)

}
