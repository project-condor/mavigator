package vfd.frontend

import scala.scalajs.js.annotation.JSExport

import org.scalajs.dom

import vfd.frontend.util.Environment

@JSExport
class Launcher(rootId: String, assetsBase: String) {

  lazy val env = new Environment {
    val root = dom.document.getElementById(rootId)
    def asset(file: String) = assetsBase + "/" + file
  }

  @JSExport
  def main() = {
    import env._

    val args: Seq[(String, String)] = for (
      i <- 0 until root.attributes.length;
      attr = root.attributes.item(i);
      if attr.name.startsWith("data-")
    ) yield {
      attr.name.drop(5) -> attr.value
    }

    while (env.root.hasChildNodes) {
      env.root.removeChild(env.root.firstChild)
    }
    
    Main.main(args.toMap)(env)
  }

}