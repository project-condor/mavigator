package vfd.frontend.ui.panels

import org.mavlink.messages.Attitude
import org.scalajs.dom.HTMLElement

import rx.core.Obs
import scalatags.JsDom.all.bindNode
import scalatags.JsDom.all.`class`
import scalatags.JsDom.all.stringAttr
import scalatags.JsDom.all.table
import scalatags.JsDom.all.tbody
import scalatags.JsDom.all.td
import scalatags.JsDom.all.tr
import vfd.frontend.Environment
import vfd.frontend.MavlinkSocket
import vfd.frontend.ui.components.Altimeter
import vfd.frontend.ui.components.Compass
import vfd.frontend.ui.components.Horizon

object Primary {

  def apply(socket: MavlinkSocket)(implicit env: Environment): HTMLElement = {

    val compass = new Compass
    val horizon = new Horizon
    val altimeter = new Altimeter

    Obs(socket.message, skipInitial = true) {
      socket.message() match {
        case Attitude(roll, pitch, yaw) =>
          horizon.update(pitch, roll)
          compass.update(yaw)
        case _ => ()
      }
    }

    table(`class` := "table-instrument")(
      tbody(
        tr(
          td(compass.element),
          td(horizon.element),
          td(altimeter.element)))).render

  }

}