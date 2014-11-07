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
import scalatags.JsDom.all._

import vfd.frontend.ui.panels
import vfd.frontend.util.Environment

object Main {

  def main(args: Map[String, String])(implicit env: Environment) = {
    val socketUrl = args("socketurl")
    val remoteSystemId = args("remotesystemid").toInt

    val socket = new MavlinkSocket(socketUrl, remoteSystemId)

    val element = div(`class` := "container-fluid")(
      div(`class` := "row")(
        div(`class` := "col-xs-12")(
          div(`class` := "panel panel-default")(
            div(`class` := "panel-body")(
              "autopilot mode")))),
      div(`class` := "row")(
        div(`class` := "col-xs-4")(
          div(`class` := "panel panel-default")(
            div(`class` := "panel-body")(
              "navigation"))),
        div(`class` := "col-xs-5")(
          div(`class` := "panel panel-default")(
            div(`class` := "panel-body")(
              "primary"))),
        div(`class` := "col-xs-3")(
          div(`class` := "panel panel-default")(
            div(`class` := "panel-body")(
              panels.Communication(
                socket.stats.packets,
                socket.stats.crcErrors,
                socket.stats.overflows,
                socket.stats.wrongIds))))))

    env.root.appendChild(element.render)
  }
}