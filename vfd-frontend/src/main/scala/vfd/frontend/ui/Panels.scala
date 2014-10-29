package vfd.frontend.ui

import rx._
import rx.ops._
import scalatags.JsDom.all._
import vfd.uav.DataFrame
import vfd.frontend.util.Application
import vfd.frontend.util.Framework._


object Panels {

  def primary(input: Rx[DataFrame])(implicit app: Application) = div(
    Components.heading(input map (_.heading), "33%"),
    Components.attitude(input map (i => (i.pitch, i.roll)), "33%"),
    Components.altitude(input map (_.altitude), "33%")
  )

  def secondary(input: Rx[DataFrame])(implicit app: Application) = div(
    iframe(
      width:="100%",
      height:="350px",
        "frameborder".attr:="0",
        "scrolling".attr:="no",
        "marginheight".attr:="0",
        "marginwidth".attr:="0",
        src:="http://www.openstreetmap.org/export/embed.html?bbox=6.5611016750335684%2C46.51718501017836%2C6.570038795471191%2C46.520577350893525&amp;layer=mapnik"
      ),
      table(`class`:="table")(
        tr(
          td("UAV Position"),
          td("N13.1234 E1234.23465")
        ),
        tr(
          td("Base Position"),
          td("N13.1234 E1234.23465")
            ),
            tr(
              td("Distance to UAV"),
              td("200 m")
            ),
            tr(
              td("Total flight distance"),
              td("12.3 km")
            ),
            tr(
              td("Groundspeed"),
              td("23 km/h")
            ),
            tr(
              td("---"),
              td("")
            ),
            tr(
              td("Below"),
              td("180 cm")
            )
        )
  )

  def eicas()(implicit app: Application) = {
    table(`class`:="table")(
      tr(
        td("Link Server"),
        td("3"),
        td("ms"),
        td(img(src:="/assets/images/leds/red-off.svg",width:="16px"))
      ),
      tr(
        td("Link UAV"),
        td("-80"),
        td("dB(m)"),
        td(img(src:="/assets/images/leds/red-on.svg",width:="16px"))
       ),
      tr(
        td("---"),
        td(""),
        td(""),
        td("")
      ),
      tr(
        td("Full Charge"),
        td("5.000"),
        td("Ah"),
        td()
      ),
      tr(
        td("Charge"),
        td("4.800"),
        td("Ah"),
        td(img(src:="/assets/images/leds/red-off.svg",width:="16px"))
      ),
      tr(
        td("Current"),
        td("80"),
        td("A"),
        td(img(src:="/assets/images/leds/yellow-on.svg",width:="16px"))
      ),
      tr(
        td("Endurance"),
        td("14"),
        td("min"),
        td(img(src:="/assets/images/leds/none.svg",width:="16px"))
      ),
      tr(
        td("GPS"),
        td("5"),
        td("satellites"),
        td(img(src:="/assets/images/leds/none.svg",width:="16px"))
      )
    )
  }

  def autopilot = div(`class` := "btn-toolbar")(
    div(`class`:="btn-group")(
      button(`type`:="button", `class`:="btn btn-default")("Auto"),
      button(`type`:="button", `class`:="btn btn-default")("Position"),
      button(`type`:="button", `class`:="btn btn-default")("Attitude")
    ),
    div(`class`:="btn-group")(
      button(`type`:="button", `class`:="btn btn-default")(
        span(`class`:="label label-default")("Manual")
      )
    )
  )
}