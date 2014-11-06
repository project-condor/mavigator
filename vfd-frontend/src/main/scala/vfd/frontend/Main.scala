package vfd.frontend

import scala.scalajs.js
import scala.scalajs.js.Any.fromFunction1
import scala.scalajs.js.Any.fromString

import org.mavlink.Packet
import org.mavlink.Parser
import org.scalajs.dom

import rx.Rx
import rx.Var
import rx.Var
import scalatags.JsDom.all.`class`
import scalatags.JsDom.all.div
import scalatags.JsDom.all.stringAttr
import vfd.frontend.ui.Panels
import vfd.frontend.util.Application

class Main(socketUrl: String)(implicit app: Application) {

  def main() = {

    //websocket conveying mavlink data
    val connection = new dom.WebSocket(socketUrl)

    //reactive propagation of mavlink events
    val packet: Var[Packet] = Var(Packet.Empty)
    val crcErrors: Var[Int] = Var(0)

    val parser = new Parser(
      p => {
        Rx { packet() = p }
        dom.console.log("got packet: seq " + (p.seq.toInt & 0xff) + ", mid " + (p.messageId.toInt & 0xff))
      },
      () => {
        //Rx{crcErrors() += 1}
        dom.console.log("crc error")
      })

    connection.binaryType = "arraybuffer";
    connection.onmessage = (e: dom.MessageEvent) => {

      val buffer = e.data.asInstanceOf[js.typedarray.ArrayBuffer]
      val dv = new js.typedarray.DataView(buffer)

      for (i <- 0 until dv.byteLength) {
        parser.push(dv.getInt8(i))

      }

    }

    val element = div(`class` := "container-fluid")(
      div(`class` := "row")(
        div(`class` := "col-xs-12")(
          div(`class` := "panel panel-default")(
            div(`class` := "panel-body")(
              Panels.autopilot)))),
      div(`class` := "row")(
        div(`class` := "col-xs-4")(
          div(`class` := "panel panel-default")(
            div(`class` := "panel-body")(
              Panels.secondary()))),
        div(`class` := "col-xs-5")(
          div(`class` := "panel panel-default")(
            div(`class` := "panel-body")(
              //Panels.primary(input) 
              Panels.mavlink(packet, crcErrors)))),
        div(`class` := "col-xs-3")(
          div(`class` := "panel panel-default")(
            div(`class` := "panel-body")(
              Panels.eicas())))))

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