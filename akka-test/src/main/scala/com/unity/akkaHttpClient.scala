package com.unity

/*
 * Copyright (C) 2020-2021 Lightbend Inc. <https://www.lightbend.com>
 */

package docs.http.scaladsl

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal

import scala.concurrent.Future
import scala.util.{Failure, Success}

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

object HttpClientSingleRequest {



  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem(Behaviors.empty, "SingleRequest")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.executionContext

    case class OrderConfirmation(title:String, url:String)
    implicit val orderFormat = jsonFormat2(OrderConfirmation)


    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = "https://jsonbin.org/remy/blog"))

    responseFuture
      .onComplete {
        case Success(res) => checkResponse(res)
        case Failure(_)   => sys.error("something wrong")
      }

    def checkResponse(res: HttpResponse) = {
      println(Unmarshal(res).to[String])
      println(Unmarshal(res).to[OrderConfirmation])
    }

   }

}




object myHttpClientTest extends App {

  HttpClientSingleRequest.main(Array("a"))
  println("running")


}