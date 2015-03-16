package vfd.dashboard.ui.components

import org.scalajs.dom
import org.scalajs.dom.html

import scalatags.JsDom.all.ExtendedString
import scalatags.JsDom.all.Int2CssNumber
import scalatags.JsDom.all.`object`
import scalatags.JsDom.all.stringAttr
import scalatags.JsDom.all.stringFrag
import scalatags.JsDom.all.stringPixelStyle
import scalatags.JsDom.all.`type`
import scalatags.JsDom.all.width
import vfd.dashboard.Environment

class Led(implicit env: Environment) extends SvgInstrument[String] {
  lazy val element = `object`(`type` := "image/svg+xml", "data".attr := env.asset("images/leds/led.svg"), width := 100.pct)(
      "Error loading led.").render
  
  def update(color: String) = {
    content.getElementById("light").setAttribute("fill", color)
  }
  
  protected def moveable = Seq()
  
}

class Horizon(implicit env: Environment) extends SvgInstrument[(Double, Double)] {
  lazy val element = SvgInstrument.svg("horizon")
  
  def pitch = content.getElementById("pitch").asInstanceOf[html.Element]
  def roll = content.getElementById("roll").asInstanceOf[html.Element]
  protected def moveable = Seq(pitch, roll)
  
  def update(pitchRoll: (Double, Double)) = {
    SvgInstrument.translate(pitch, 0, pitchRoll._1.toInt)
    SvgInstrument.rotate(roll, pitchRoll._2.toInt)
  }
}

class Altimeter(implicit env: Environment) extends SvgInstrument[Double] {
  lazy val element = SvgInstrument.svg("altimeter")
  
  def hand = content.getElementById("hand").asInstanceOf[html.Element]
  protected def moveable = Seq(hand)
  
  // 36deg === 1m
  def update(altitude: Double) = {
    SvgInstrument.rotate(hand, (altitude * 36).toInt)
  }
}

class Compass(implicit env: Environment) extends SvgInstrument[Double] {
  lazy val element = SvgInstrument.svg("compass")
  
  def plate = content.getElementById("heading").asInstanceOf[html.Element]
  protected def moveable = Seq(plate)
  
  def update(heading: Double) = {
    SvgInstrument.rotate(plate, heading.toInt)
  }
}

class Generic(
    min: Double,
    med: Double,
    max: Double,
    unit: String)(implicit env: Environment) extends SvgInstrument[Double] {
  
  lazy val element = SvgInstrument.svg("generic")
  
  def handElement = content.getElementById("hand").asInstanceOf[html.Element]
  def unitElement = content.getElementById("unit")
  def valueElement = content.getElementById("value")
  def minElement = content.getElementById("min")
  def medElement = content.getElementById("med")
  def maxElement = content.getElementById("max")
  protected def moveable = Seq(handElement)
  
  override protected def load(e: dom.Event) = {
    unitElement.textContent = unit
    minElement.textContent = min.toString
    medElement.textContent = med.toString
    maxElement.textContent = max.toString
    update(min)
    super.load(e)
  }

  def update(value: Double) = {
    SvgInstrument.rotate(handElement, (value * 270 / (max - min)).toInt)
    valueElement.textContent = value.toString
  }
}
 
class Bar(implicit env: Environment) extends SvgInstrument[Double] {
  
  lazy val element = SvgInstrument.svg("bar")
  
  def level = content.getElementById("level").asInstanceOf[html.Element]
  protected def moveable = Seq(level)
  
  def update(value: Double) = {
    SvgInstrument.translate(level, 0, (97 * (1 - value / 100)).toInt)
  }
  
}

class Balance(implicit env: Environment) extends SvgInstrument[(Double, Double, Double, Double)] {
  lazy val element = SvgInstrument.svg("balance")
  
  def position = content.getElementById("position").asInstanceOf[html.Element]
  protected def moveable = Seq(position)
  
  def update(value: (Double, Double, Double, Double)) = {
    val m0 = value._1
    val m1 = value._2
    val m2 = value._3
    val m3 = value._4
    val s = m0 + m1 + m2 + m3
    val i = (m0 - m2) / s
    val j = (m1 - m3) / s
    val x = 0.5 * (i - j)
    val y = 0.5 * (-i - j)
    SvgInstrument.translate(position, (x * 50).toInt, (y * 50).toInt)
  }
}