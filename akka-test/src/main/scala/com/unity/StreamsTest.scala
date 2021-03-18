package com.unity

import akka.stream._
import akka.stream.scaladsl._

import akka.{ Done, NotUsed }
import akka.actor.ActorSystem
import akka.util.ByteString
import scala.concurrent._
import scala.concurrent.duration._
import java.nio.file.Paths


object StreamsTest extends App {

  implicit val system: ActorSystem = ActorSystem("QuickStart")
  //implicit val ec: ExecutionContext = system.executionContext

  //val resultSink = Sink.head[Int]
  val resultSink = Sink.foreach(println)

  def myAction(x:Int):Int = {
    val y = x+1
    Thread.sleep(100)
    println(s"y:${y}")
    if ( y > 3)
      3
    else
      y
  }

  val g = RunnableGraph.fromGraph(GraphDSL.create(resultSink) { implicit builder => sink =>
    import GraphDSL.Implicits._
    val in = Source(1 to 20)
    //val out = Sink.ignore

    //val bcast = builder.add(Broadcast[Int](2))
    //val merge = builder.add(Merge[Int](2))

    //val f1, f2, f3, f4 = Flow[Int].map( (x) => { println(x); x+1} )
    //val f1 = Flow[Int].map( (x) => { println(s"x:${x+1}"); x+1 } )
    val f1 = Flow[Int].map(myAction)

    //in ~> f1 ~> bcast ~> f2 ~> merge ~> f3 ~> sink
    //bcast ~> f4 ~> merge

    in ~> f1 ~> sink
    ClosedShape
  })


  //val max: Future[Int] = g.run()
  val max: Future[Done] = g.run()
  val result = Await.result(max, 5000.millis)

  println(s"Result: ${result}")

  Thread.sleep(1000)
  system.terminate()

}
