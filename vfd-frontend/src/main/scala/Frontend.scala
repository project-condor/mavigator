

import scala.scalajs.js
import js.annotation.JSExport
import org.scalajs.dom

class Data extends js.Object {
  def roll: Double = ???
  def pitch: Double = ???
  def heading: Double = ???
  def altitude: Double = ???
  def temperature: Double = ???
}
@JSExport
class Frontend(attitudeSelector: String, azimuthSelector: String, altitudeSelector: String) {

  val svg = dom.document.querySelector(attitudeSelector).asInstanceOf[dom.HTMLObjectElement]
  val svg2 = dom.document.querySelector(azimuthSelector).asInstanceOf[dom.HTMLObjectElement]
  var svgDoc = svg.contentDocument;
  var svgDoc2 = svg2.contentDocument;
  val svg3 = dom.document.querySelector(altitudeSelector).asInstanceOf[dom.HTMLObjectElement]
  var svgDoc3 = svg3.contentDocument;

  @JSExport
  def main() = {
    var roll = svgDoc.getElementById("roll");
    var pitch = svgDoc.getElementById("pitch");
    var heading = svgDoc2.getElementById("heading");
    var altitude = svgDoc3.getElementById("hand")
    
    val connection = new dom.WebSocket("ws://localhost:9000/socket");

    connection.onmessage = (e: dom.MessageEvent) => {
      Console.println(e.data);
      val data = js.JSON.parse(e.data.asInstanceOf[String]).asInstanceOf[Data]
      
      roll.setAttribute("transform", "rotate(" + data.roll.toDouble + ")");
      pitch.setAttribute("transform", "translate(0, " + data.pitch.toDouble + ")");
      heading.setAttribute("transform", "rotate(" +  data.heading.toDouble + ")");
      altitude.setAttribute("transform", "rotate(" +  data.altitude.toDouble * 36 + ")")
    }
  }

}