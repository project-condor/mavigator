

import scala.scalajs.js
import js.annotation.JSExport
import org.scalajs.dom

@JSExport
class Frontend(attitudeSelector: String, azimuthSelector: String) {

  val svg = dom.document.querySelector(attitudeSelector).asInstanceOf[dom.HTMLObjectElement]
  val svg2 = dom.document.querySelector(azimuthSelector).asInstanceOf[dom.HTMLObjectElement]
  var svgDoc = svg.contentDocument;
  var svgDoc2 = svg2.contentDocument;

  @JSExport
  def main() = {
    dom.setInterval(() => foo, 50)
  }

  var a = 0.0
  var r = 0.0
  var p = 0.0
  def foo() = {
    a += 3
    r += 0.05
    p += 0.1
    var roll = svgDoc.getElementById("roll");
    var pitch = svgDoc.getElementById("pitch");
    pitch.setAttribute("transform", "translate(0, " + 30 * math.sin(p) + ")");
    roll.setAttribute("transform", "rotate(" + 60 * math.sin(r) + ")");

    var azimuth = svgDoc2.getElementById("heading");
    azimuth.setAttribute("transform", "rotate(" + a + ")");
  }

}