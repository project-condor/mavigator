

import scala.scalajs.js
import js.annotation.JSExport
import org.scalajs.dom
import org.scalajs.spickling._
import org.scalajs.spickling.jsany._

@JSExport
class Frontend {
  PicklerRegistry.register[vfd.uav.DataFrame]

  lazy val attitude = dom.document.getElementById("attitude").asInstanceOf[dom.HTMLObjectElement].contentDocument
  lazy val heading = dom.document.getElementById("heading").asInstanceOf[dom.HTMLObjectElement].contentDocument
  lazy val altitude = dom.document.getElementById("altitude").asInstanceOf[dom.HTMLObjectElement].contentDocument

  @JSExport
  def main() = {
    var roll = attitude.getElementById("roll");
    var pitch = attitude.getElementById("pitch");
    var heading = this.heading.getElementById("heading");
    var altitude = this.altitude.getElementById("hand")
    
    val connection = new dom.WebSocket("ws://localhost:9000/socket");

    connection.onmessage = (e: dom.MessageEvent) => {
     
      val data = PicklerRegistry.unpickle(js.JSON.parse(e.data.asInstanceOf[String]).asInstanceOf[js.Any] ).asInstanceOf[vfd.uav.DataFrame]  
      //Console.println(d.roll)
   
      
      
      
      roll.setAttribute("transform", "rotate(" + data.roll.toDouble + ")");
      pitch.setAttribute("transform", "translate(0, " + data.pitch.toDouble + ")");
      heading.setAttribute("transform", "rotate(" +  data.heading.toDouble + ")");
      altitude.setAttribute("transform", "rotate(" +  data.altitude.toDouble * 36 + ")")
    }
  }

}