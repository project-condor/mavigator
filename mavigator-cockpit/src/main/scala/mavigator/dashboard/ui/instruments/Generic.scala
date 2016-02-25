package mavigator.dashboard.ui.instruments

import org.scalajs.dom
import org.scalajs.dom.html
import rx._
import mavigator.util.Environment

class Generic(
    min: Double,
    med: Double,
    max: Double,
    unit: String,
    val value: Rx[Double])
    (implicit env: Environment)
  extends SvgInstrument[Double] {

  import SvgInstrument._

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
    super.load(e)
  }

  protected def update(value: Double) = {
    rotate(handElement, value / (max - min) * math.Pi * 3 / 2)
    valueElement.textContent = value.toString
  }
}
