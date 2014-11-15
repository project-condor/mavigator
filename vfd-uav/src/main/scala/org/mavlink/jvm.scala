package org.mavlink

import scala.language.implicitConversions

import java.nio.ByteBuffer
import java.nio.ByteOrder

package object messages {
  import org.mavlink.messages.PayloadReader
  import org.mavlink.messages.PayloadWriter

  implicit def mkReader(s: Seq[Byte]) = new BufferedPayloadReader(s.toArray)
  implicit def mkWriter(a: Array[Byte]) = new BufferedPayloadWriter(a)

}

package messages {

  class BufferedPayloadReader(payload: Array[Byte]) extends PayloadReader {
    private val buffer = ByteBuffer.wrap(payload)

    //mavlink uses little endian
    buffer.order(ByteOrder.LITTLE_ENDIAN)

    def int8 = buffer.get()
    def int16 = buffer.getShort()
    def int32 = buffer.getInt()
    def int64 = buffer.getLong()
    def float = buffer.getFloat()
    def double = buffer.getDouble()

  }

  class BufferedPayloadWriter(payload: Array[Byte]) extends PayloadWriter {
    private val buffer = ByteBuffer.wrap(payload)

    //mavlink uses little endian
    buffer.order(ByteOrder.LITTLE_ENDIAN)

    def int8(x: Byte) = buffer.put(x)
    def int16(x: Short) = buffer.putShort(x)
    def int32(x: Int) = buffer.putInt(x)
    def int64(x: Long) = buffer.putLong(x)
    def float(x: Float) = buffer.putFloat(x)
    def double(x: Double) = buffer.putDouble(x)
  }
}

