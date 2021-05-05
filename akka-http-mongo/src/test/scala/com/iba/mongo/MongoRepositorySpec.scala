package com.iba.mongo

import java.util.UUID

import org.scalatest.concurrent._

import scala.concurrent.ExecutionContext.Implicits.global
import org.scalatest.wordspec.{AnyWordSpec, AnyWordSpecLike}
import org.slf4j.LoggerFactory


class MongoRepositorySpec extends AnyWordSpec with ScalaFutures {

  //implicit val ec: ExecutionContextExecutor = ExecutionContext.global

  val logger = LoggerFactory.getLogger(getClass.getSimpleName)

  "MongoRepository" must {


    "save an entry" in {

      val userId = UUID.randomUUID.toString

      val user1 = User(id=userId,name = "Name1", age=33, countryOfResidence = "Spain", address = Address("Av. America","Madrid"))
      val result = MongoRepository.saveUser(user1)

      whenReady(result) { r =>
        println(r.getInsertedId)
        assert(r.wasAcknowledged)
      }

    }

    "save and read " in {
      val userId = UUID.randomUUID.toString
      val user2 = User(id=userId,name = "Name2", age=33, countryOfResidence = "Spain", address = Address("Av. America","Madrid"))
      val result = MongoRepository.saveUser(user2)

      val user2R = result.map( r => {
        logger.info(s"Mongo Write, InsertedId: ${r.getInsertedId.toString}")
        MongoRepository.readUser(userId)
      }).flatMap(user => user)

      whenReady(result) { r =>
        println(s"Mongo Write, InsertId: ${r.getInsertedId}")
        assert(r.wasAcknowledged)
      }

      whenReady(user2R) { user =>
        println(s"Mongo Read, ${user}")
        assert(user.id == userId)
      }

    }

  }


}