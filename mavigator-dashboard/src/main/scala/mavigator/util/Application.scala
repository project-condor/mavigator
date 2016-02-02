package mavigator
package util

import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js

import org.scalajs.dom.html

trait Application {

  def main(env: Environment, args: Map[String, String]): Unit

  @JSExport
  final def _start(settings: js.Dynamic): Unit = {

    val env = new StaticEnvironment(
      root = settings.root.asInstanceOf[html.Element],
      assetsBase = settings.root.asInstanceOf[String]
    )

    val args = settings.args.asInstanceOf[Map[String, String]]

    main(env, args)
  }

}
