package vfd.dashboard.ui.panels

import org.mavlink.messages.{VfrHud, Attitude, GlobalPositionInt}
import org.scalajs.dom.html

import rx.core.Obs
import scalatags.JsDom.all.bindNode
import scalatags.JsDom.all.`class`
import scalatags.JsDom.all.stringAttr
import scalatags.JsDom.all.table
import scalatags.JsDom.all.tbody
import scalatags.JsDom.all.td
import scalatags.JsDom.all.tr
import vfd.dashboard.Environment
import vfd.dashboard.MavlinkSocket
import vfd.dashboard.ui.components.Altimeter
import vfd.dashboard.ui.components.Compass
import vfd.dashboard.ui.components.Horizon

object Primary {

  def apply(socket: MavlinkSocket)(implicit env: Environment): html.Element = {

    val compass = new Compass
    val horizon = new Horizon
    val altimeter = new Altimeter

    Obs(socket.message, skipInitial = true) {
      socket.message() match {
        case a: Attitude =>
//          compass.update(a.yaw / Math.PI * 360)
//          horizon.update(a.pitch, a.roll)
        case vh: VfrHud =>
//          altimeter.update(vh.alt)
//          compass.update(vh.heading / Math.PI * 360)
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