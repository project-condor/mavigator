package vfd.dashboard.ui.instruments

import org.scalajs.dom.html
import vfd.dashboard.Environment

class Compass(implicit env: Environment) extends SvgInstrument[Double] {
  import SvgInstrument._
  
  val initial = 0.0
  
  lazy val element = svgObject("compass")
  lazy val plate = part("heading")
  lazy val moveable = Seq(plate)
  
  protected def update(heading: Double) = {
    rotate(plate, heading)
  }
}