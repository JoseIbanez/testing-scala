package com.unity

import akka.NotUsed
import akka.actor.typed.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods.POST
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.model.headers.Accept
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, MediaRanges}
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.ClosedShape
import akka.stream.scaladsl.{Flow, GraphDSL, RunnableGraph, Sink, Source}
import com.typesafe.config.{Config, ConfigFactory}
import com.vodafone.ordering.actors.OrderRegistry.OrderCreationResponse
import com.vodafone.ordering.marshaller.{JsonMarshalling, OrderDeserializer}
import com.vodafone.ordering.model.Order
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write
import akka.http.scaladsl.unmarshalling.Unmarshal
import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

class Consumers()(implicit val system: ActorSystem[_]) extends JsonMarshalling {

  implicit val ec: ExecutionContext = system.executionContext
  implicit val config: Config = ConfigFactory.load()

  def apply() = {
    //    val kafkaImage = DockerImageName.parse("wurstmeister/kafka")
    //    val kafka = new KafkaContainer(kafkaImage)
    //    kafka.start()

    val graph = RunnableGraph.fromGraph(GraphDSL.create() {
      implicit builder: GraphDSL.Builder[NotUsed] =>
        import GraphDSL.Implicits._

        val in = builder.add(kafkaConsumer())
        val sendOrderViaHTTP = builder.add(Flow[Order].map(httpRequest))
        val sink = Sink.ignore

        in ~> sendOrderViaHTTP ~> sink
        ClosedShape
    })
    graph.run()

    //    Thread.sleep(20 * 1000) // For testing - let's let it run for a bit then die
    //    consumer.shutdown
  }

  private def kafkaConsumer() = {
    val kafkaTopic = "initial-orders"
    val groupId = config.getString("com.vodafone.ordering.consumers.kafkaInitialOrderGroupId")
    val kafkaConsumerSettings = ConsumerSettings(system, new StringDeserializer, new OrderDeserializer)
      .withGroupId(groupId)
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
      .withStopTimeout(0.seconds)

    Consumer.plainSource(kafkaConsumerSettings, Subscriptions.topics(kafkaTopic))
      .map(_.value)
  }

  def httpRequestForInitialOrders(order: Order) = {
    implicit val formats = DefaultFormats

    val apiVersion = config.getString("com.vodafone.order.api.version")
    val ringCentralHost = config.getString("com.vodafone.ordering.ringMain.host")
    val ringCentralPort = config.getString("com.vodafone.ordering.ringMain.port")
    val ringCentralUrl = s"${ringCentralHost}:${ringCentralPort}"
    val accountsUrlPath = s"${ringCentralUrl}/restapi/v${apiVersion}/accounts/"
    HttpRequest(uri = s"${accountsUrlPath}/initial-order")
      .withHeaders(Accept(MediaRanges.`application/*`))
      .withMethod(POST)
      .withEntity(write(order))
  }

  private def extractResponse(response: HttpResponse) = {
    import org.squbs.marshallers.json.XLangJsonSupport._

    response match {
      case HttpResponse(OK, _, entity, _) => Unmarshal(response).to[Source[OrderCreationResponse, NotUsed]]
      case notOkResponse =>
        Future.failed(new RuntimeException(s"Error not OK response received: $notOkResponse"))
    }
  }

  //  private def httpRequest(order: Order)(implicit um: Unmarshaller[HttpResponse, Source[OrderCreationResponse, NotUsed]]): Future[Future[Source[OrderCreationResponse, NotUsed]]] = {
  private def httpRequest(order: Order) = {
    val request = httpRequestForInitialOrders(order)
    Http(system)
      .singleRequest(request)
      .map(extractResponse)
    //      .map(JsonMethods.parse(_).extract[Order])
    //      .andThen(read[Order](_))
  }
}


HttpResponse(301 Moved Permanently,List(Date: Wed, 17 Mar 2021 15:19:07 GMT, Connection: keep-alive, Set-Cookie: __cfduid=ddd88e3792ccd1e755de4145d7c2eee921615994347; Expires=Fri, 16 Apr 2021 15:19:07 GMT; Domain=akka.io; Path=/; HttpOnly; SameSite=Lax, Location: https://akka.io/, CF-Cache-Status: DYNAMIC, cf-request-id: 08e25df7a30000dfc3ef3ff000000001, Report-To: {"group":"cf-nel","endpoints":[{"url":"https:\/\/a.nel.cloudflare.com\/report?s=Tcj7UuoVE3cEhBE55aa4UuVY9SbZeyLuUhIYXtFhy8b0iVCVILazEjvzmVBaCxbVeE4IIsdSp0s1BfqNc89xaBT3DWzLL1Th"}],"max_age":604800}, NEL: {"max_age":604800,"report_to":"cf-nel"}, Server: cloudflare, CF-RAY: 6317329f6e69dfc3-FRA, alt-svc: h3-27=":443"; ma=86400, h3-28=":443"; ma=86400, h3-29=":443"; ma=86400),HttpEntity.Chunked(text/html; charset=ISO-8859-1),HttpProtocol(HTTP/1.1))
