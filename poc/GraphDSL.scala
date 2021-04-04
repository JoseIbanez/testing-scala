package poc

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ClosedShape
import akka.stream.scaladsl.{Flow, GraphDSL, RunnableGraph, Sink, Source}
//import akka.stream.scaladsl.batch

object GraphDSLTest extends App {

  implicit val system: ActorSystem = ActorSystem("QuickStart")

  val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
    import akka.stream.scaladsl.GraphDSL.Implicits._
    val in = Source(1 to 100)
    val out = Sink.foreach(println)


    val saveEvent = Flow[Int].map(f1)
    val checkAll = Flow[String].map(f2)
    //val httpRegister = Flow[String].buffer(20,OverflowStrategy.backpressure)
    //  .map { x => f3(x)}.async
    val httpRegister = Flow[String].map(f3).async

    val saveRegister = Flow[String].map(f3)

    in ~> saveEvent ~> checkAll ~> httpRegister ~> out
    ClosedShape
  })

  g.run()

  def f1(x:Int): String = {
    println(s"f1 $x")
    s"$x"
  }

  def f2(x:String): String ={
    println(s"f2 $x")
    s"aa$x"
  }

  def f3(x:String): String = {
    println(s"f3-in")

    Thread.sleep(1000)
    println("f3-out")
    s"${x}zz"
  }

}
