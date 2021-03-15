package fr.ps.eng.kafka.app4s

import java.security.MessageDigest
import java.util.Base64

//import sun.misc.BASE64Encoder
import java.util.Base64


/**
 * Created by loicmdivad.
 */
package object common {

  val Base64Encoder: Base64.Encoder =  Base64.getEncoder
  val SHA1Digest: MessageDigest = java.security.MessageDigest.getInstance("SHA-1")

  def sha1(name: String): String = Base64Encoder.encodeToString(SHA1Digest.digest(name.getBytes))
}
