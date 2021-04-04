package poc

import play.api.libs.json.{Json, OWrites, Reads}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest, HttpResponse, StatusCodes, headers}
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.typesafe.scalalogging.LazyLogging
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}


case class RegistrationConfirmation(uri:String)

object RegistrationConfirmation {
  implicit val toJson: OWrites[RegistrationConfirmation] = Json.writes[RegistrationConfirmation]
  implicit val fromJson: Reads[RegistrationConfirmation] = Json.reads[RegistrationConfirmation]
}



object MailClientTest extends App with PlayJsonSupport with LazyLogging {

  implicit val system: ActorSystem = ActorSystem("EventProcessor")
  implicit val ec: ExecutionContextExecutor = ExecutionContext.global



  val person1: Person = Person("middleware@vodafone.com",Some("Unity Middleware"))
  val person2: Person = Person("other@vodafone.com",None)
  val header1: KeyValuePairObject = KeyValuePairObject("x-Request-ID","UUID")
  val content1: Content = Content("Hi there","text/plain")

  val m1: Message = Message(
    from = person1,
    to = List(person2),
    subject = Some("Test mail"),
    header = Some(List(header1)),
    content = List(content1),
    cc = None,
    bcc=None,
    attachment = None
  )

  print(Json.toJson(m1))

  //println(m1.toJson)


  def requestSendMail(id:String, mailMessage:Message): Future[Option[RegistrationConfirmation]] = {

    val cleanId = id.replace(":", "_")
    //Headers
    val uri: String = s"https://jsonstorage.net/api/items/$cleanId"
    val authenticationKey: String = "6d9298aa-e883-4e82-8c94-65c022dbe3ac"
    val auth = RawHeader("authorization", s"token $authenticationKey")

    // Body
    val body = HttpEntity(ContentTypes.`application/json`, Json.toJson(mailMessage).as[String])

    // Send request
    logger.info(s"AccountId:$id, Registering: $uri")

    Http()
      .singleRequest(
        HttpRequest(method = HttpMethods.POST, uri = uri, entity = body)
        .withHeaders(auth)
      )
      .flatMap(res => mailResponse(id,res))
      .map(Option(_))

  }

  def mailResponse(id: String, response: HttpResponse): Future[RegistrationConfirmation] = {

    logger.info(s"AccountId:$id, Register Response, Status:${response.status} ${Unmarshal(response).to[String]}")

    response.status match {
      case StatusCodes.Created  =>
        val registrationConfirmation = (Unmarshal(response).to[RegistrationConfirmation])
        logger.info(s"AccountId:$id, Registration confirmation: $registrationConfirmation")
        registrationConfirmation
      case code =>
        logger.error(s"AccountId:$id, Register Error: ${response.status}: ${Unmarshal(response).to[String]}")
        Future.failed(new RuntimeException(s"Account:$id, Registration failed: $response"))
    }

  }



}
