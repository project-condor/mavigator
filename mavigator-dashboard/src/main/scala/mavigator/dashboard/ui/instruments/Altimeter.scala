package mavigator.dashboard.ui.instruments

import mavigator.util.Environment

import org.scalajs.dom.html
import rx._

class Altimeter(val value: Rx[Double])(implicit env: Environment) extends SvgInstrument[Double] {
  import SvgInstrument._

  lazy val element = svgObject("altimeter")
  lazy val hand = part("hand")
  lazy val moveable = Seq(hand)

  // 1m === 36deg = 2Pi / 10 ~= 0.62832
  protected def update(altitude: Double) = {
    rotate(hand, (altitude * 0.62832).toInt)
  }
}
