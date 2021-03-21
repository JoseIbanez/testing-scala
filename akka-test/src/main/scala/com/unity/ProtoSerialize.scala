package com.unity

import java.util.Base64
import java.io.{FileInputStream, FileNotFoundException, FileOutputStream}


import appdirectWebhookIngester.Messages.AppDirectEvent

import com.unity.addressbook.{AddressBook, Person}
import com.unity.addressbook.Person.PhoneNumber
import com.unity.addressbook.Person.PhoneType.{MOBILE, WORK}

import scala.util.Try
import scala.io.StdIn
import scala.util.Using

object ProtoTest extends App {


  val p1 = Person(
    id = 33,
    name = "jose",
    email = Some("jose.ibanez@vodafone.com"),
    phones = Seq(PhoneNumber("+33", Option(MOBILE)),PhoneNumber("+91728",Option(WORK)))
  )

  println(p1)

  val s1 =   p1.toByteArray
  val ss1 = Base64.getEncoder().encodeToString(s1)
  println(ss1)

  val s2 = Base64.getDecoder().decode(ss1)

  val p2 = Person.parseFrom(s2)
  println(p2)


}