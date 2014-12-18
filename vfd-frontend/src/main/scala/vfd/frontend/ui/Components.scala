package vfd.frontend.ui

import org.scalajs.dom.HTMLElement
import rx.Obs
import rx.Rx
import rx.Rx
import scalatags.JsDom.all.ExtendedString
import scalatags.JsDom.all.bindNode
import scalatags.JsDom.all.div
import scalatags.JsDom.all.`object`
import scalatags.JsDom.all.stringAttr
import scalatags.JsDom.all.stringFrag
import scalatags.JsDom.all.stringStyle
import scalatags.JsDom.all.style
import scalatags.JsDom.all.`type`
import scalatags.JsDom.all.width
import vfd.frontend.util.Environment

object Components {

  private def instrument(name: String)(implicit app: Environment) = {
    val path = app.asset("images/instruments/" + name + ".svg")
    `object`(`type` := "image/svg+xml", "data".attr := path, width := "100%")(
      "Error loading image " + name).render
  }

  private def frame(elem: HTMLElement, size: String) = {
    div(style := s"width: $size; height: $size; display: inline-block;")(
      elem)
  }
  
  def led(color: Rx[String], size: String)(implicit app: Environment) = {
    val elem = `object`(`type` := "image/svg+xml", "data".attr := app.asset("leds/led.svg"), width := size)(
      "Error loading image.").render

    Obs(color, skipInitial = true) {
      val svg = elem.contentDocument
      svg.getElementById("light").setAttribute("fill", color())
    }
    elem
  }

  def horizon(pitchRoll: Rx[(Double, Double)], size: String)(implicit app: Environment) = {
    val inst = instrument("horizon")
    Obs(pitchRoll, skipInitial = true) {
      val svg = inst.contentDocument
      val pitch = svg.getElementById("pitch")
      val roll = svg.getElementById("roll")
      pitch.style.transition = "transform 250ms ease-out"
      roll.style.transition = "transform 250ms ease-out"
      pitch.style.transform = "translate(0px, " + pitchRoll()._1 + "px)"
      roll.style.transform = "rotate(" + pitchRoll()._2 + "deg)"
    }
    frame(inst, size)
  }

  def altimeter(value: Rx[Double], size: String)(implicit app: Environment) = {
    val inst = instrument("altimeter")
    Obs(value, skipInitial = true) {
      val svg = inst.contentDocument
      // 36deg === 1m
      svg.getElementById("hand").setAttribute("transform", "rotate(" + value() * 36 + ")");
    }
    frame(inst, size)
  }

  def compass(value: Rx[Double], size: String)(implicit app: Environment) = {
    val inst = instrument("compass")
    Obs(value, skipInitial = true) {
      val svg = inst.contentDocument
      val heading = svg.getElementById("heading")
      heading.style.transition = "transform 250ms ease-out"
      heading.style.transform = "rotate(" + value() + "deg)"
    }
    frame(inst, size)
  }
  
  def basic(value: Rx[Double], size: String)(implicit app: Environment) = {
    val inst = instrument("basic")
    Obs(value, skipInitial = true) {
      val svg = inst.contentDocument
      val hand = svg.getElementById("hand")
      hand.style.transform = "rotate(" + value() * 270 / 100 + "deg)";
      hand.style.transition = "transform 250ms ease-out"
      svg.getElementById("unit").textContent = "%"
      svg.getElementById("value").textContent = value().toString
    }
    frame(inst, size)
  }
  
  def bar(value: Rx[Double], size: String)(implicit app: Environment) = {
    val inst = instrument("bar")
    Obs(value, skipInitial = true) {
      val svg = inst.contentDocument
      val level = svg.getElementById("level")
      level.style.transform = "translate(0px, " + 97 * (1 - value() / 100) + "px)";
      level.style.transition = "transform 250ms ease-out"
      svg.getElementById("unit").textContent = "%"
      svg.getElementById("value").textContent = value().toString
    }
    frame(inst, size)
  }

}

