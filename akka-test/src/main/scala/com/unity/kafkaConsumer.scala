package com.unity

import akka.stream._
import akka.stream.scaladsl._
import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.util.ByteString

import scala.concurrent._
import scala.concurrent.duration._
import java.nio.file.Paths

import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer


object KafkaTest extends App {

  implicit val system: ActorSystem = ActorSystem("QuickStart")
  //implicit val ec: ExecutionContext = system.executionContext
  implicit val config: Config = ConfigFactory.load()

  val kafkaConfig = config.getConfig("com.vodafone.ordering.our-kafka-consumer")
  //val config = system.settings.config.getConfig("our-kafka-consumer")


  //val resultSink = Sink.head[Int]
  val resultSink = Sink.foreach(println)

  def myAction2(x:String):String = {
    val y = x
    println(s"String: ${y}")
    y
  }

  def myAction(x:Int):Int = {
    val y = x+1
    Thread.sleep(100)
    println(s"y:${y}")
    y
  }

  val g = RunnableGraph.fromGraph(GraphDSL.create(resultSink) { implicit builder => sink =>
    import GraphDSL.Implicits._
    //val in = Source(1 to 20)
    //val out = Sink.ignore
    val in = kafkaConsumer()

    //val bcast = builder.add(Broadcast[Int](2))
    //val merge = builder.add(Merge[Int](2))

    //val f1, f2, f3, f4 = Flow[Int].map( (x) => { println(x); x+1} )
    //val f1 = Flow[Int].map( (x) => { println(s"x:${x+1}"); x+1 } )
    //val f1 = Flow[Int].map(myAction)
    val f1 = Flow[Int].map( _.toString() )
    val f2 = Flow[String].map(myAction2)

    //in ~> f1 ~> bcast ~> f2 ~> merge ~> f3 ~> sink
    //bcast ~> f4 ~> merge

    in ~> f2 ~> sink
    ClosedShape
  })


  //Ref:
  //https://doc.akka.io/docs/akka/current/stream/stream-quickstart.html

  private def kafkaConsumer() = {
    val kafkaTopic = "initial-orders"
    //val groupId = config.getString("com.vodafone.ordering.consumers.kafkaInitialOrderGroupId")
    val groupId = "group1"
    val kafkaConsumerSettings = ConsumerSettings(kafkaConfig, new StringDeserializer, new StringDeserializer)
      .withGroupId(groupId)
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
      .withStopTimeout(0.seconds)

    Consumer.plainSource(kafkaConsumerSettings, Subscriptions.topics(kafkaTopic))
      .map(_.value)
  }


  kafkaConsumer.runWith(Sink.foreach(myAction2))

  println("Running")

  //val max: Future[Done] = g.run()
  //val result = Await.result(max, 120*1000.millis)

  //println(s"Result: ${result}")

  Thread.sleep(120*1000)
  system.terminate()

}
