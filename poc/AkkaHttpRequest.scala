package poc

import scala.concurrent.{ExecutionContext, Future}

object AkkaHttpRequest extends  App {

  implicit val ec = ExecutionContext.global

  val restHost = HttpRegister
  val value1 = restHost.CustomerAccount("0001","Hi there")
  val value2 = restHost.CustomerAccount("0002","Hi there")
  val value3 = restHost.CustomerAccount("0003","Hi there")

  restHost.registerAccount(value1.id, value1)
  restHost.registerAccount(value2.id, value2)
  restHost.registerAccount(value3.id, value3)



}
