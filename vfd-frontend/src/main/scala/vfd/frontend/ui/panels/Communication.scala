package vfd.frontend.ui.panels

import rx.Rx
import rx.Rx
import scalatags.JsDom.all.`class`
import scalatags.JsDom.all.div
import scalatags.JsDom.all.intFrag
import scalatags.JsDom.all.stringAttr
import scalatags.JsDom.all.stringFrag
import scalatags.JsDom.all.table
import scalatags.JsDom.all.tbody
import scalatags.JsDom.all.td
import scalatags.JsDom.all.tr
import vfd.frontend.util.Environment
import vfd.frontend.util.Framework.RxStr
import rx.core.Var
import rx.core.Obs

object Communication {

  def apply(packets: Rx[Int], crcs: Rx[Int], overflows: Rx[Int], wrongIds: Rx[Int])(implicit app: Environment) = {
    div(
      "Packets",
      table(`class` := "table")(
        tr(
          td("OK"),
          td(packets),
          td("CRC"),
          td(crcs),
          td("OFLW"),
          td(packets),
          td("BID"),
          td(packets))))
  }

}