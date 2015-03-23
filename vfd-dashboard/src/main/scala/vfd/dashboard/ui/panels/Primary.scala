package vfd.dashboard.ui.panels

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
        //TODO match message and update UI
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