package vfd.dashboard.ui.instruments

import scalatags.JsDom.all._
import vfd.dashboard.Environment

class Led(implicit env: Environment) extends SvgInstrument[String] {

  val initial = "none"

  lazy val element = `object`(`type` := "image/svg+xml", "data".attr := env.asset("images/leds/led.svg"), width := 100.pct)(
    "Error loading led.").render
  protected def moveable = Seq()

  protected def update(color: String) = {
    part("light").setAttribute("fill", color)
  }

}