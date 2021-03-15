package pool

import java.net.URI
import java.sql.Connection
import java.sql.Statement

import org.apache.commons.dbcp2._

case class NumberPool(id:String, begin:String, size: Int)

class PoolManager {

  def apply(id:String): NumberPool = {
    NumberPool(id,"2000",10)
  }

}

