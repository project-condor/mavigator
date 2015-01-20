package vfd.dashboard.ui

import org.scalajs.dom.HTMLElement

import scalatags.JsDom.all.ExtendedString
import scalatags.JsDom.all.bindNode
import scalatags.JsDom.all.`class`
import scalatags.JsDom.all.div
import scalatags.JsDom.all.height
import scalatags.JsDom.all.iframe
import scalatags.JsDom.all.p
import scalatags.JsDom.all.src
import scalatags.JsDom.all.stringAttr
import scalatags.JsDom.all.stringFrag
import scalatags.JsDom.all.stringStyle
import scalatags.JsDom.all.style
import scalatags.JsDom.all.width
import vfd.dashboard.Environment
import vfd.dashboard.MavlinkSocket
import vfd.dashboard.ui.panels.Communication
import vfd.dashboard.ui.panels.Primary

class Layout(socket: MavlinkSocket) {

  val map = iframe(
    width := "100%",
    height := "350px",
    "frameborder".attr := "0",
    "scrolling".attr := "no",
    "marginheight".attr := "0",
    "marginwidth".attr := "0",
    src := "http://www.openstreetmap.org/export/embed.html?bbox=6.5611016750335684%2C46.51718501017836%2C6.570038795471191%2C46.520577350893525&amp;layer=mapnik")

  val feed = div(style := "width: 100%; height: 460px; color: #ffffff; background-color: #c2c2c2; text-align: center;")(
    p(style := "padding-top: 220px")("video feed"))

  def element(implicit env: Environment): HTMLElement = div(`class` := "container-fluid")(
    div(`class` := "row")(
      div(`class` := "col-xs-12")(
        div(`class` := "panel panel-default")(
          div(`class` := "panel-body")()))),
    div(`class` := "row")(
      div(`class` := "col-xs-4")(
        div(`class` := "panel panel-default")(
          div(`class` := "panel-body")(
            map))),
      div(`class` := "col-xs-5")(
        div(`class` := "panel panel-default")(
          div(`class` := "panel-body")(
            feed)),
        div(`class` := "panel panel-default")(
          div(`class` := "panel-body")(Primary(socket)))),
      div(`class` := "col-xs-3")(
        div(`class` := "panel panel-default")(
          div(`class` := "panel-body")(Communication(socket)))))).render

}