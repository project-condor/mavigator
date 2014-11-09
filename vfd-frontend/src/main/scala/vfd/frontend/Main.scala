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
              button(`class` := "btn")("ACK"),
              img(`src` := env.asset("images/leds/red-on.svg"), height := "30px"))))),
      div(`class` := "row")(
        div(`class` := "col-xs-4")(
          div(`class` := "panel panel-default")(
            div(`class` := "panel-body")(
              iframe(
                width := "100%",
                height := "350px",
                "frameborder".attr := "0",
                "scrolling".attr := "no",
                "marginheight".attr := "0",
                "marginwidth".attr := "0",
                src := "http://www.openstreetmap.org/export/embed.html?bbox=6.5611016750335684%2C46.51718501017836%2C6.570038795471191%2C46.520577350893525&amp;layer=mapnik")))),
        div(`class` := "col-xs-5")(
          div(`class` := "panel panel-default")(
            div(`class` := "panel-body")(
              panels.Primary()))),
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