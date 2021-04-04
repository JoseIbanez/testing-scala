package com.iba

import akka.actor.typed.{ActorSystem}
import akka.actor.typed.scaladsl.Behaviors
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.{SendProducer}
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.LazyLogging
import com.vodafone.ucc.middleware.appdirectwebhookingester.messages.AppDirectEvent.AppDirectSubscriptionOrderEvent
import org.apache.kafka.clients.producer.{ProducerRecord, RecordMetadata}
import org.apache.kafka.common.serialization.StringSerializer
import scalapb.json4s.JsonFormat

import scala.concurrent.{Await, ExecutionContext, Future}




object KafkaProducerPlain extends LazyLogging  {

  implicit val system = ActorSystem(Behaviors.empty, "producer")
  implicit val config: Config = ConfigFactory.load()
  val kafkaConfig: Config = config.getConfig("com.vodafone.ucc.middleware.our-kafka-consumer")
  val topic1 = "topic1"

  val producer = SendProducer(ProducerSettings(kafkaConfig, new StringSerializer, new StringSerializer))


  def sendOrder(order: AppDirectSubscriptionOrderEvent): Future[RecordMetadata] = {
    val message = JsonFormat.toJsonString(order)
    logger.info(s"in: $message")
    val result: Future[RecordMetadata] = producer.send(new ProducerRecord(topic1, "key", message))
    result
  }


  def send(message: String): Future[RecordMetadata] = {
    logger.info(s"in: $message")
    val result: Future[RecordMetadata] = producer.send(new ProducerRecord(topic1, "key", message))
    result
  }

}