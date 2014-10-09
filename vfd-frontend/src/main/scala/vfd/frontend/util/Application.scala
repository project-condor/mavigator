package vfd.frontend.util

import org.scalajs.dom.HTMLElement

class Application(element: HTMLElement, assetsBase: String) {

  def root = element

  def asset(file: String): String = assetsBase + "/" + file

}