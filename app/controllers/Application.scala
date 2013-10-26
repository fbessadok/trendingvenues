package controllers

import scala.concurrent._
import ExecutionContext.Implicits.global

import play.api._
import play.api.mvc._
import play.api.libs.ws._

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

import java.util.Calendar
import java.text.SimpleDateFormat

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }
  
  def about = Action {
    Ok(views.html.about())
  }
  
  def post = Action { request =>
    val lat = request.body.asFormUrlEncoded.get("lat")(0).toDouble
    val lng = request.body.asFormUrlEncoded.get("lng")(0).toDouble
    val oauth_token = request.body.asFormUrlEncoded.get("oauth_token")(0)
    Async {
      Venue.searchVenue(lat, lng, oauth_token).map {
        case v => Ok(Json.toJson(v))
      }
    }
  }
  
  implicit val writeVenueAsJson = Json.writes[Venue]

  case class Venue(id: String, name: String, address: String, city: String, lat: Double, lng: Double, hereNow: Int)
  
  object Venue {
      
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
