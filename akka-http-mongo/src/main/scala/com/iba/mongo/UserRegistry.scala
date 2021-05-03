package com.iba.mongo

//#user-registry-actor
import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.typesafe.scalalogging.LazyLogging
import org.mongodb.scala.{SingleObservable, result}

import scala.collection.immutable
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}


//#user-case-classes
final case class Address(street: String, city: String)
final case class User(id: String, name: String, age: Int, countryOfResidence: String, address: Address)
final case class Users(users: immutable.Seq[User])
//#user-case-classes

object UserRegistry extends LazyLogging {
  // actor protocol
  sealed trait Command
  final case class SaveUser(replyTo: ActorRef[result.InsertOneResult]) extends Command
  final case class GetUsers(replyTo: ActorRef[Users]) extends Command
  final case class CreateUser(user: User, replyTo: ActorRef[ActionPerformed]) extends Command
  final case class GetUser(name: String, replyTo: ActorRef[GetUserResponse]) extends Command
  final case class DeleteUser(name: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetUserResponse(maybeUser: Option[User])
  final case class ActionPerformed(description: String)

  def apply(): Behavior[Command] = registry(Set.empty)

  implicit val ec: ExecutionContextExecutor = ExecutionContext.global

  private def registry(users: Set[User]): Behavior[Command] =
    Behaviors.receiveMessage {
      case SaveUser(replyTo) =>
        val localReplyTo = replyTo
        MongoRepository.save(users.head)
          .map { saveResult =>
            logger.info(saveResult.toString)
            localReplyTo ! saveResult
          }
        //replyTo ! mongoResult
        Behaviors.same
      case GetUsers(replyTo) =>
        logger.info(users.toString)
        replyTo ! Users(users.toSeq)
        Behaviors.same
      case CreateUser(user, replyTo) =>
        MongoRepository.saveUser(user)
          .onComplete{
            case Success(result) => replyTo ! ActionPerformed(s"User ${user.id} created.")
            case Failure(exception) => replyTo ! ActionPerformed(s"User ${user.id} failed. Error:${exception.getMessage}")
          }
        //replyTo ! ActionPerformed(s"User ${user.name} created.")
        //registry(users + user)
        Behaviors.same
      case GetUser(name, replyTo) =>
        MongoRepository.readUser(name)
          .onComplete {
            case Success(user) => replyTo ! GetUserResponse(Some(user))
            case Failure(exception) => replyTo ! GetUserResponse(None)
          }
        Behaviors.same
      case DeleteUser(name, replyTo) =>
        replyTo ! ActionPerformed(s"User $name deleted.")
        registry(users.filterNot(_.name == name))
    }
}
//#user-registry-actor
