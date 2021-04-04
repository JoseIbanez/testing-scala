package poc


import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

import scala.concurrent.ExecutionContextExecutor

// (un)marshalling with play
// https://blog.knoldus.com/marshalling-unmarshalling-in-akka-http/
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import play.api.libs.json.{Json, OWrites, Reads}


import scala.io.StdIn

case class Address(street: String, city: String, country: String)
case class User(userId: String, group: String, name:String, address: Address)

object User {
  implicit val toJson: OWrites[User] = Json.writes[User]
  implicit val fromJson: Reads[User] = Json.reads[User]
}

object Address {
  implicit val toJson: OWrites[Address] = Json.writes[Address]
  implicit val fromJson: Reads[Address] = Json.reads[Address]
}



object AkkaHttpServerTest extends  App {

  HttpServerRoutingMinimal.main()

}




object HttpServerRoutingMinimal extends PlayJsonSupport {

  def main(): Unit = {

    implicit val system = ActorSystem(Behaviors.empty, "my-system")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.executionContext

    val route =
      path("hello") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
        }
      } ~
      path( "group" / Segment / Segment ) { (group,userId) =>
        get {
          complete(getUser(group,userId))
        }
      } ~
      path( "group" ) {
        post {
          entity(as[User]) { user =>
            complete(addUser(user))
          }
        }
      }




    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

  }


  /////
  def addUser(user:User): User = {
    User("user0001",user.group,user.name,Address("Av. America", "Madrid", "Spain"))
  }

  /////
  def getUser(groupId:String, userId:String): User = {
    User(userId,groupId,"myUser",Address("Av. America", "Madrid", "Spain"))
  }

}


