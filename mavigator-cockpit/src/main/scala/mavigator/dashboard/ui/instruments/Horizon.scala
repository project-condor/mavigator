package mavigator.dashboard.ui.instruments

import org.scalajs.dom.html
import rx._
import mavigator.util.Environment

class Horizon(val value: Rx[(Double, Double)])(implicit env: Environment) extends SvgInstrument[(Double, Double)] {
  import SvgInstrument._
  
  lazy val element = svgObject("horizon")
  lazy val pitch = part("pitch")
  lazy val roll = part("roll")
  lazy val moveable = Seq(pitch, roll)
  
  protected def update(pitchRoll: (Double, Double)) = {
    translate(pitch, 0, (pitchRoll._1 * 180 / math.Pi).toInt) // 1deg === 1px
    rotate(roll, pitchRoll._2)
  }
}
