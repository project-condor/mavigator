package vfd.dashboard.ui.instruments

import org.scalajs.dom.html
import vfd.dashboard.Environment

class Bar(implicit env: Environment) extends SvgInstrument[Double] {
  import SvgInstrument._
  
  val initial = 0.0
  
  lazy val element = svgObject("bar")
  lazy val level = part("level")
  lazy val moveable = Seq(level)
  
  protected def update(value: Double) = {
    translate(level, 0, (97 * (1 - value / 100)).toInt)
  }
  
}