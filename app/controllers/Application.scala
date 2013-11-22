package controllers

import scala.concurrent._
import ExecutionContext.Implicits.global

import play.api._
import play.api.mvc._
import play.api.libs.json._

import trending.util._

object Application extends Controller {

  def index = Action.async { request =>
    Location.getCoordinates(request.remoteAddress).map {
      case (lat: Double, lng: Double) => Ok(views.html.index(lat, lng))
    }
  }
  
  def about = Action {
    Ok(views.html.about())
  }
  
  implicit val writeVenueAsJson = Json.writes[Venue]
  
  def post = Action.async { request =>
    val lat = request.body.asFormUrlEncoded.get("lat")(0)
    val lng = request.body.asFormUrlEncoded.get("lng")(0)

    Venue.trending(lat, lng).map {
      case v => Ok(Json.toJson(v))
    }
  }
}
