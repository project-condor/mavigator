package vfd.dashboard.ui

import rx._
import scalatags.JsDom.all._
import vfd.dashboard.Environment
import vfd.dashboard.MavlinkSocket
import vfd.dashboard.ui.instruments._
import org.mavlink.messages._
import vfd.dashboard.rxutil._

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

  val altimeter = new Altimeter(
    Var(0.0)
  )
  val horizon = new Horizon(socket.message.collect((0.0, 0.0)) {
    case att: Attitude => (att.pitch, att.roll)
  })
  val compass = new Compass(socket.message.collect(0.0) {
    case att: Attitude => att.yaw
  })
  val motor0 = new Generic(0, 50, 100, "%", socket.message.collect(0.0) {
    case s: ServoOutputRaw => 100 * (s.servo1Raw - 1000) / 1000
  })
  val motor1 = new Generic(0, 50, 100, "%", socket.message.collect(0.0) {
    case s: ServoOutputRaw => 100 * (s.servo2Raw - 1000) / 1000
  })
  val motor2 = new Generic(0, 50, 100, "%", socket.message.collect(0.0) {
    case s: ServoOutputRaw => 100 * (s.servo3Raw - 1000) / 1000
  })
  val motor3 = new Generic(0, 50, 100, "%", socket.message.collect(0.0) {
    case s: ServoOutputRaw => 100 * (s.servo4Raw - 1000) / 1000
  })
  val powerDistribution = new Distribution(
    socket.message.collect((0.0, 0.0, 0.0, 0.0)) {
      case s: ServoOutputRaw => (s.servo1Raw, s.servo2Raw, s.servo3Raw, s.servo4Raw)
    }
  )
  val batteryLevel = new Bar(
    Var(0.0)
  )

  val top = header(
    div("Flight Control Panel"),
    div((new Clock).element),
    div("UAV " + socket.remoteSystemId)
  )

  val left = panel(
    map,
    table(`class` := "table-instrument")(
      thead("Motors"),
      tbody(
        tr(
          td(motor1.element),
          td(),
          td(motor0.element),
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

}