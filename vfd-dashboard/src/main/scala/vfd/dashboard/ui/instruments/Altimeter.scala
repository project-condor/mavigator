package vfd.dashboard.ui.instruments

import org.scalajs.dom.html
import vfd.dashboard.Environment

class Altimeter(implicit env: Environment) extends SvgInstrument[Double] {
  import SvgInstrument._

  val initial = 0.0

  lazy val element = svgObject("altimeter")
  lazy val hand = part("hand")
  lazy val moveable = Seq(hand)

  // 1m === 36deg = 2Pi / 10 ~= 0.62832
  protected def update(altitude: Double) = {
    rotate(hand, (altitude * 0.62832).toInt)
  }
}