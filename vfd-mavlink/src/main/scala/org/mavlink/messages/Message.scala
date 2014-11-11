package org.mavlink.messages

import org.mavlink.PayloadReader
import org.mavlink.PayloadWriter
import org.mavlink.Packet

trait Message {
  def pack(implicit mkWriter: Array[Byte] => PayloadWriter): Array[Byte]
}

trait MessageCompanion[M <: Message] {
  def unpack(bytes: Seq[Byte])(implicit mkReader: Seq[Byte] => PayloadReader): M
}

object Message {

  def unpack(packet: Packet)(implicit mkReader: Seq[Byte] => PayloadReader) = packet.messageId match {
    case 0 => Heartbeat.unpack(packet.payload)
    case 109 => RadioStatus.unpack(packet.payload)
  }
  
  /*
  private val msg = Heartbeat(23,1,2,4,5,6)
  def foo = {
    val spec = msg.pickle.value
    (spec.id, spec.payload.mkString("(", ",", ")"))
  }*/

}
  