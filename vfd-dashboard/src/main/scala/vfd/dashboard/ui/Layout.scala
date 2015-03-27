package vfd.dashboard.ui

import org.scalajs.dom.html
import scalatags.JsDom.all.ExtendedString
import scalatags.JsDom.all.Int2CssNumber
import scalatags.JsDom.all.`class`
import scalatags.JsDom.all.div
import scalatags.JsDom.all.header
import scalatags.JsDom.all.height
import scalatags.JsDom.all.iframe
import scalatags.JsDom.all.p
import scalatags.JsDom.all.src
import scalatags.JsDom.all.stringAttr
import scalatags.JsDom.all.stringFrag
import scalatags.JsDom.all.stringPixelStyle
import scalatags.JsDom.all.style
import scalatags.JsDom.all.width
import scalatags.JsDom.all.button
import scalatags.JsDom.all.id
import scalatags.JsDom.all._
import scalatags.jsdom._
import scalatags.jsdom.Frag
import vfd.dashboard.Environment
import vfd.dashboard.MavlinkSocket
import vfd.dashboard.ui.panels.Communication
import vfd.dashboard.ui.panels.Primary
import org.scalajs.dom.MouseEvent
import org.scalajs.dom

class Layout(socket: MavlinkSocket)(implicit env: Environment) {

  private def panel(contents: Frag*) = div(`class` := "d-panel")(contents: _*)

  def layout =
    div(`class` := "d-container d-column")(
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
            below)),
        div(`class` := "d-container d-right")(
          right)))

  def top = header(
    div("Flight Control Panel"),
    div("00:00:00"),
    div("System #"))

  def left = panel(map)
  def center = panel(feed)
  def below = panel(Primary(socket))
  def right = panel(Communication(socket))

  def mode(name: String, kind: String, on: Boolean = false) = div(`class` := s"mode $kind ${if (!on) "off"}")(name)

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

  def element: html.Element = layout.render
}