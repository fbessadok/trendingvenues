package trending.util

import scala.concurrent._
import ExecutionContext.Implicits.global

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Reads._

import play.api.libs.ws._

object Location {
  
  implicit def readLocation: Reads[(Double, Double)] =
    (__ \ "latitude").read[Double] and
    (__ \ "longitude").read[Double] tupled

  def getCoordinates(ip: String): Future[(Double, Double)] = {
    val default = (37.789404, -122.401042)
    WS.url("http://freegeoip.net/json/" + ip)
      .get()
      .map(
        r => r.status match {
          case 200 => r.json.asOpt[(Double, Double)].getOrElse(default)
          case e => default
        }
      )
  }
}