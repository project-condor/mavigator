package org.mavlink

import scala.language.implicitConversions
import scala.scalajs.js.JSConverters.array2JSRichGenTrav
import scala.scalajs.js.typedarray.DataView
import scala.scalajs.js.typedarray.Int8Array

import org.mavlink.messages.PayloadReader
import org.mavlink.messages.PayloadWriter

package object messages {
  import org.mavlink.messages.PayloadReader
  import org.mavlink.messages.PayloadWriter

  implicit def mkReader(s: Seq[Byte]) = new BufferedPayloadReader(s.toArray)
  implicit def mkWriter(a: Array[Byte]) = new BufferedPayloadWriter(a)

}

package messages {

  class BufferedPayloadReader(payload: Array[Byte]) extends PayloadReader {
    private val buffer = new Int8Array(payload.toJSArray)
    private val view = new DataView(buffer.buffer)
    private var pos = 0

    def int8 = {
      val r = view.getInt8(pos)
      pos += 1
      r
    }
    def int16 = {
      val r = view.getInt16(pos, true)
      pos += 2
      r
    }
    def int32 = {
      val r = view.getInt32(pos, true)
      pos += 4
      r
    }
    def int64 = {
      val l = int32
      val m = int32
      (m.asInstanceOf[Long] << 32) | l.asInstanceOf[Long]
    }
    def float = {
      val r = view.getFloat32(pos, true)
      pos += 4
      r
    }
    def double = {
      val r = view.getFloat64(pos, true)
      pos += 8
      r
    }

  }

  class BufferedPayloadWriter(payload: Array[Byte]) extends PayloadWriter {
    def int8(x: Byte) = ???
    def int16(x: Short) = ???
    def int32(x: Int) = ???
    def int64(x: Long) = ???
    def float(x: Float) = ???
    def double(x: Double) = ???
  }
}

