package vfd.frontend

import scala.scalajs.js.annotation.JSExport

import org.scalajs.dom

import vfd.frontend.util.Application

@JSExport
class Launcher {

  @JSExport
  def launch(rootId: String, assetsBase: String, mavlinkSocketUrl: String) = {
    val root = dom.document.getElementById(rootId)
    val app = new Application(root, assetsBase)
    val frontend = new Main(mavlinkSocketUrl)(app)

    while (root.hasChildNodes) {
      root.removeChild(root.firstChild)
    }
    frontend.main()
  }

}