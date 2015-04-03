package vfd.dashboard.ui.instruments

import org.scalajs.dom.html
import vfd.dashboard.Environment

class Distribution(implicit env: Environment) extends SvgInstrument[(Double, Double, Double, Double)] {
  import SvgInstrument._
  
  val initial = (0.0, 0.0, 0.0, 0.0)
  
  lazy val element = svgObject("distribution")
  lazy val position = part("position")
  lazy val moveable = Seq(position)

  private final val Radius = 50 //px
  
  protected def update(value: (Double, Double, Double, Double)) = {
    val sum = value._1 + value._2 + value._3 + value._4
    val i = (value._1 - value._3) / sum
    val j = (value._2 - value._4) / sum
    val x = math.sqrt(2) / 2 * (i - j)
    val y = math.sqrt(2) / 2 * (-i - j)
    translate(position, (x * Radius).toInt, (y * Radius).toInt)
  }
  
}