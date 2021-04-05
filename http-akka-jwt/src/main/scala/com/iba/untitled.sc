
import java.time.Clock
import pdi.jwt.{Jwt, JwtAlgorithm, JwtHeader, JwtClaim, JwtOptions}
implicit val clock: Clock = Clock.systemUTC

val token = Jwt.encode("""{"user":1}""", "secretKey", JwtAlgorithm.HS256)
