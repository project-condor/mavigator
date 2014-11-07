package org.mavlink

import java.nio.ByteBuffer
import java.nio.ByteOrder

class BufferedPayloadReader(payload: Array[Byte]) extends PayloadReader {
  private val buffer = ByteBuffer.wrap(payload)

  //mavlink uses little endian
  buffer.order(ByteOrder.LITTLE_ENDIAN)

  def nextInt8 = buffer.get()
  def nextInt16 = buffer.getShort()
  def nextInt32 = buffer.getInt()
  def nextInt64 = buffer.getLong()
  def nextFloat = buffer.getFloat()
  def nextDouble = buffer.getDouble()
  def nextChar = buffer.getChar()

}

class BufferedPayloadBuilder(payload: Array[Byte]) extends PayloadBuilder {
  private val buffer = ByteBuffer.wrap(payload)

  //mavlink uses little endian
  buffer.order(ByteOrder.LITTLE_ENDIAN)

  def writeInt8(x: Byte) = buffer.put(x)
  def writeInt16(x: Short) = buffer.putShort(x)
  def writeInt32(x: Int) = buffer.putInt(x)
  def writeInt64(x: Long) = buffer.putLong(x)
  def writeFloat(x: Float) = buffer.putFloat(x)
  def writeDouble(x: Double) = buffer.putDouble(x)
  def writeChar(x: Char) = buffer.putChar(x)

}