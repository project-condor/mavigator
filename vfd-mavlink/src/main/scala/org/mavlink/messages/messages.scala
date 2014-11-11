package org.mavlink.messages

import org.mavlink.PayloadWriter
import org.mavlink.PayloadReader

case class Heartbeat(customMode: Int, `type`: Byte, autopilot: Byte, baseMode: Byte, systemStatus: Byte, mavlinkVersion: Byte) extends Message {
  def pack(implicit mkWriter: Array[Byte] => PayloadWriter): Array[Byte] = {
    val arr = new Array[Byte](9)
    val writer = mkWriter(arr)
    writer.writeInt32(customMode)
    writer.writeInt8(`type`)
    writer.writeInt8(autopilot)
    writer.writeInt8(baseMode)
    writer.writeInt8(systemStatus)
    writer.writeInt8(mavlinkVersion)
    arr
  }
}

object Heartbeat extends MessageCompanion[Heartbeat]{
  def unpack(payload: Seq[Byte])(implicit mkReader: Seq[Byte] => PayloadReader) = {
    val reader = mkReader(payload)
    Heartbeat(reader.nextInt32, reader.nextInt8, reader.nextInt8, reader.nextInt8, reader.nextInt8, reader.nextInt8)
  }
}

case class RadioStatus(rxErrors: Short, fixed: Short, rssi: Byte, remoteRssi: Byte, txBuf: Byte, noise: Byte, remoteNoise: Byte) extends Message {
  def pack(implicit mkWriter: Array[Byte] => PayloadWriter): Array[Byte] = {
    val arr = new Array[Byte](9)
    val writer = mkWriter(arr)
    writer.writeInt16(rxErrors)
    writer.writeInt16(fixed)
    writer.writeInt8(rssi)
    writer.writeInt8(remoteRssi)
    writer.writeInt8(txBuf)
    writer.writeInt8(noise)
    writer.writeInt8(remoteNoise)
    arr
  }
}

object RadioStatus extends MessageCompanion[RadioStatus]{
  def unpack(payload: Seq[Byte])(implicit mkReader: Seq[Byte] => PayloadReader) = {
    val reader = mkReader(payload)
    RadioStatus(reader.nextInt16, reader.nextInt16, reader.nextInt8, reader.nextInt8, reader.nextInt8, reader.nextInt8, reader.nextInt8)
  }
}