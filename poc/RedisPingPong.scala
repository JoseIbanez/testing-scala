package poc

import redis.RedisClient
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {
  implicit val akkaSystem = akka.actor.ActorSystem()
  //implicit val system: ActorSystem = ActorSystem("QuickStart")


  val redis = RedisClient()

  val futurePong = redis.ping()
  println("Ping sent!")
  futurePong.map(pong => {
    println(s"Redis replied with a $pong")
  })
  Await.result(futurePong, 5000.millis)

  redis.set("dumbKey", "hi")
  val r = redis.get[String]("dumbKey")
  val v = Await.result(r, 5000.millis)
  println(v)

  akkaSystem.terminate()
}

