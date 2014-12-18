package controllers

import util._
import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.mvc.WebSocket.FrameFormatter

import play.api.libs.json._
import plugins.UavPlugin

object Application extends Controller {
  
  private def uav = current.plugin[UavPlugin].getOrElse(throw new RuntimeException("UAV plugin is not available"))

  def index = Action { implicit request =>
    Redirect(routes.Application.uav(0))
  }
  
  def uav(sysId: Int) = Action { implicit request =>
    Ok(views.html.uav(routes.Application.mavlink.webSocketURL(), sysId))
  }

  def mavlink = WebSocket.acceptWithActor[Array[Byte], Array[Byte]] { implicit request =>
    out => uav.register(out)
  }

}