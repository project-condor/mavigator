package vfd.dashboard

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

import org.scalajs.dom.html

import vfd.dashboard.ui.Layout

@JSExport("Main")
object Main {

  @JSExport
  def main(rootElement: html.Element, assetsBase: String, args: js.Dictionary[String]) = {
    implicit val env = new Environment {
       def root = rootElement
       def asset(file: String) = assetsBase + "/" + file
    }
    
    val socket = new MavlinkSocket(args("socketUrl"), args("remoteSystemId").toInt)
    val layout = new Layout(socket)

    env.root.appendChild(layout.element)
  }
}