package mavigator.index

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

import org.scalajs.dom.html

import mavigator.dashboard.ui.Layout
import mavigator.util.Environment
import mavigator.util.Application

import scalatags.JsDom.all._

import org.mavlink.Parser
import org.mavlink.messages.Message
import org.mavlink.messages.Heartbeat

import org.scalajs.dom

import rx._


@JSExport("mavigator_index_Main")
object Main extends Application {
  import Util._

  val active = Var(Set.empty[ActiveVehicle])

  val parser = new Parser(
    packet => {
      val m: Message = Message.unpack(packet.messageId, packet.payload)
      println(m)
      m match {
        case hb: Heartbeat =>
          active() += ActiveVehicle.fromHeartbeat(packet.systemId, hb)
        case _ => ()
      }
    }
  )

  override def main(args: Map[String, String])(implicit env: Environment): Unit = {
    val root = env.root
    val connection = new dom.WebSocket(args("socketUrl"))

    connection.binaryType = "arraybuffer"
    connection.onmessage = (e: dom.MessageEvent) => {
      val buffer = e.data.asInstanceOf[js.typedarray.ArrayBuffer]
      val view = new js.typedarray.DataView(buffer)

      for (i <- 0 until view.byteLength) {
        parser.push(view.getInt8(i))
      }
    }

    connection.onclose = (e: dom.Event) => {
      while (root.firstChild != null) {
        root.removeChild(root.firstChild);
      }
      root.appendChild(
        div(`class`:="alert alert-danger")(
          "Connection to server unexpectedly closed. Check server logs for errors."
        ).render
      )

    }

    root.appendChild(div(
      table(`class` := "table table-hover")(
        thead(
          tr(
            th("System ID"),
            th("Type"),
            th("Autopilot"),
            th("State"),
            th("")
          )
        ),
        Rx {
          tbody(
            for (vehicle <- active().toSeq) yield {
              tr(
                td(vehicle.systemId),
                td(vehicle.vehicleType),
                td(vehicle.autopilot),
                td(vehicle.state),
                td(a(href := "/dashboard/" + vehicle.systemId, `class` := "btn btn-default")("Pilot"))
              )
            }
          )
        }
      ),
      i(`class`:="fa fa-spinner fa-spin")(),
      " Listening for heartbeats..."
    ).render)

  }
}
