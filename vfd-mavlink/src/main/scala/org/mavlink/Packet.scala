package org.mavlink

case class Packet(
  seq: Byte,
  systemId: Byte,
  componentId: Byte,
  messageId: Byte,
  payload: Seq[Byte]
) {

  lazy val crc = {
    var c = new Crc()
    c = c.next(payload.length.toByte)
    c = c.next(seq)
    c = c.next(systemId)
    c = c.next(componentId)
    c = c.next(messageId)
    for (p <- payload) {
      c = c.next(p)
    }
    c = c.next(Packet.MessageIdCrcEnds(messageId & 0xff))
    c
  }

  def toSeq: Seq[Byte] = Seq(
    Packet.Stx,
    payload.length.toByte,
    seq,
    systemId,
    componentId,
    messageId
  ) ++ payload ++ Seq(
    crc.lsb,
    crc.msb
  )

}

object Packet {

  final val Stx: Byte = (0xfe).toByte;
  final val MaxPayloadLength: Int = 255 

  final val MessageIdCrcEnds: Seq[Byte] = Array(
     50, 124, 137,   0, 237, 217, 104, 119,   0,   0,
      0,  89,   0,   0,   0,   0,   0,   0,   0,   0,
    214, 159, 220, 168,  24,  23, 170, 144,  67, 115,
     39, 246, 185, 104, 237, 244, 222, 212,   9, 254,
    230,  28,  28, 132, 221, 232,  11, 153,  41,  39,
      0,   0,   0,   0,  15,   3,   0,   0,   0,   0,
      0, 153, 183,  51,  82, 118, 148,  21,   0, 243,
    124,   0,   0,  38,  20, 158, 152, 143,   0,   0,
      0, 106,  49,  22, 143, 140,   5, 150,   0, 231,
    183,  63,  54,   0,   0,   0,   0,   0,   0,   0,
    175, 102, 158, 208,  56,  93, 211, 108,  32, 185,
     84,   0,   0, 124, 119,   4,  76, 128,  56, 116,
    134, 237, 203, 250,  87, 203, 220,  25, 226,   0,
     29, 223,  85,   6, 229, 203,   1,   0,   0,   0,
      0,   0,   0,   0,   0,   0,   0, 154,  49,   0,
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
      0,   0,   0,   0,   0,   0,   0,   0,   0,   0,
      0,   0,   0,   0,   0,   0,   0,   0,   8, 204,
     49, 170,  44,  83,  46,   0
  ).map(_.toByte)

  final val Empty = Packet(0, 0, 0, 0, List())

}