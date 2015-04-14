package vfd.dashboard

import rx._

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

}