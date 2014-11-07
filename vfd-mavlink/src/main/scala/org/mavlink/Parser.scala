package org.mavlink

import scala.collection.mutable.ArrayBuffer

object Parser {

  object ParseStates {
    sealed trait State
    case object Idle extends State
    case object GotStx extends State
    case object GotLength extends State
    case object GotSeq extends State
    case object GotSysId extends State
    case object GotCompId extends State
    case object GotMsgId extends State
    case object GotCrc1 extends State
    case object GotPayload extends State
  }
  
  object ParseErrors {
    sealed trait ParseError
    case object CrcError extends ParseError
    case object OverflowError extends ParseError
  }
}

class Parser(receiver: Packet => Unit, error: Parser.ParseErrors.ParseError => Unit) {
  import Parser._

  private var state: ParseStates.State = ParseStates.Idle

  private object inbound {
    var length: Int = 0
    var seq: Byte = 0
    var systemId: Byte = 0
    var componentId: Byte = 0
    var messageId: Byte = 0
    var payload = new ArrayBuffer[Byte]
    var crc: Crc = new Crc()
  }

  def push(c: Byte): Unit = {
    import ParseStates._

    state match {
      case Idle =>
        if (c == Packet.Stx) {
          state = GotStx
        }

      case GotStx =>
        inbound.crc = new Crc()
        inbound.length = (c & 0xff)
        inbound.crc = inbound.crc.next(c)
        state = GotLength

      case GotLength =>
        inbound.seq = c;
        inbound.crc = inbound.crc.next(c)
        state = GotSeq

      case GotSeq =>
        inbound.systemId = c
        inbound.crc = inbound.crc.next(c)
        state = GotSysId

      case GotSysId =>
        inbound.componentId = c
        inbound.crc = inbound.crc.next(c)
        state = GotCompId

      case GotCompId =>
        inbound.messageId = c
        inbound.crc = inbound.crc.next(c)
        if (inbound.length == 0) {
          state = GotPayload
        } else {
          state = GotMsgId
          inbound.payload.clear()
        }

      case GotMsgId =>
        inbound.payload += c
        inbound.crc = inbound.crc.next(c)
        if(inbound.payload.length >= Packet.MaxPayloadLength) {
          state = Idle
          error(ParseErrors.OverflowError)
        }
        if (inbound.payload.length >= inbound.length) {
          state = GotPayload
        }

      case GotPayload =>
        inbound.crc = inbound.crc.next(Packet.MessageIdCrcEnds(inbound.messageId & 0xff))
        if (c != inbound.crc.lsb) {
          state = Idle
          if (c == Packet.Stx) {
            state = GotStx
          }
          error(ParseErrors.CrcError)
        } else {
          state = GotCrc1
        }

      case GotCrc1 =>
        if (c != inbound.crc.msb) {
          state = Idle
          if (c == Packet.Stx) {
            state = GotStx
          }
          error(ParseErrors.CrcError)
        } else {
          val packet = Packet(
            inbound.seq,
            inbound.systemId,
            inbound.componentId,
            inbound.messageId,
            inbound.payload)
          state = Idle
          inbound.payload = new ArrayBuffer[Byte]()
          receiver(packet)
        }
    }
  }

}
