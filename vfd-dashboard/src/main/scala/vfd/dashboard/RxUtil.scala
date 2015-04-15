package vfd.dashboard

import scala.language.implicitConversions
import scala.util.Failure
import scala.util.Success

import org.scalajs.dom.html

import rx.Obs
import rx.Rx
import rx.Rx
import rx.Var
import rx.Var
import scalatags.JsDom.all.Frag
import scalatags.JsDom.all.HtmlTag
import scalatags.JsDom.all.backgroundColor
import scalatags.JsDom.all.bindNode
import scalatags.JsDom.all.span
import scalatags.JsDom.all.stringFrag
import scalatags.JsDom.all.stringStyle

package object rxutil {

  /** Rx, implicitly enhanced with additional methods. */
  implicit class RichRx(val rx: Rx[_]) extends AnyVal {

    /**
     * Builds a new Rx by applying a partial function to all values of
     * this Rx on which the function is defined.
     * @param initial initial value of the returned Rx
     * @param pf the partial function which filters and maps this Rx
     * @return a new Rx resulting from applying the given partial
     * function pf to each value on which it is defined and collecting
     * the result
     */
    def collect[B](initial: B)(pf: PartialFunction[Any, B]): Rx[B] = {
      val result: Var[B] = Var(initial)
      Obs(rx, skipInitial = true) {
        if (pf.isDefinedAt(rx())) {
          result() = pf(rx())
        }
      }
      result
    }

  }

  /**
   * Copied from https://github.com/lihaoyi/workbench-example-app/blob/todomvc/src/main/scala/example/Framework.scala
   *
   * Sticks some Rx into a Scalatags fragment, which means hooking up an Obs
   * to propagate changes into the DOM via the element's ID. Monkey-patches
   * the Obs onto the element itself so we have a reference to kill it when
   * the element leaves the DOM (e.g. it gets deleted).
   */
  implicit def rxMod[T <: html.Element](r: Rx[HtmlTag]): Frag = {
    def rSafe = r.toTry match {
      case Success(v) => v.render
      case Failure(e) => span(e.toString, backgroundColor := "red").render
    }
    var last = rSafe
    Obs(r, skipInitial = true) {
      val newLast = rSafe
      last.parentElement.replaceChild(newLast, last)
      last = newLast
    }
    bindNode(last)
  }

}