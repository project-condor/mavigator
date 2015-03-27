package vfd.dashboard.ui

import org.mavlink.messages.Attitude
import org.mavlink.messages.Motor
import org.mavlink.messages.Power

import rx.Obs
import scalatags.JsDom.all._
import vfd.dashboard.Environment
import vfd.dashboard.MavlinkSocket
import vfd.dashboard.ui.instruments.Altimeter
import vfd.dashboard.ui.instruments.Balance
import vfd.dashboard.ui.instruments.Bar
import vfd.dashboard.ui.instruments.Compass
import vfd.dashboard.ui.instruments.Generic
import vfd.dashboard.ui.instruments.Horizon

class Layout(socket: MavlinkSocket)(implicit env: Environment) {

  private def panel(contents: Frag*) = div(`class` := "d-panel")(contents: _*)

  private def mode(name: String, kind: String, on: Boolean = false) = div(`class` := s"mode $kind ${if (!on) "off"}")(name)

  val modes = div(
    mode("MANUAL", "warning", true),
    mode("STABILIZED", "info", true),
    mode("GUIDED", "success", true),
    mode("AUTO", "success", true))

  val infos = div(
    mode("BAY", "info"),
    mode("RECOVERY", "danger"),
    mode("NOGPS", "warning", true),
    mode("OVERLOAD", "danger", true),
    mode("BATTERY", "danger", false),
    mode("LINK", "danger", true),
    mode("SOCKET", "danger", true),
    div(style := "float: right")(mode("CRITICAL", "danger", true)))

  val map = iframe(
    width := 100.pct,
    height := 350.px,
    "frameborder".attr := "0",
    "scrolling".attr := "no",
    "marginheight".attr := "0",
    "marginwidth".attr := "0",
    src := "http://www.openstreetmap.org/export/embed.html?bbox=6.5611016750335684%2C46.51718501017836%2C6.570038795471191%2C46.520577350893525&amp;layer=mapnik")

  val feed = div(style := "width: 100%; height: 460px; color: #ffffff; background-color: #c2c2c2; text-align: center;")(
    p(style := "padding-top: 220px")("video feed"))

  val altimeter = new Altimeter
  val horizon = new Horizon
  val compass = new Compass
  val motor0 = new Generic(0, 50, 100, "%")
  val motor1 = new Generic(0, 50, 100, "%")
  val motor2 = new Generic(0, 50, 100, "%")
  val motor3 = new Generic(0, 50, 100, "%")
  val powerDistribution = new Balance()
  val batteryLevel = new Bar()

  val top = header(
    div("Flight Control Panel"),
    div("00:00:00"),
    div("System #"))

  val left = panel(
    map,
    table(`class` := "table-instrument")(
      thead("Motors"),
      tbody(
        tr(
          td(motor0.element),
          td(),
          td(motor1.element),
          td()
        ),
        tr(
          td(),
          td(powerDistribution.element),
          td(),
          td()
        ),
        tr(
          td(motor2.element),
          td(),
          td(motor3.element),
          td()
        )
      )
    )
  )

  val center = panel(feed)

  val below = panel(
    table(`class` := "table-instrument")(
      tbody(
        tr(
          td(compass.element),
          td(horizon.element),
          td(altimeter.element)
        )
      )
    )
  )

  val right = panel()

  val element = div(`class` := "d-container d-column")(
    div(`class` := "d-above")(
      top),
    div(`class` := "d-above d-container d-row")(
      panel(modes),
      panel(infos)),
    div(`class` := "d-container d-row")(
      div(`class` := "d-container d-details")(
        panel("foo")),
      div(`class` := "d-container d-left")(
        left),
      div(`class` := "d-container d-column d-middle")(
        div(`class` := "d-container d-center")(
          center),
        div(`class` := "d-container d-below")(
          below)
      ),
      div(`class` := "d-container d-right")(
        right
      )
    )
  ).render

  //message router
  Obs(socket.message, skipInitial = true) {
    socket.message() match {

      case Motor(m0, m1, m2, m3) =>
        motor0.value() = m0
        motor1.value() = m1
        motor2.value() = m2
        motor3.value() = m3
        powerDistribution.value() = (m0, m1, m2, m3)

      case Power(mV) =>
        batteryLevel.value() = (100 * (mV - 9600) / 12600)

      case Attitude(roll, pitch, yaw) =>
        horizon.value() = (pitch, roll)
        compass.value() = (yaw)
        
    }
  }

}