package mavigator
package util

import org.scalajs.dom
import org.scalajs.dom.html

/** Represents an application's environment
  * @param root The application's root element.
  * @param styleRoot An html 'style' tag to which app-specific styles are appended.
  * @param baseUrl Base URL. */
case class Environment(
  root: html.Element,
  styleRoot: html.Element,
  baseUrl: String
)
