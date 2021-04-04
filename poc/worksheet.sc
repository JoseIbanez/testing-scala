

import org.json4s.native.JsonMethods._
import org.json4s._

implicit val formats = DefaultFormats

val str = """[{"Id":14706061,
             "Rcvr":1,
              "HasSig":true,
              "Sig":80},
           {"Id":3425490,
            "Rcvr":1,
            "HasSig":false,
            "Sig": 80}]"""


case class Example (Id: BigInt, Rcvr: Int, HasSig: Boolean, Sig: Int)

val json = parse(str)

val examples = json.extract[List[Example]]
