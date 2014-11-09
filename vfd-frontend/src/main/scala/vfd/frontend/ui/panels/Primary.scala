package vfd.frontend.ui.panels

import rx.core.Var
import scalatags.JsDom.all.div
import vfd.frontend.ui.Components
import vfd.frontend.util.Environment

object Primary {

  def apply()(implicit env: Environment) = {
    val pitchRoll = Var((0.0, 0.0))
    val heading = Var(0.0)
    val altitude = Var(0.0)

    div(
      Components.heading(heading, "33%"),
      Components.attitude(pitchRoll, "33%"),
      Components.altitude(altitude, "33%"))
  }

}