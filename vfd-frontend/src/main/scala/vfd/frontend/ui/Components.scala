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
import vfd.frontend.util.Application

object Components {

  def led(color: Rx[String], size: String)(implicit app: Application) = {
    val elem = `object`(`type` := "image/svg+xml", "data".attr := app.asset("leds/led.svg"), width := size)(
      "Error loading image.").render

    Obs(color, skipInitial = true) {
      val svg = elem.contentDocument
      svg.getElementById("light").setAttribute("fill", color())
    }
    elem
  }

  private def instrument(name: String)(implicit app: Application) = {
    val path = app.asset("images/instruments/" + name + ".svg")
    `object`(`type` := "image/svg+xml", "data".attr := path, width := "100%")(
      "Error loading image " + name).render
  }

  private def frame(elem: HTMLElement, size: String) = {
    div(style := s"width: $size; height: $size; display: inline-block;")(
      elem)
  }

  def attitude(pitchRoll: Rx[(Double, Double)], size: String)(implicit app: Application) = {
    val inst = instrument("attitude")
    Obs(pitchRoll, skipInitial = true) {
      val svg = inst.contentDocument
      val pitch = svg.getElementById("pitch")
      val roll = svg.getElementById("roll")
      pitch.setAttribute("transform", "translate(0, " + pitchRoll()._1 / math.Pi * 180 + ")");
      roll.setAttribute("transform", "rotate(" + pitchRoll()._2 / math.Pi * 180 + ")");
    }
    frame(inst, size)
  }

  def altitude(value: Rx[Double], size: String)(implicit app: Application) = {
    val inst = instrument("altitude")
    Obs(value, skipInitial = true) {
      val svg = inst.contentDocument
      // 36deg === 1m
      svg.getElementById("hand").setAttribute("transform", "rotate(" + value() * 36 + ")");
    }
    frame(inst, size)
  }

  def heading(value: Rx[Double], size: String)(implicit app: Application) = {
    val inst = instrument("heading")
    Obs(value, skipInitial = true) {
      val svg = inst.contentDocument
      // 1deg === 1deg
      svg.getElementById("heading").setAttribute("transform", "rotate(" + value() / math.Pi * 180 + ")");
    }
    frame(inst, size)
  }

}