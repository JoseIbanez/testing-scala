package poc

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.typesafe.scalalogging.LazyLogging
import spray.json.DefaultJsonProtocol.{jsonFormat2, _}
import spray.json.{RootJsonFormat, _}

import scala.concurrent.{ExecutionContextExecutor, Future}





object HttpRegister extends LazyLogging {

  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "SingleRequest")
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  case class RegistrationConfirmation(uri:String)
  case class OrderConfirmation(title:String, url:String)
  case class CustomerAccount(id:String, data1:String)
  implicit val orderFormat: RootJsonFormat[OrderConfirmation] = jsonFormat2(OrderConfirmation)
  implicit val customerAccountFormat: RootJsonFormat[CustomerAccount] = jsonFormat2(CustomerAccount)
  implicit val registrationConfirmationFormat: RootJsonFormat[RegistrationConfirmation] = jsonFormat1(RegistrationConfirmation)



  def registerAccount(id:String, account:CustomerAccount): Future[Option[RegistrationConfirmation]] = {

    val request = accountRequest(id,account)
    Http()
      .singleRequest(request)
      .flatMap(res => accountResponse(id,res))

  }



  def accountRequest(id:String, account:CustomerAccount): HttpRequest = {

    val cleanId = id.replace(":", "_")

    //Headers
    val uri: String = s"https://jsonstorage.net/api/items/$cleanId"
    val authenticationKey: String = "6d9298aa-e883-4e82-8c94-65c022dbe3ac"
    val auth = RawHeader("authorization", s"token $authenticationKey")

    // Body
    val body = HttpEntity(ContentTypes.`application/json`, account.toJson.compactPrint)

    // Send request
    logger.info(s"AccountId:$id, Registering: $uri")
    HttpRequest(method = HttpMethods.POST, uri = uri, entity = body).withHeaders(auth)

  }


  def accountResponse(id: String, response: HttpResponse): Future[Option[RegistrationConfirmation]] = {

    logger.info(s"AccountId:$id, Register Response, Status:${response.status} ${Unmarshal(response).to[String]}")

    response.status match {
      case StatusCodes.Created  =>
        val registrationConfirmation = Unmarshal(response).to[Option[RegistrationConfirmation]]
        logger.info(s"AccountId:$id, Registration confirmation: $registrationConfirmation")
        registrationConfirmation
      case code =>
        logger.error(s"AccountId:$id, Register Error: ${response.status}: ${Unmarshal(response).to[String]}")
        Future.failed(new RuntimeException(s"Account:$id, Registration failed: $response"))
    }

  }




}
