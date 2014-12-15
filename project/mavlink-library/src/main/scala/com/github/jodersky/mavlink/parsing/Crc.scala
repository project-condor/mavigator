package com.github.jodersky.mavlink.parsing

case class Crc(val crc: Int = 0xffff) extends AnyVal {

  def accumulate(datum: Byte): Crc = {
    val d = datum & 0xff
    var tmp = d ^ (crc & 0xff)
    tmp ^= (tmp << 4) & 0xff;
    Crc(
      ((crc >> 8) & 0xff) ^ (tmp << 8) ^ (tmp << 3) ^ ((tmp >> 4) & 0xff))
  }

  def accumulate(data: Seq[Byte]): Crc = {
    var next = this
    for (d <- data) {
      next = next.accumulate(d)
    }
    next
  }

  def lsb: Byte = crc.toByte
  def msb: Byte = (crc >> 8).toByte

}