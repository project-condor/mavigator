package org.mavlink

trait PayloadReader {
  def nextInt8: Byte
  def nextInt16: Short
  def nextInt32: Int
  def nextInt64: Long
  def nextFloat: Float
  def nextDouble: Double  
  def nextChar: Char
}

trait PayloadWriter {
  def writeInt8(x: Byte)
  def writeInt16(x: Short)
  def writeInt32(x: Int)
  def writeInt64(x: Long)
  def writeFloat(x: Float)
  def writeDouble(x: Double)  
  def writeChar(x: Char)
}