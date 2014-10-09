package vfd.frontend

import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.spickling._
import org.scalajs.spickling.jsany._

import scalatags.JsDom.all._

import rx._

import vfd.frontend.ui._
import vfd.frontend.util.Application
import vfd.uav.DataFrame

class Frontend(socketUrl: String)(implicit app: Application) {

  PicklerRegistry.register[vfd.uav.DataFrame]
  
  def main() = {
    val connection = new dom.WebSocket(socketUrl);
    val input: Var[DataFrame] = Var(DataFrame(0,0,0,0,0))

    connection.onmessage = (e: dom.MessageEvent) => {
      val json = js.JSON.parse(e.data.asInstanceOf[String]).asInstanceOf[js.Any]
      val frame = PicklerRegistry.unpickle(json).asInstanceOf[vfd.uav.DataFrame]
      input() = frame
    }

    val element = div(`class` := "container-fluid")(
      div(`class` := "row")(
        div(`class` := "col-xs-12")(
          div(`class` := "panel panel-default")(
            div(`class` := "panel-body")(
             Panels.autopilot
            )
          )
        )
      ),
      div(`class` := "row")(
        div(`class` := "col-xs-4")(
          div(`class` := "panel panel-default")(
            div(`class` := "panel-body")(
              Panels.secondary(input)
            )
          )
        ),
        div(`class` := "col-xs-5")(
          div(`class` := "panel panel-default")(
            div(`class` := "panel-body")(
              Panels.primary(input) 
            )
          )
        ),
        div(`class` := "col-xs-3")(
          div(`class` := "panel panel-default")(
            div(`class` := "panel-body")(
              Panels.eicas()
            )
          )
        )
      )
    )

    app.root.appendChild(element.render)
  }

 



/*
  def alert() = {
    val image = "/assets/images/leds/led.svg"
    val off = "#782121"
    val on = "#ff0000"
    val controls = div(
      `object`("data".attr := image, `type` := "image/svg+xml", width:= "32px")(
        "Cannot load"
      ),
      audio(
        "autoplay".attr:="true",
        source(src:="/assets/audio/alarm.ogg", `type`:="audio/ogg")
      )

    ).render
    controls
  }*/

}