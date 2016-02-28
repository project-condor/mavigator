package mavigator
package cockpit

import scala.scalajs.js.annotation.JSExport

import mavigator.util.{Environment, Application}
import scalatags.JsDom.all._
import util._

class Cockpit
    extends Page
    with Layout
    with MavlinkWebSockets
    with Instruments

@JSExport("mavigator_cockpit_Main")
object Cockpit extends Application {

  override def main(args: Map[String, String])(implicit env: Environment): Unit = { 
    val app = new Cockpit
    app.attach(env)
    app.connect(args("socketUrl"), args("remoteSystemId").toInt)
  }

}

