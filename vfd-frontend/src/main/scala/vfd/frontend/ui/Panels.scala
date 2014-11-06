package vfd.frontend.ui

import org.mavlink.Packet
import org.mavlink.messages.Heartbeat
import org.mavlink.messages.Message

import rx.Rx
import rx.Rx
import rx.ops.RxOps
import scalatags.JsDom.all.ExtendedString
import scalatags.JsDom.all.button
import scalatags.JsDom.all.byteFrag
import scalatags.JsDom.all.`class`
import scalatags.JsDom.all.div
import scalatags.JsDom.all.height
import scalatags.JsDom.all.iframe
import scalatags.JsDom.all.img
import scalatags.JsDom.all.intFrag
import scalatags.JsDom.all.p
import scalatags.JsDom.all.span
import scalatags.JsDom.all.src
import scalatags.JsDom.all.stringAttr
import scalatags.JsDom.all.stringFrag
import scalatags.JsDom.all.stringStyle
import scalatags.JsDom.all.table
import scalatags.JsDom.all.tbody
import scalatags.JsDom.all.td
import scalatags.JsDom.all.th
import scalatags.JsDom.all.thead
import scalatags.JsDom.all.tr
import scalatags.JsDom.all.`type`
import scalatags.JsDom.all.width
import vfd.frontend.util.Application
import vfd.frontend.util.Framework.RxStr
import vfd.frontend.util.Framework.rxMod
import vfd.frontend.util.richRx

object Panels {

  def primary(input: Rx[Message])(implicit app: Application) = {
    val foos = input.only[Heartbeat]
    div(
      Components.heading(foos.map(_ => 0), "33%"),
      Components.attitude(input map (i => (21, 40)), "33%"),
      Components.altitude(input map (_ => 0), "33%"))
  }

  def mavlink(packet: Rx[Packet], crc: Rx[Int])(implicit app: Application) = {
    div(
      p(
        "CRC errors: ",
        span(
          Rx { crc() })),
      table(`class` := "table")(
        thead(
          tr(
            th("System"),
            th("Component"),
            th("Message"),
            th("Payload Length"))),
        tbody(
          Rx {
            tr(
              td(packet().systemId),
              td(packet().componentId),
              td(packet().messageId),
              td(packet().payload.length))
          })))
  }

  def secondary()(implicit app: Application) = div(
    iframe(
      width := "100%",
      height := "350px",
      "frameborder".attr := "0",
      "scrolling".attr := "no",
      "marginheight".attr := "0",
      "marginwidth".attr := "0",
      src := "http://www.openstreetmap.org/export/embed.html?bbox=6.5611016750335684%2C46.51718501017836%2C6.570038795471191%2C46.520577350893525&amp;layer=mapnik"),
    table(`class` := "table")(
      tr(
        td("UAV Position"),
        td("N13.1234 E1234.23465")),
      tr(
        td("Base Position"),
        td("N13.1234 E1234.23465")),
      tr(
        td("Distance to UAV"),
        td("200 m")),
      tr(
        td("Total flight distance"),
        td("12.3 km")),
      tr(
        td("Groundspeed"),
        td("23 km/h")),
      tr(
        td("---"),
        td("")),
      tr(
        td("Below"),
        td("180 cm"))))

  def eicas()(implicit app: Application) = {
    table(`class` := "table")(
      tr(
        td("Link Server"),
        td("3"),
        td("ms"),
        td(img(src := "/assets/images/leds/red-off.svg", width := "16px"))),
      tr(
        td("Link UAV"),
        td("-80"),
        td("dB(m)"),
        td(img(src := "/assets/images/leds/red-on.svg", width := "16px"))),
      tr(
        td("---"),
        td(""),
        td(""),
        td("")),
      tr(
        td("Full Charge"),
        td("5.000"),
        td("Ah"),
        td()),
      tr(
        td("Charge"),
        td("4.800"),
        td("Ah"),
        td(img(src := "/assets/images/leds/red-off.svg", width := "16px"))),
      tr(
        td("Current"),
        td("80"),
        td("A"),
        td(img(src := "/assets/images/leds/yellow-on.svg", width := "16px"))),
      tr(
        td("Endurance"),
        td("14"),
        td("min"),
        td(img(src := "/assets/images/leds/none.svg", width := "16px"))),
      tr(
        td("GPS"),
        td("5"),
        td("satellites"),
        td(img(src := "/assets/images/leds/none.svg", width := "16px"))))
  }

  def autopilot = div(`class` := "btn-toolbar")(
    div(`class` := "btn-group")(
      button(`type` := "button", `class` := "btn btn-default")("Auto"),
      button(`type` := "button", `class` := "btn btn-default")("Position"),
      button(`type` := "button", `class` := "btn btn-default")("Attitude")),
    div(`class` := "btn-group")(
      button(`type` := "button", `class` := "btn btn-default")(
        span(`class` := "label label-default")("Manual"))))
}