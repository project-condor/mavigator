package vfd.dashboard.ui.instruments

import rx._
import org.scalajs.dom.html

/** Common trait to all flight instruments. */
trait Instrument[A] {

  /** Initial value. */
  val initial: A

  /** Current value that is displayed in the instrument. */
  val value: Var[A] = Var(initial)

  /** HTML element that contains the rendered instrument */
  val element: html.Element

  /** Performs the actual UI update of this instrument. */
  protected def update(newValue: A): Unit

  /** Call when instrument has finished setting up its UI. */
  protected def ready() = {
    Obs(value, skipInitial = true) {
      update(value())
    }
  }

}