package mavigator.dashboard

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

import org.scalajs.dom.html

import mavigator.dashboard.ui.Layout
import mavigator.util.Environment
import mavigator.util.Application

import scalatags.JsDom.all._

@JSExport("mavigator_dashboard_Main")
object Main extends Application {

  override def main(args: Map[String, String])(implicit env: Environment): Unit = {
    val socket = new MavlinkSocket(args("socketUrl"), args("remoteSystemId").toInt)
    val layout = new Layout(socket)

    env.root.appendChild(layout.element)
  }
}
