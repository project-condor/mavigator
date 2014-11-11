package org

import scala.language.implicitConversions

package object mavlink {
  
  implicit def mkReader(bytes: Seq[Byte]): PayloadReader = new BufferedPayloadReader(bytes.toArray)

}