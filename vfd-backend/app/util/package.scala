

import org.scalajs.spickling._
import org.scalajs.spickling.playjson._
import play.api.data.validation.ValidationError
import play.api.libs.json._


package object util {

  def spicklerFormat[A](implicit manifest: Manifest[A], pickler: Pickler[A], unpickler: Unpickler[A]) = new Format[A] {
    PicklerRegistry.register[A]

    def writes(o: A): JsValue = PicklerRegistry.pickle(o)

    def reads(j: JsValue): JsResult[A] = PicklerRegistry.unpickle(j) match {
      case a: A => JsSuccess(a)
      case _ => JsError("unpickling yielded wrong type")
    }
  }
  
}