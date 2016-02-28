package mavigator
package util

import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js

import org.scalajs.dom.console
import org.scalajs.dom.html

trait Application {

  def main(args: Map[String, String])(implicit env: Environment): Unit

  @JSExport
  final def start(settings: js.Dynamic): Unit = {

    console.info("Initializing environment,,,")
    val env = new Environment(
      root = settings.root.asInstanceOf[html.Element],
      styleRoot = settings.styleRoot.asInstanceOf[html.Element],
      baseUrl = settings.baseUrl.asInstanceOf[String]
    )

    console.info("Reading arguments...")
    val args: Map[String, String] =
      settings.args.asInstanceOf[js.Dictionary[Any]].mapValues(_.toString).toMap

    console.info("Entering main...")
    main(args)(env)
  }

}
