package vfd.frontend.ui.components

import scala.scalajs.js.Any.fromFunction1

import org.scalajs.dom

import scalatags.JsDom.all.ExtendedString
import scalatags.JsDom.all.`object`
import scalatags.JsDom.all.stringAttr
import scalatags.JsDom.all.stringFrag
import scalatags.JsDom.all.stringStyle
import scalatags.JsDom.all.`type`
import scalatags.JsDom.all.width
import vfd.frontend.Environment

trait SvgInstrument[A] {
  
  /** SVG object element that contains the rendered instrument */
  def element: dom.HTMLObjectElement
  
  /** Actual svg document */
  protected def content: dom.Document = element.contentDocument
  
  /** Moveable parts of the instrument */
  protected def moveable: Seq[dom.HTMLElement]
  
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

  def svg(name: String)(implicit app: Environment): dom.HTMLObjectElement = {
    val path = app.asset("images/instruments/" + name + ".svg")
    `object`(`type` := "image/svg+xml", "data".attr := path, width := "100%")(
      "Error loading instrument " + name).render
  }

  def translate(elem: dom.HTMLElement, x: Int, y: Int): Unit = {
    elem.style.transform = "translate(" + x + "px, " + y + "px)";
  }

  def rotate(elem: dom.HTMLElement, deg: Int): Unit = {
    elem.style.transform = "rotateZ(" + deg + "deg)";
  }

}