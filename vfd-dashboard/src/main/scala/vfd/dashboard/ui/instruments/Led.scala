package vfd.dashboard.ui.instruments

import rx._
import scalatags.JsDom.all._
import vfd.dashboard.Environment

class Led(val value: Rx[String])(implicit env: Environment) extends SvgInstrument[String] {

  lazy val element = `object`(`type` := "image/svg+xml", "data".attr := env.asset("images/leds/led.svg"), width := 100.pct)(
    "Error loading led.").render
  protected def moveable = Seq()

  protected def update(color: String) = {
    part("light").setAttribute("fill", color)
  }

}