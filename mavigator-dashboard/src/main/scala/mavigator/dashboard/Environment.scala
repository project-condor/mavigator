package vfd.dashboard

import org.scalajs.dom.html

/** Represents an application's environment */
trait Environment {
  
  /** The application's root element. */
  def root: html.Element

  /** Retrieve an asset's URL based on its file location. */
  def asset(file: String): String

}