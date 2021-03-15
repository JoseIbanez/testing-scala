import scala.concurrent.ExecutionContext.Implicits.global
import scala.async.Async.{async, await}
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}


def getUrl(x:Int): Future[String] = async {
  val url = "http://api.hostip.info/get_json.php?ip=12.215.42.19"
  scala.io.Source.fromURL(url).mkString
}

val result = Await.result(getUrl(1), 2.seconds)

val list1 = List.range(1,10)
val list2: List[Future[String]] = list1.mapAsync(getUrl)


val futset: Future[List[String]] = Future.sequence(list2)
println (Await.result(futset, 30 seconds))


def slowCalcFuture: Future[Int] =  async { 2 + 3 }

def combined: Future[Int] = async {
  await(slowCalcFuture) + await(slowCalcFuture)
}

val y = Await.result(showCalcF2, 10.second)
val x: Int = Await.result(combined, 10.seconds)



