package com.iba.mongo

import java.util.UUID

import org.mongodb.scala._
import com.typesafe.scalalogging.LazyLogging
import org.mongodb.scala.result.InsertOneResult
import com.mongodb.MongoCredential._

import collection.JavaConverters._
import scala.concurrent.Future

object MongoRepository extends LazyLogging {


  val user: String = "user1"                       // the user name
  val source: String = "test_db"                 // the source where the user is defined
  val password: Array[Char] = "user1".toCharArray  // the password as a character array
  val credential: MongoCredential = createCredential(user, source, password)


  val mongoSettings: MongoClientSettings = MongoClientSettings.builder()
      .applyToClusterSettings(b => b.hosts(List(new ServerAddress("localhost:27017")).asJava))
      .credential(credential)
      .build()


  //val mongoClient: MongoClient = MongoClient("mongodb://user1:user1@localhost:27017/?authSource=test_db")
  val mongoClient: MongoClient = MongoClient(mongoSettings)
  val database: MongoDatabase = mongoClient.getDatabase("test_db")
  val collection: MongoCollection[Document] =database.getCollection("col1")


  def save(user: String): Future[result.InsertOneResult] = {
    val doc: Document = Document("_id" -> UUID.randomUUID.toString, "name" -> "MongoDB", "type" -> "database", "count" -> 1, "info" -> Document("x" -> 203, "y" -> 102))
    val result:  SingleObservable[InsertOneResult] =  collection.insertOne(doc)

    /*
    result
      .subscribe(new Observer[InsertOneResult] {
        override def onNext(result: InsertOneResult): Unit = println(s"onNext: $result")
        override def onError(e: Throwable): Unit = println(s"onError: $e")
        override def onComplete(): Unit = println("onComplete")
      })
    */

    logger.info(doc.toString())
    result.toFuture()
  }

}
