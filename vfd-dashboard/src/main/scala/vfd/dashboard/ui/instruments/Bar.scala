package vfd.dashboard.ui.instruments

import org.scalajs.dom.html
import rx._
import vfd.dashboard.Environment

class Bar(val value: Rx[Double])(implicit env: Environment) extends SvgInstrument[Double] {
  import SvgInstrument._
  
  lazy val element = svgObject("bar")
  lazy val level = part("level")
  lazy val moveable = Seq(level)
  
  protected def update(value: Double) = {
    translate(level, 0, (97 * (1 - value / 100)).toInt)
  }
  
}