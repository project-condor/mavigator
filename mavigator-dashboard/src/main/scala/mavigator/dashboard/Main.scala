package mavigator.dashboard

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

import org.scalajs.dom.html

import mavigator.dashboard.ui.Layout
import mavigator.util.Environment

import scalatags.JsDom.all._

@JSExport("Main")
object Main extends mavigator.util.Application {

  override def main(args: Map[String, String])(implicit env: Environment): Unit = {
    val socket = new MavlinkSocket(args("socketUrl"), args("remoteSystemId").toInt)
    val layout = new Layout(socket)

    env.root.appendChild(layout.element)
  }
}
