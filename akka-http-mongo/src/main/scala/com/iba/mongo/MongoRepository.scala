package com.iba.mongo

//import java.util.UUID

import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters._
import scala.concurrent.Future
//import scala.concurrent.JavaConversions
import scala.concurrent.ExecutionContext.Implicits.global
import org.mongodb.scala._
//import org.mongodb.scala.result.InsertOneResult
import com.mongodb.MongoCredential._
import org.mongodb.scala.model.Filters._
import org.bson.codecs.{DecoderContext, EncoderContext}
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.bson.{BsonDocumentReader, BsonDocumentWrapper, BsonDocumentWriter, BsonWriter}
import org.mongodb.scala.bson.codecs.Macros

//import scala.reflect.classTag
import org.mongodb.scala.bson.collection.mutable.Document
import MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.BsonDocument
import org.bson.codecs.Codec




/*
* ref:
* https://stackoverflow.com/questions/33967099/serialize-to-object-using-scala-mongo-driver
* https://stackoverflow.com/questions/33643484/how-do-i-turn-a-scala-case-class-into-a-mongo-document
*/

object MongoRepository {

  val logger = LoggerFactory.getLogger(getClass.getSimpleName)

  val user: String = "user1"                       // the user name
  val source: String = "admin"                 // the source where the user is defined
  val password: Array[Char] = "user1".toCharArray  // the password as a character array
  val credential: MongoCredential = createCredential(user, source, password)

  val addressCodecProvider = Macros.createCodecProvider[Address]()
  val personCodecProvider = Macros.createCodecProvider[User]()
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(personCodecProvider,addressCodecProvider), DEFAULT_CODEC_REGISTRY)
  val decoderContext = DecoderContext.builder.build
  val encoderContet = EncoderContext.builder.isEncodingCollectibleDocument(true).build()
  //val codec = codecRegistry.get(classTag[User].runtimeClass)
  val codec = Macros.createCodec[User](codecRegistry)



  val mongoSettings: MongoClientSettings = MongoClientSettings.builder()
      .applyToClusterSettings(b => b.hosts(List(new ServerAddress("localhost:27017")).asJava))
      .credential(credential)
      .build()

  val mongoClient: MongoClient = MongoClient(mongoSettings)
  val database: MongoDatabase = mongoClient.getDatabase("test_db")
  val collection: MongoCollection[Document] =database.getCollection("col1")



  def saveUser(user: User): Future[result.InsertOneResult] = {
    val bsonWrite = new BsonDocumentWriter(BsonDocument())
    codec.encode(bsonWrite, user, encoderContet)
    val doc = Document(bsonWrite.getDocument)

    logger.info(s"Mongo Write, document: ${doc.toJson}")
    val result =  collection.insertOne(doc)
    result.toFuture()
  }


  def readUser(id: String): Future[User] = {
    val result = collection.find(equal("id", id)).first()

    result.toFuture.map(document => {
      logger.info(s"Mongo Read, document: ${document}")
      val bsonDocument = BsonDocumentWrapper.asBsonDocument(document,DEFAULT_CODEC_REGISTRY)
      val bsonReader = new BsonDocumentReader(bsonDocument)
      val user: User = codec.decode(bsonReader, decoderContext).asInstanceOf[User]
      logger.info(s"Mongo Read, user: $user")
      user
    })

  }




}
