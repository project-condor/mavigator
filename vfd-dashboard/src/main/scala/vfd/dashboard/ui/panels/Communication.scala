package vfd.dashboard.ui.panels

import org.scalajs.dom.html

import rx.core.Obs
import scalatags.JsDom.all.bindNode
import scalatags.JsDom.all.`class`
import scalatags.JsDom.all.div
import scalatags.JsDom.all.i
import scalatags.JsDom.all.stringAttr
import scalatags.JsDom.all.stringFrag
import scalatags.JsDom.all.stringPixelStyle
import scalatags.JsDom.all.style
import scalatags.JsDom.all.table
import scalatags.JsDom.all.tbody
import scalatags.JsDom.all.td
import scalatags.JsDom.all.thead
import scalatags.JsDom.all.tr
import scalatags.JsDom.all.width
import vfd.dashboard.Environment
import vfd.dashboard.MavlinkSocket
import vfd.dashboard.ui.components.Balance
import vfd.dashboard.ui.components.Bar
import vfd.dashboard.ui.components.Generic
import vfd.dashboard.ui.components.Led

object Communication {

  def apply(socket: MavlinkSocket)(implicit app: Environment): html.Element = {

    val hb = i(`class` := "fa fa-heart heartbeat").render
    
    def foo() = {
      hb.textContent = ""
    }

    val motor0 = new Generic(0, 50, 100, "%")
    val motor1 = new Generic(0, 50, 100, "%")
    val motor2 = new Generic(0, 50, 100, "%")
    val motor3 = new Generic(0, 50, 100, "%")
    val powerDistribution = new Balance()
    val batteryLevel = new Bar()

    Obs(socket.message, skipInitial = true) {
      socket.message() match {
        //TODO match message and update UI 
        case _ =>
      }
    }

    div(
      table(`class` := "table")(
        thead("Communication"),
        tbody(
          tr(
            td("Conn"),
            div(width := "20px")(td((new Led()).element)),
            td("Server"),
            td("5 ms")),
          tr(
            td("Uplink"),
            td("-20 dBm"),
            td("Heartbeat"),
            td(hb)))),
      table(`class` := "table-instrument", style := "height: 100px")(
        tbody(
          tr(
            td(),
            td(),
            td(),
            td(),
            td(),
            td(),
            td(),
            td(),
            td(),
            td(batteryLevel.element)))),
        table (`class` := "table-instrument")(
          thead("Motors"),
          tbody(
            tr(
              td(motor0.element),
              td(),
              td(motor1.element)),
            tr(
              td(),
              td(powerDistribution.element),
              td()),
            tr(
              td(motor2.element),
              td(),
              td(motor3.element))))).render
  }

}