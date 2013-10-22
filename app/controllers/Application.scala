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
  
  def trending(lat: Double, lng: Double, oauth_token: String) = Action {
    Async {
      Venue.fetchPage(lat, lng, oauth_token).map { case json => Ok(json) } 
    }
  }

  case class Venue(id: String, name: String, lat: Double, lng: Double)
  
  object Venue {
    def search(lat: Double, lng: Double): Enumerator[Venue] = {
      ???
    }
    
    def fetchPage(lat: Double, lng: Double, oauth_token: String): Future[String] = {
      WS.url("https://api.foursquare.com/v2/venues/trending").withQueryString(
        "ll" -> (lat.toString + "," + lng.toString),
        "oauth_token" -> oauth_token,
        "v" -> "20131022"
      ).get().map(
        r => r.status match {
          case 200 => r.body
          case e => "Bad response"
        }
      )
    }
  }
  
}