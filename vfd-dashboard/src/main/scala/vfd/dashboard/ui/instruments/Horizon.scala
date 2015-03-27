package vfd.dashboard.ui.instruments

import org.scalajs.dom.html
import vfd.dashboard.Environment

class Horizon(implicit env: Environment) extends SvgInstrument[(Double, Double)] {
  import SvgInstrument._
  
  val initial = (0.0, 0.0)
  
  lazy val element = svgObject("horizon")
  lazy val pitch = part("pitch")
  lazy val roll = part("roll")
  lazy val moveable = Seq(pitch, roll)
  
  protected def update(pitchRoll: (Double, Double)) = {
    translate(pitch, 0, pitchRoll._1.toInt)
    rotate(roll, pitchRoll._2.toInt)
  }
}
