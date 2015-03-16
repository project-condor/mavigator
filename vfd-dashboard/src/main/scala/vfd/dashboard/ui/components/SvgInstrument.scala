package vfd.dashboard.ui.components

import scala.scalajs.js.Any.fromFunction1

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

trait SvgInstrument[A] {
  
  /** SVG object element that contains the rendered instrument */
  def element: html.Object
  
  /** Actual svg document */
  protected def content = element.contentDocument
  
  /** Moveable parts of the instrument */
  protected def moveable: Seq[html.Element]
  
  /** Updates the instrument to show a new value */
  def update(value: A): Unit
  
  protected def load(event: dom.Event): Unit = {
    for (part <- moveable) {
      part.style.transition = "transform 250ms ease-out"
    }
  }
  
  element.addEventListener("load", (e: dom.Event) => load(e))
}

object SvgInstrument {

  def svg(name: String)(implicit app: Environment): html.Object = {
    val path = app.asset("images/instruments/" + name + ".svg")
    `object`(`type` := "image/svg+xml", "data".attr := path, width := 100.pct)(
      "Error loading instrument " + name).render
  }

  def translate(elem: html.Element, x: Int, y: Int): Unit = {
    elem.style.transform = "translate(" + x + "px, " + y + "px)";
  }

  def rotate(elem: html.Element, deg: Int): Unit = {
    elem.style.transform = "rotateZ(" + deg + "deg)";
  }

}