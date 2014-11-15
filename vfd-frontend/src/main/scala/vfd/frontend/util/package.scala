package vfd.frontend

import scala.reflect.ClassTag

import rx.Rx
import rx.Rx
import rx.ops.RxOps

package object util {

  implicit class richRx[A](val input: Rx[A]) extends AnyVal {
    def only[B <: A](implicit ct: ClassTag[B]): Rx[B] = input filter (_.isInstanceOf[B]) map (_.asInstanceOf[B])
  }

}