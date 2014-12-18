package vfd.frontend.ui.panels

import rx.Rx
import rx.Rx
import scalatags.JsDom.all.`class`
import scalatags.JsDom.all.div
import scalatags.JsDom.all.i
import scalatags.JsDom.all.intFrag
import scalatags.JsDom.all.stringAttr
import scalatags.JsDom.all.stringFrag
import scalatags.JsDom.all.style
import scalatags.JsDom.all.table
import scalatags.JsDom.all.tbody
import scalatags.JsDom.all.td
import scalatags.JsDom.all.tr
import vfd.frontend.util.Environment
import vfd.frontend.util.Framework.RxStr
import vfd.frontend.ui.Components
import rx.core.Var
import rx.core.Obs
import org.mavlink.messages._

object Communication {

  def apply(packets: Rx[Int], crcs: Rx[Int], overflows: Rx[Int], wrongIds: Rx[Int], message: Rx[Message])(implicit app: Environment) = {
    
    val m0 = Var(0.0)
    val m1 = Var(0.0)
    val m2 = Var(0.0)
    val m3 = Var(0.0)
    val battery = Var(0.0)
    
    Obs(message) {
      message() match {
        case Motor(_m0, _m1, _m2, _m3) =>
          m0() = _m0
          m1() = _m1
          m2() = _m2
          m3() = _m3
        case Power(mV) => battery() = mV / 120
        case _ => ()
      }
    }
    
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
            td(i(`class` := "fa fa-heart", style := "color: #ff0000;"))))),
      "Packet Statistics",
      table(`class` := "table table-condensed")(
        tbody(
          tr(
            td("OK"),
            td(packets),
            td(`class` := "danger")("CRC"),
            td(crcs),
            td("OFLW"),
            td(overflows),
            td("BID"),
            td(wrongIds)))),
      div(Components.bar(battery, "25%")),
      div(
        Components.basic(m0, "25%"),Components.basic(m1, "25%"),Components.basic(m2, "25%"),Components.basic(m3, "25%")))
  }

}