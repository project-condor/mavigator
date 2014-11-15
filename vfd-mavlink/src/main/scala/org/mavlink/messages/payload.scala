package org.mavlink.messages

trait PayloadReader {
  def int8: Byte
  def int16: Short
  def int32: Int
  def int64: Long
  def float: Float
  def double: Double
}

trait PayloadWriter {
  def int8(x: Byte)
  def int16(x: Short)
  def int32(x: Int)
  def int64(x: Long)
  def float(x: Float)
  def double(x: Double)
}