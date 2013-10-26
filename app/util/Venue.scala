package trending.util

import scala.concurrent._
import ExecutionContext.Implicits.global

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Reads._

import play.api.libs.ws._

case class Venue(id: String, name: String, address: String, city: String, lat: Double, lng: Double, hereNow: Int)

object Venue {
  
  val client_id = "X"
  val client_secret = "X"
  
  implicit def convertVenue: Reads[Seq[Venue]] =
    (__ \ "response" \ "venues").read(
      seq(
        (__ \ "id").read[String] and
        (__ \ "name").read[String] and
        (__ \ "location").read(
          (__ \ "address").read[String] and
          (__ \ "city").read[String] and
          (__ \ "lat").read[Double] and
          (__ \ "lng").read[Double] tupled
        ) and
        (__ \ "hereNow").read(
          (__ \ "count").read[Int]
        )
        tupled
      )
    ).map(
      _.collect {
        case (id, name, (address, city, lat, lng), hereNow) => Venue(id, name, address, city, lat, lng, hereNow)
      }
    )
  
  def trending(lat: String, lng: String): Future[Seq[Venue]] = {
    WS.url("https://api.foursquare.com/v2/venues/trending").withQueryString(
      "ll" -> (lat + "," + lng),
      "limit" -> "50",
      "radius" -> "2000",
      "client_id" -> client_id,
      "client_secret" -> client_secret,
      "v" -> "20131025"
    ).get().map(
      r => r.status match {
        case 200 => r.json.asOpt[Seq[Venue]].getOrElse(Nil)
        case e => sys.error(s"Bad response. Status $e") // Usually 500 error status: rate limit exceeded
        // TODO implement authentication to get 500 requests per hour per user and fallback to foursquare application when these 500 r/h are exceeded
        // Foursquare apps have 5000 requests per hour.
      }
    )
  }
}