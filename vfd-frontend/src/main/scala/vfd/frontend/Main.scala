package vfd.frontend

import org.mavlink.messages.Message
import rx.ops.RxOps
import scalatags.JsDom.all.ExtendedString
import scalatags.JsDom.all.button
import scalatags.JsDom.all.`class`
import scalatags.JsDom.all.div
import scalatags.JsDom.all.height
import scalatags.JsDom.all.iframe
import scalatags.JsDom.all.img
import scalatags.JsDom.all.src
import scalatags.JsDom.all.stringAttr
import scalatags.JsDom.all.stringFrag
import scalatags.JsDom.all.stringStyle
import scalatags.JsDom.all.width
import vfd.frontend.ui.panels
import vfd.frontend.util.Environment
import rx.core.Obs

object Main {

  def main(args: Map[String, String])(implicit env: Environment) = {
    val socketUrl = args("socketurl")
    val remoteSystemId = args("remotesystemid").toInt

    val socket = new MavlinkSocket(socketUrl, remoteSystemId)

    val message = socket.packet.map { p =>
      Message.unpack(socket.packet().messageId, socket.packet().payload)
    }

    Obs(message) {
      println(message().toString())
    }

    val element = div(`class` := "container-fluid")(
      div(`class` := "row")(
        div(`class` := "col-xs-12")(
          div(`class` := "panel panel-default")(
            div(`class` := "panel-body")(
              button(`class` := "btn")("LOCKED"),
              button(`class` := "btn")("MANUAL"),
              button(`class` := "btn")("GUIDED"),
              button(`class` := "btn")("AUTO"))))),
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
              panels.Primary(message)))),
        div(`class` := "col-xs-3")(
          div(`class` := "panel panel-default")(
            div(`class` := "panel-body")(
              panels.Communication(
                socket.stats.packets,
                socket.stats.crcErrors,
                socket.stats.overflows,
                socket.stats.wrongIds,
                message))))))

    env.root.appendChild(element.render)
  }
}