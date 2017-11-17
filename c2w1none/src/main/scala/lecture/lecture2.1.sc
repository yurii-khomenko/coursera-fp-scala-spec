sealed trait JSON
case class JObj(bindings: Map[String, JSON]) extends JSON
case class JSeq(elem: List[JSON]) extends JSON
case class JStr(str: String) extends JSON
case class JNum(num: Double) extends JSON
case class JBool(b: Boolean) extends JSON
case object JNull extends JSON

def show(json: JSON): String = json match {
  case JObj(bindings) =>
    val assocs = bindings map {
      case (key, value) => '\"' + key + "\": " + show(value)
    }
    '{' + (assocs mkString ", ") + '}'

  case JSeq(elems) =>
    '[' + (elems map show mkString ", ") + ']'

  case JStr(str) => '\'' + str + '\''
  case JNum(num) => num.toString
  case JBool(b) => b.toString
  case JNull => "null"
}

val data = JObj(Map(
  "firstName" -> JStr("John"),
  "lastName" -> JStr("Smith"),
  "address" -> JObj(Map(
    "streetAddress" -> JStr("21 2nd Street"),
    "state" -> JStr("NY"),
    "postalCode" -> JNum(10021)
  )),
  "phoneNumbers" -> JSeq(List(
    JObj(Map(
      "type" -> JStr("home"), "number" -> JStr("212 555-1234")
    )),
    JObj(Map(
      "type" -> JStr("fax"), "number" -> JStr("646 555-4567")
    ))
  )),
  "bool" -> JBool(true),
  "null" -> JNull,
))

show(data)