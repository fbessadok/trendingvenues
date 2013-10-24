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

import java.util.Calendar
import java.text.SimpleDateFormat

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }
  
  def post = Action { request =>
    val lat = request.body.asFormUrlEncoded.get("lat")(0)
    val lng = request.body.asFormUrlEncoded.get("lng")(0)
    val oauth_token = request.body.asFormUrlEncoded.get("oauth_token")(0)
    Redirect(routes.Application.trending(lat.toDouble, lng.toDouble, oauth_token))
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
      val today = Calendar.getInstance().getTime()
      val datePattern = new SimpleDateFormat("yyyyMMdd")
      val todayString = datePattern.format(today)
      
      WS.url("https://api.foursquare.com/v2/venues/trending").withQueryString(
        "ll" -> (lat.toString + "," + lng.toString),
        "limit" -> "50",
        "radius" -> "2000",
        "oauth_token" -> oauth_token,
        "v" -> todayString
      ).get().map(
        r => r.status match {
          case 200 => r.json.asOpt[Seq[Venue]].getOrElse(Nil)
          case e => sys.error(s"Bad response. Status $e")
        }
      )
    }
  }
}
