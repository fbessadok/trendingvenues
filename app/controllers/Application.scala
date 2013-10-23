package controllers

import scala.concurrent._
import ExecutionContext.Implicits.global

import play.api.libs.iteratee._

import play.api._
import play.api.mvc._
import play.api.libs._
import play.api.libs.ws._

import play.api.libs.json._
import play.api.libs.functional._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  
  implicit val writeVenueAsJson = Json.writes[Venue]

  def trending(lat: Double, lng: Double, oauth_token: String) = Action {
    Async {
      Venue.searchVenue(lat, lng, oauth_token).map {
        case v => Ok(Json.toJson(v))
      }
    }
  }

  case class Venue(id: String, name: String, lat: Double, lng: Double, hereNow: Int)
  
  object Venue {
      
    implicit def convertVenue: Reads[Seq[Venue]] =
      (__ \ "response" \ "venues").read(
        seq(
          (__ \ "id").read[String] and
          (__ \ "name").read[String] and
          (__ \ "location").read(
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
          case (id, name, (lat, lng), hereNow) => Venue(id, name, lat, lng, hereNow)
        }
      )

    def searchVenue(lat: Double, lng: Double, oauth_token: String): Future[Seq[Venue]] = {
      WS.url("https://api.foursquare.com/v2/venues/trending").withQueryString(
        "ll" -> (lat.toString + "," + lng.toString),
        "limit" -> "50",
        "radius" -> "2000",
        "oauth_token" -> oauth_token,
        "v" -> "20131024"
      ).get().map(
        r => r.status match {
          case 200 => r.json.asOpt[Seq[Venue]].getOrElse(Nil)
          case e => sys.error(s"Bad response. Status $e")
        }
      )
    }
  }
}
