package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.mvc.WebSocket.FrameFormatter

import play.api.libs.json._

import vfd.uav.DataFrame
import plugins.UavPlugin


object Application extends Controller {

  def use[A <: Plugin](implicit app: Application, m: Manifest[A]) = {
    app.plugin[A].getOrElse(throw new RuntimeException(m.runtimeClass.toString + " plugin should be available at this point"))
  }

  def index() = Action {
    Ok(views.html.index())
  }

  implicit object DataFrameFormat extends Format[DataFrame] {
    import org.scalajs.spickling._
    import org.scalajs.spickling.playjson._
    import play.api.data.validation.ValidationError

    PicklerRegistry.register[DataFrame]

    def writes(o: DataFrame): JsValue = PicklerRegistry.pickle(o)

    def reads(j: JsValue): JsResult[DataFrame] = PicklerRegistry.unpickle(j) match {
      case df: DataFrame => JsSuccess(df)
      case _ => JsError("unpickling yielded wrong type")
    }
  }

  //implicit val dataFrameFormat = Json.format[DataFrame]
  implicit val dataFrameFormatter = FrameFormatter.jsonFrame[DataFrame]

  def socket = WebSocket.acceptWithActor[String, DataFrame] { request =>
    out => use[UavPlugin].register(out)
  }
}