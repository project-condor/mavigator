package mavigator
package util

import org.scalajs.dom.html

/** Represents an application's environment */
trait Environment {
  
  /** The application's root element. */
  def root: html.Element

  /** Retrieve an asset's URL based on its file location. */
  def asset(file: String): String

}

class StaticEnvironment(
  override val root: html.Element,
  assetsBase: String
) extends Environment {

  override def asset(file: String): String = assetsBase + "/" + file

}
