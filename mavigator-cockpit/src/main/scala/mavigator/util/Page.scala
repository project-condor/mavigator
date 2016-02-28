package mavigator
package util

import org.scalajs.dom
import org.scalajs.dom.html

/** Contents of a single-page app, consisting of styles and a main element. */
trait Page {

  private var isAttached = false
  private def attached[A](action: => A) = if (!isAttached) {
    sys.error("Page has not been attached to an environment yet.")
  } else {
    action
  }

  private var baseUrl: String = null

  def asset(path: String): String = attached { baseUrl + "/" + path }

  def styles: Seq[String]

  def elements: Seq[html.Element]

  /** Attach this page to a website. */
  def attach(env: Environment): Unit = {
    baseUrl = env.baseUrl
    isAttached = true

    val styleText = dom.document.createTextNode(styles.reduce(_ + _))
    env.styleRoot.appendChild(styleText)

    for (elem <- elements) {
      env.root.appendChild(elem)
    }
  }

}
