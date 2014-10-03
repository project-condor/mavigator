package vfd.uav

import scala.collection.mutable.ArrayBuffer

class Framer {
  final val MTU: Int = 1024

  final val START: Byte = 0xfd.toByte
  final val STOP: Byte = 0xfe.toByte
  final val ESCAPE: Byte = 0xff.toByte

  final val WAITING = 0
  final val RECEIVING = 1
  final val ESCAPING = 2

  private val data = new Array[Byte](MTU)
  private var index = 0
  private var state = WAITING

  private def add(byte: Byte): Unit = {
    data(index) = byte
    index += 1
    if (index >= MTU) index = 0
  }

  private def clear(): Unit = index = 0


  def push(byte: Byte): Option[Array[Byte]] = state match {
    case WAITING =>
      if (byte == START) {
        clear()
        state = RECEIVING
      }
      None

    case RECEIVING => byte match {
      case START =>
        clear()
        state = RECEIVING  
        None
      case ESCAPE =>
        state = ESCAPING
        None
      case STOP =>
        state = WAITING
        Some(java.util.Arrays.copyOfRange(data, 0, index))

      case _ =>
        add(byte)
        None
    }
    case ESCAPING =>
      add(byte)
      state = RECEIVING
      None
  }

  def push(bytes: Array[Byte]): Seq[Array[Byte]] = {
    val messages = new ArrayBuffer[Array[Byte]]

    for (byte <- bytes) push(byte) match {
      case None => ()
      case Some(message) => messages += message
    }

    messages
  }

}