package com.example

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import com.example.GreeterMain.SayHello


object Worker {

  trait Order
  final case class Order1(msg: String, counter: Int) extends Order
  final case class Order2(msg: String) extends Order

  def apply(): Behavior[Order] = exeOrder()

  private def exeOrder(): Behavior[Order] =
    Behaviors.receive { (context, message) => {

      message match {
        case Order1(msg, counter) =>
          context.log.info("order1: BEGIN {}, count {}", msg, counter)
          Thread.sleep(500)
          context.log.info("order1: END")
          Behaviors.same
        case Order2(msg) =>
          context.log.info("order2: BEGIN {}", msg)
          Thread.sleep(1000)
          context.log.info("order2: END")
          Behaviors.same
        }
      }
    }

} //End Worker






//#greeter-main
object EchoMain {

  final case class SayHello(name: String)

  def apply(): Behavior[SayHello] =
    Behaviors.setup { context =>

      context.log.info("New actor 'EchoMain' {}",context.toString)

      //#create-actors
      //val echoActor = context.spawn(Echo(), "echo")
      //#create-actors


      Behaviors.receiveMessage { message =>
        //#create-actors
        val wrk1Actor = context.spawn(Worker(), s"wkr1:${message.name}")
        val wkr2Actor = context.spawn(Worker(), s"wrk2:${message.name}")
        //#create-actors

        wrk1Actor ! Worker.Order1(s"${message.name} 1", 10)
        wkr2Actor ! Worker.Order2(s"${message.name} 2")
        Behaviors.same
      }
    }


}
//#greeter-main




//#main-class
object myApp extends App {
  //#actor-system
  val greeterMain: ActorSystem[EchoMain.SayHello] = ActorSystem(EchoMain(), "EchoQuickStart")
  //#actor-system

  //#main-send-messages
  greeterMain ! EchoMain.SayHello("Jose")
  greeterMain ! EchoMain.SayHello("Juan")
  Thread.sleep(1000)
  greeterMain ! EchoMain.SayHello("2222")
  //#main-send-messages
}
//#main-class
