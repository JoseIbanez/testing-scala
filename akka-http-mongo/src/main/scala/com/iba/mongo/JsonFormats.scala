package com.iba.mongo

import com.iba.mongo.UserRegistry.ActionPerformed

//#json-formats
import spray.json.DefaultJsonProtocol

object JsonFormats  {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val addressJsonFormat = jsonFormat2(Address)
  implicit val userJsonFormat = jsonFormat5(User)
  implicit val usersJsonFormat = jsonFormat1(Users)

  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)
}
//#json-formats
