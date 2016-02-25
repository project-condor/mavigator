package mavigator.index

import scala.language.implicitConversions

import org.scalajs.dom.html

import rx._

import scala.util.Try
import scala.util.Success
import scala.util.Failure

import scalatags.JsDom.all._

object Util {
  
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
