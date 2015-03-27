package vfd.dashboard.ui.instruments

import org.scalajs.dom.html
import vfd.dashboard.Environment

class Balance(implicit env: Environment) extends SvgInstrument[(Double, Double, Double, Double)] {
  import SvgInstrument._
  
  val initial = (0.0, 0.0, 0.0, 0.0)
  
  lazy val element = svgObject("balance")
  lazy val position = part("position")
  lazy val moveable = Seq(position)
  
  protected def update(value: (Double, Double, Double, Double)) = {
    val m0 = value._1
    val m1 = value._2
    val m2 = value._3
    val m3 = value._4
    val s = m0 + m1 + m2 + m3
    val i = (m0 - m2) / s
    val j = (m1 - m3) / s
    val x = 0.5 * (i - j)
    val y = 0.5 * (-i - j)
    translate(position, (x * 50).toInt, (y * 50).toInt)
  }
}