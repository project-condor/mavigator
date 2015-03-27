package vfd.dashboard.ui.instruments

import org.scalajs.dom
import org.scalajs.dom.html
import vfd.dashboard.Environment

class Generic(
    min: Double,
    med: Double,
    max: Double,
    unit: String)(implicit env: Environment)
  extends SvgInstrument[Double] {

  import SvgInstrument._
  
  val initial = 0.0

  lazy val element = svgObject("generic")
  lazy val handElement = part("hand")
  lazy val unitElement = element.contentDocument.getElementById("unit")
  lazy val valueElement = element.contentDocument.getElementById("value")
  lazy val minElement = element.contentDocument.getElementById("min")
  lazy val medElement = element.contentDocument.getElementById("med")
  lazy val maxElement = element.contentDocument.getElementById("max")
  lazy val moveable = Seq(handElement)

  override protected def load(e: dom.Event) = {
    unitElement.textContent = unit
    minElement.textContent = min.toString
    medElement.textContent = med.toString
    maxElement.textContent = max.toString
    update(min)
    super.load(e)
  }

  protected def update(value: Double) = {
    rotate(handElement, (value * 270 / (max - min)).toInt)
    valueElement.textContent = value.toString
  }
}