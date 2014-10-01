package controllers

import uav._
import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.mvc.WebSocket.FrameFormatter

import play.api.libs.functional.syntax._
import play.api.libs.json._


object Application extends Controller {
  def use[A <: Plugin](implicit app: Application, m: Manifest[A]) = {
    app.plugin[A].getOrElse(throw new RuntimeException(m.runtimeClass.toString + " plugin should be available at this point"))
  }



  implicit val dataWrites: Writes[UavConnection.Data] = (
    (__ \ "roll").write[Double] and
    (__ \ "pitch").write[Double] and
    (__ \ "heading").write[Double] and
    (__ \ "altitude").write[Double] and
    (__ \ "temperature").write[Double])(unlift(UavConnection.Data.unapply))

  implicit val dataReads: Reads[UavConnection.Data] = (
    (__ \ "roll").read[Double] and
    (__ \ "pitch").read[Double] and
    (__ \ "heading").read[Double] and
    (__ \ "altitude").read[Double] and
    (__ \ "temperature").read[Double])(UavConnection.Data.apply _)

  implicit val dataFormat = Format(dataReads, dataWrites)


  //implicit val inEventFormat = Json.format[uav.UavConnection.Data]
  //implicit val outEventFormat = Json.format[uav.Data]
  


  def index() = Action {
    Ok(views.html.index())
  }

  implicit val dataFrameFormatter = FrameFormatter.jsonFrame[UavConnection.Data]


  def socket = WebSocket.acceptWithActor[String, UavConnection.Data] { request =>
    out => use[UavPlugin].register(out)
  }
}