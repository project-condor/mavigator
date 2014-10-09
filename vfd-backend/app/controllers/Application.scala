package controllers

import util._
import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.mvc.WebSocket.FrameFormatter

import play.api.libs.json._

import vfd.uav.DataFrame
import plugins.UavPlugin


object Application extends Controller {

  implicit val dataFrameFormat = spicklerFormat[DataFrame]
  implicit val dataFrameFormatter = FrameFormatter.jsonFrame[DataFrame]

  def use[A <: Plugin](implicit app: Application, m: Manifest[A]) = {
    app.plugin[A].getOrElse(throw new RuntimeException(m.runtimeClass.toString + " plugin should be available at this point"))
  }

  def index = Action { implicit request =>
    Ok(views.html.index(routes.Application.socket.webSocketURL()))
  }


  def socket = WebSocket.acceptWithActor[String, DataFrame] { implicit request =>
    out => use[UavPlugin].register(out)
  }
}