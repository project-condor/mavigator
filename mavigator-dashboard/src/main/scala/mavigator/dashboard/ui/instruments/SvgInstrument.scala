package vfd.dashboard.ui.instruments


import org.scalajs.dom
import org.scalajs.dom.html

import scalatags.JsDom.all._

import vfd.dashboard.Environment

/** An instrument backed by an SVG image. */
trait SvgInstrument[A] extends Instrument[A] {
  
  /** SVG object element that contains the rendered instrument */
  val element: html.Object
  
  /** Retrieves an element of the underlying SVG document by ID. */
  protected def part(id: String) = element.contentDocument.getElementById(id).asInstanceOf[html.Element]
  
  /** Movable parts of the instrument */
  protected def moveable: Seq[html.Element]
  
  /** Called when element has been loaded. */
  protected def load(event: dom.Event): Unit = {
    for (part <- moveable) {
      part.style.transition = "transform 50ms ease-out"
    }
    ready()
  }
  
  element.addEventListener("load", (e: dom.Event) => load(e))
}

/** Contains helpers for SVG instruments. */
object SvgInstrument {

  /** Retrieves an SVG object element by its instrument's name. */
  def svgObject(name: String)(implicit app: Environment): html.Object = {
    val path = app.asset("images/instruments/" + name + ".svg")
    `object`(`type` := "image/svg+xml", "data".attr := path, width := 100.pct)(
      "Error loading instrument " + name).render
  }

  /** Applies translation styling to an element. */
  def translate(elem: html.Element, x: Int, y: Int): Unit = {
    elem.style.transform = "translate(" + x + "px, " + y + "px)";
  }

  /** Applies rotation styling to an element. */
  def rotate(elem: html.Element, rad: Double): Unit = {
    elem.style.transform = "rotateZ(" + rad + "rad)";
  }

}