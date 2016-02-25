package mavigator.dashboard.ui.instruments

import org.scalajs.dom
import rx._
import scala.scalajs.js.Date
import scalatags.JsDom.all._

class Clock extends Instrument[Date] {
  
  def format(date: Date) = date.toLocaleTimeString()
  
  val value = Var(new Date)
  
  val element = span(format(value())).render
  
  protected def update(value: Date) = {
    element.innerHTML = format(value)
  }
  
  dom.setInterval(() => {value() = new Date}, 1000)
  ready()

}
