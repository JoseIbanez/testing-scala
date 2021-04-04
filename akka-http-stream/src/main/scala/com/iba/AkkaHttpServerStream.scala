package com.iba


import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.marshalling.{Marshaller, PredefinedToEntityMarshallers, ToEntityMarshaller, ToResponseMarshallable}
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, PredefinedFromEntityUnmarshallers}
import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.clients.producer.RecordMetadata
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.headers.`Content-Type`

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}
import com.vodafone.ucc.middleware.appdirectwebhookingester.messages.AppDirectEvent.AppDirectSubscriptionOrderEvent
import spray.json.DefaultJsonProtocol

// (un)marshalling with Protobuf
import scalapb.json4s.JsonFormat

import scala.io.StdIn

object AkkaHttpServerTest extends App {
  //val kafkaProducer = KafkaProducerPlain
  HttpServer.main()

}


object HttpServer extends SprayJsonSupport with LazyLogging {

  // Actor and exectuionContext
  implicit val system = ActorSystem(Behaviors.empty, "my-system")
  implicit val executionContext = system.executionContext

  // Protobuf json unmarshaller
  implicit val orderUnmarshaller: FromEntityUnmarshaller[AppDirectSubscriptionOrderEvent] =
    PredefinedFromEntityUnmarshallers.stringUnmarshaller map { string =>
      JsonFormat.fromJsonString[AppDirectSubscriptionOrderEvent](string)
    }

  implicit val OrderMarshaller: ToEntityMarshaller[AppDirectSubscriptionOrderEvent] =
    Marshaller.withFixedContentType(ContentTypes.`application/json`) { p =>
      HttpEntity(ContentTypes.`application/json`, JsonFormat.toJsonString(p))
    }

  // Auxiliary responses
  val timeoutResponse = HttpResponse(
    StatusCodes.InternalServerError,
    entity = HttpEntity(ContentTypes.`application/json`, """{"result":"Kafka Timeout"}""")
  )

  val responseCreated = HttpResponse(
    status=StatusCodes.Created,
    entity = HttpEntity(ContentTypes.`application/json`, """{"result":"ok"}""")
  )

  def responseCustom(status: StatusCode, message:String): HttpResponse = HttpResponse(
    status = status,
    entity = HttpEntity(ContentTypes.`application/json`, message)
  )


  def main(): Unit = {


    val route =
      (get & path("hello")) {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
      } ~
      (get & path( "group" / Segment / Segment )) { (group,userId) =>
        complete(s"Group:$group, User:$userId")
      } ~
      (post & path( "order" )) { entity(as[AppDirectSubscriptionOrderEvent]) { order =>
        logger.info(order.toString)
        withRequestTimeout(2.second, request => timeoutResponse) {
          val result = KafkaProducerPlain.sendOrder(order)
          onComplete(result) {
            case Success(v) => complete(StatusCodes.Created, order)
            case Failure(ex) => complete(responseCustom(StatusCodes.InternalServerError, s"An error occurred: ${ex.getMessage}"))
          }
        }
      }}


    // Start http server
    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

  }

}
