package com.unity

import java.util.Base64
import java.io.{FileInputStream, FileNotFoundException, FileOutputStream}

import scalapb.json4s.JsonFormat

import scala.util.Try
import scala.io.StdIn
import scala.util.Using
//import appdirectWebhookIngester.Messages.AppDirectEvent
import appdirectWebhookIngester.Messages.AppDirectEvent.{AppDirectEvent, CompanyAddress, CompanyInformation, CompanyLocation, ContactDetails, OpcoInformation, OrderDetails}


object AppDirectProtoTest extends App {

  val address1 = CompanyAddress("Av. America 115", "", "Madrid","Spain","28042")
  val location1 = CompanyLocation("Vodafone Plaza",Option(ca1))
  val contact1 = ContactDetails("Jose", "Ibanez", "jose.ibanez@vodafone.com","+34666777666")
  val company1 = CompanyInformation("VF-ES",Option(cl1))
  val opcoId1 = OpcoInformation("VF-ES","opcoCustomerID:1")
  val order1 = OrderDetails(1, "skuID:33", "opcoCustomerID:1")

  val ev = AppDirectEvent("partner1",Option(company1),"externalAcountId",Option(contact1),Option(order1),Option(opcoId1))


  //println(JsonFormat.toJsonSring(ev))


  println(ev)

  val s1 =   ev.toByteArray

  val ss1 = Base64.getEncoder().encodeToString(s1)
  println(ss1)

  val s2 = Base64.getDecoder().decode(ss1)

  val o2 = AppDirectEvent.parseFrom(s2)
  println(o2)

}
