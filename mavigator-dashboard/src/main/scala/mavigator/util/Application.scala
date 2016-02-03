package mavigator
package util

import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js

import org.scalajs.dom.html

trait Application {

  def main(args: Map[String, String])(implicit env: Environment): Unit

  @JSExport
  final def start(settings: js.Dynamic): Unit = {

    val env = new StaticEnvironment(
      root = settings.root.asInstanceOf[html.Element],
      assetsBase = settings.assetsBase.asInstanceOf[String]
    )

    val args = settings.args.asInstanceOf[js.Dictionary[Any]].mapValues(_.toString).toMap

    main(args)(env)
  }

}
