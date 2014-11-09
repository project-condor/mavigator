package vfd.frontend.ui.panels

import rx.Rx
import rx.Rx
import scalatags.JsDom.all.style
import scalatags.JsDom.all.`class`
import scalatags.JsDom.all.div
import scalatags.JsDom.all.i
import scalatags.JsDom.all.intFrag
import scalatags.JsDom.all.stringAttr
import scalatags.JsDom.all.stringFrag
import scalatags.JsDom.all.table
import scalatags.JsDom.all.tbody
import scalatags.JsDom.all.td
import scalatags.JsDom.all.tr
import vfd.frontend.util.Environment
import vfd.frontend.util.Framework.RxStr

object Communication {

  def apply(packets: Rx[Int], crcs: Rx[Int], overflows: Rx[Int], wrongIds: Rx[Int])(implicit app: Environment) = {
    div(
      "Link Status",
      table(`class` := "table table-condensed")(
        tbody(
          tr(
            td("Uplink"),
            td("-20 dBm"),
            td("Server"),
            td("5 ms")),
          tr(
            td("Heartbeat"),
            td(i(`class` := "fa fa-heart", style:="color: #ff0000;"))))),
      "Packet Statistics",
      table(`class` := "table table-condensed")(
        tbody(
          tr(
            td("OK"),
            td(packets),
            td(`class` := "danger")("CRC"),
            td(crcs),
            td("OFLW"),
            td(packets),
            td("BID"),
            td(packets)))))
  }

}