package vfd.frontend.util

import org.scalajs.dom.HTMLElement

/** Represents an application's environment */
trait Environment {
  
  /** The application's root element. */
  def root: HTMLElement

  /** Retrieve an asset's URL based on its file location. */
  def asset(file: String): String

}