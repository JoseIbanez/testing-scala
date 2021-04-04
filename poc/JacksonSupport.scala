package poc

import akka.http.scaladsl.marshalling.{Marshaller, _}
import akka.http.scaladsl.model.{HttpEntity, HttpRequest}
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.unmarshalling._
import akka.stream.Materializer

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule


object JacksonUtils {
  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  def serialize(doc: AnyRef): String = mapper.writeValueAsString(doc)

  def deserialize[T](json: String)(implicit c: ClassTag[T]): T = mapper.readValue(json, c.runtimeClass).asInstanceOf[T]

}

object JacksonSupport {

  implicit def JacksonMarshaller: ToEntityMarshaller[AnyRef] = {
    Marshaller.withFixedContentType(`application/json`) { obj =>
      HttpEntity(`application/json`, JacksonUtils.serialize(obj).getBytes("UTF-8"))
    }
  }

  implicit def JacksonUnmarshaller[T <: AnyRef](implicit c: ClassTag[T]): FromRequestUnmarshaller[T] = {
    new FromRequestUnmarshaller[T]{
      override def apply(request: HttpRequest)(implicit ec: ExecutionContext, materializer: Materializer): Future[T] = {
        import scala.concurrent.duration._
        import scala.language.postfixOps
        request.entity.toStrict(5 seconds).map(_.data.decodeString("UTF-8")).map { str =>
          JacksonUtils.deserialize[T](str)
        }
      }
    }
  }

}

