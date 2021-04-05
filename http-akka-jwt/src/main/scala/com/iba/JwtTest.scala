package com.iba



import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.spec.PKCS8EncodedKeySpec
import java.security.{KeyFactory, PublicKey}
import java.time.Clock
import java.util.Base64
import java.security.KeyFactory
import java.security.spec.RSAPublicKeySpec

import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim, JwtHeader, JwtOptions}






object JwtTest extends App {

  val notes = """"
  {"keys":
    [
      { "alg":"RS256",
        "e":"AQAB",
        "kid":"zSZlZdBehxMH9grLql+IXQAlOCrtOiDIjP817fdaakc=",
        "kty":"RSA",
        "n":"zv9UUR21bk9_eaLxrvAcFCLVeKq1ja1wOfq9De0UjHreaTHEchJAFKanqO2xsBY9NrxpNgAC5o0rx_qsm6VI0FeeqsXdYyq-0fUlmBEp-uz8YAzKnvBq-38Y31cyEgyEQIhtrr3u_sCn1HShmBJsGlIvd6B5KvdBhh-AB5UumzNgx2tBo7-dOYMkEbLp1Jl6dIUtqYOag-uOqpJy3jAfcJLYmgRf5NO8hkVj6VnnguG4k2b3l_0ptWaSdC6onhDe_9NybMjGXt97cH-pO-FNloUihg5t80Czc2qnQUt-ggNdX93pgFgFgJexZ2IGVxnBLkZKNzyf0udqrpackkpmQw",
        "use":"sig"},
      {"alg":"RS256",
        "e":"AQAB",
        "kid":"d1koDf8ORJ2Bq5+qeEd4rMkVWFyzDYs2JIWd8OeC3Cw=",
        "kty":"RSA",
        "n":"43EaA0H_vp6LjMc-FmGmitM3GIquRlQOm7iQZr8CxqBxra4BudpTRW24OTEipvsZVmu0Z922oLrrx0d6Wya3dqCb6r1OCbRW0X-1pft0FOUkqD_xjQCHSvXjTJHIXqqxkVDRJcj4mxYTp4-w5Y2hB3ZOC9iD3BV1xEW90_Et5DxbEsgzxGfCVlk46tiqbOYAosR59p0a1LosBVTtO88_qNXEYY1EqNkdjF8fjjJ30hwrPGhhxfYjBBTtmBro7BUT20idXi6Ug7NhumuDtFLvIIvWNqG-BlojOfYA_gQQHjo_lWeVeVPJazru8_gPWMj9lwBoGk4088x5whTUL8DV3w",
        "use":"sig"}
    ]}
  """


  implicit val clock: Clock = Clock.systemUTC

  //val token = Jwt.encode("""{"user":1}""", "secretKey", JwtAlgorithm.HS256)
  //println(token.toString)

  //val res1 = Jwt.decodeRawAll(token, "secretKey", Seq(JwtAlgorithm.HS256))
  //println(res1)



  val key2a = "zv9UUR21bk9_eaLxrvAcFCLVeKq1ja1wOfq9De0UjHreaTHEchJAFKanqO2xsBY9NrxpNgAC5o0rx_qsm6VI0FeeqsXdYyq-0fUlmBEp-uz8YAzKnvBq-38Y31cyEgyEQIhtrr3u_sCn1HShmBJsGlIvd6B5KvdBhh-AB5UumzNgx2tBo7-dOYMkEbLp1Jl6dIUtqYOag-uOqpJy3jAfcJLYmgRf5NO8hkVj6VnnguG4k2b3l_0ptWaSdC6onhDe_9NybMjGXt97cH-pO-FNloUihg5t80Czc2qnQUt-ggNdX93pgFgFgJexZ2IGVxnBLkZKNzyf0udqrpackkpmQw"
  val key2b = "43EaA0H_vp6LjMc-FmGmitM3GIquRlQOm7iQZr8CxqBxra4BudpTRW24OTEipvsZVmu0Z922oLrrx0d6Wya3dqCb6r1OCbRW0X-1pft0FOUkqD_xjQCHSvXjTJHIXqqxkVDRJcj4mxYTp4-w5Y2hB3ZOC9iD3BV1xEW90_Et5DxbEsgzxGfCVlk46tiqbOYAosR59p0a1LosBVTtO88_qNXEYY1EqNkdjF8fjjJ30hwrPGhhxfYjBBTtmBro7BUT20idXi6Ug7NhumuDtFLvIIvWNqG-BlojOfYA_gQQHjo_lWeVeVPJazru8_gPWMj9lwBoGk4088x5whTUL8DV3w"
  val token2 = "eyJraWQiOiJkMWtvRGY4T1JKMkJxNStxZUVkNHJNa1ZXRnl6RFlzMkpJV2Q4T2VDM0N3PSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiIzYTN1OXI2MzJnY2ZrZjZmZnJxcHFmMG45aiIsInRva2VuX3VzZSI6ImFjY2VzcyIsInNjb3BlIjoidHJhbnNhY3Rpb25zXC9wb3N0IiwiYXV0aF90aW1lIjoxNjE3NjQ3OTMyLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAuZXUtY2VudHJhbC0xLmFtYXpvbmF3cy5jb21cL2V1LWNlbnRyYWwtMV9iOW00S0tFNTMiLCJleHAiOjE2MTc2NTE1MzIsImlhdCI6MTYxNzY0NzkzMiwidmVyc2lvbiI6MiwianRpIjoiZWUxYTQwZDctNjM0OC00ZTIyLWE5NDQtYmNiM2UyNjNmMWEyIiwiY2xpZW50X2lkIjoiM2EzdTlyNjMyZ2Nma2Y2ZmZycXBxZjBuOWoifQ.L2Pmq-Bn90tUjJ2gbQ8dM831siLC7-h_8K3nCzS6KbCVzIwUFEH-gX5yJ-BpAqRQVn9mtL0nPyWiTZikzhpAWwts-AlTa4ZNX9VpvFxW6xdjo_6VkD0AbBe01uDmQ9-24sSi5Xi5YWGpqPLV0olMaszL9DbJ7gFyU_27KpWXDMIg8m35EpMXrxNqvJWcASRkWQQjZHfBcbXhXAXwKzXwwc3nckwRv7404kOUfc_tgP2EfQhRUkJgCTRiorNWjZMEa6s9XpQVZLzAKJs-JrIYjZh0iz_VmQaDP_aZxdBU1Smy16x_xIzexXx7muTxG1tAg_WJa1WE4K-7gJRGR7XiFw"

  //val res2 = Jwt.isValid(token2, key2b, Seq(JwtAlgorithm.RS256))
  //println("res2")
  //println(res2)
  //println("---")
  //val publicKeyN = "zv9UUR21bk9_eaLxrvAcFCLVeKq1ja1wOfq9De0UjHreaTHEchJAFKanqO2xsBY9NrxpNgAC5o0rx_qsm6VI0FeeqsXdYyq-0fUlmBEp-uz8YAzKnvBq-38Y31cyEgyEQIhtrr3u_sCn1HShmBJsGlIvd6B5KvdBhh-AB5UumzNgx2tBo7-dOYMkEbLp1Jl6dIUtqYOag-uOqpJy3jAfcJLYmgRf5NO8hkVj6VnnguG4k2b3l_0ptWaSdC6onhDe_9NybMjGXt97cH-pO-FNloUihg5t80Czc2qnQUt-ggNdX93pgFgFgJexZ2IGVxnBLkZKNzyf0udqrpackkpmQw"
  val publicKeyN = key2b
  val publicKeyE = "AQAB"

  val header = "eyJraWQiOiJkMWtvRGY4T1JKMkJxNStxZUVkNHJNa1ZXRnl6RFlzMkpJV2Q4T2VDM0N3PSIsImFsZyI6IlJTMjU2In0"
  println(new String(Base64.getUrlDecoder.decode(header),StandardCharsets.UTF_8))

  val modulus = new BigInteger(1, Base64.getUrlDecoder.decode(publicKeyN))
  val exponent = new BigInteger(1, Base64.getUrlDecoder.decode(publicKeyE))


  val kf         = KeyFactory.getInstance("RSA")
  val pubSpec    = new RSAPublicKeySpec(modulus,exponent)
  val publicKey  = kf.generatePublic(pubSpec)

  val res3 = Jwt.isValid(token = token2, publicKey, Seq(JwtAlgorithm.RS256) )
  println(s"res3: $res3")

  val res4 = Jwt.decode(token2,publicKey,Seq(JwtAlgorithm.RS256))
  println(res4.get.content)


}
