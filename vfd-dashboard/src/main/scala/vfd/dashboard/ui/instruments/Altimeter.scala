package vfd.dashboard.ui.instruments

import org.scalajs.dom.html
import vfd.dashboard.Environment

class Altimeter(implicit env: Environment) extends SvgInstrument[Double] {
  import SvgInstrument._

  val initial = 0.0

  lazy val element = svgObject("altimeter")
  lazy val hand = part("hand")
  lazy val moveable = Seq(hand)

  // 36deg === 1m
  protected def update(altitude: Double) = {
    rotate(hand, (altitude * 36).toInt)
  }
}