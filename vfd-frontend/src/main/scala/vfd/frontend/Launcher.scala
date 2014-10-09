package vfd.frontend

import scala.scalajs.js
import js.annotation.JSExport

import org.scalajs.dom

import vfd.frontend.util.Application

@JSExport
class Launcher {

  @JSExport
  def launch(rootId: String, assetsBase: String, socketUrl: String) = {
    val root = dom.document.getElementById(rootId)
    val app = new Application(root, assetsBase)
    val frontend = new Frontend(socketUrl)(app)

    while(root.hasChildNodes) {
      root.removeChild(root.firstChild)
    }
    frontend.main()
  }

}