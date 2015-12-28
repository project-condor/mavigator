package vfd.dashboard.ui.instruments

import org.scalajs.dom.html
import rx._
import vfd.dashboard.Environment

class Compass(val value: Rx[Double])(implicit env: Environment) extends SvgInstrument[Double] {
  import SvgInstrument._
  
  lazy val element = svgObject("compass")
  lazy val plate = part("heading")
  lazy val moveable = Seq(plate)
  
  protected def update(heading: Double) = {
    rotate(plate, heading)
  }
}