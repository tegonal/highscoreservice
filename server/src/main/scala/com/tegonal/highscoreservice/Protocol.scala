package com.tegonal.highscoreservice

import spray.json.DefaultJsonProtocol
import spray.json._
import org.joda.time._
import org.joda.time.format._

case class Score(user: String, timestamp: Option[DateTime], value: BigDecimal)

trait Protocol extends DefaultJsonProtocol {
  /*
   * joda datetime format
   */
  implicit val dateTimeFormat = new JsonFormat[DateTime] {

    val formatter = ISODateTimeFormat.basicDateTimeNoMillis

    def write(obj: DateTime): JsValue = {
      JsString(formatter.print(obj))
    }

    def read(json: JsValue): DateTime = json match {
      case JsString(s) => try {
        formatter.parseDateTime(s)
      } catch {
        case t: Throwable => error(s)
      }
      case _ =>
        error(json.toString())
    }

    def error(v: Any): DateTime = {
      val example = formatter.print(0)
      deserializationError(f"'$v' is not a valid date value. Dates must be in compact ISO-8601 format, e.g. '$example'")
    }
  }

  implicit val scoreFormat = jsonFormat3(Score.apply)
}
