package vfd.index

import org.mavlink.Parser
import org.mavlink.messages.Message
import org.mavlink.messages.Heartbeat

import org.scalajs.dom
import org.scalajs.dom.html

import rx._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

import scalatags.JsDom.all._

@JSExport("Main")
object Main {
  import Util._

  case class ActiveVehicle(id: Int)

  val active = Var(Set.empty[ActiveVehicle])

  val parser = new Parser(
    packet => Message.unpack(packet.messageId, packet.payload) match {
      case hb: Heartbeat =>
        active() += ActiveVehicle(packet.systemId)
      case _ => ()
    })

  @JSExport
  def main(root: html.Element, baseAssets: String, args: js.Dictionary[String]): Unit = {

    val connection = new dom.WebSocket(args("socketUrl"))

    connection.binaryType = "arraybuffer";
    connection.onmessage = (e: dom.MessageEvent) => {
      val buffer = e.data.asInstanceOf[js.typedarray.ArrayBuffer]
      val view = new js.typedarray.DataView(buffer)

      for (i <- 0 until view.byteLength) {
        parser.push(view.getInt8(i))
      }
    }

    root.appendChild(table(`class` := "table table-hover")(
      thead(
        tr(
          th("System ID"),
          th(""))),
      Rx {
        tbody(
          for (vehicle <- active().toSeq) yield {
            tr(
              td(vehicle.id),
              td(a(href := "/dashboard/" + vehicle.id, `class` := "btn btn-default")("Pilot")))
          })
      }).render)
  }

}