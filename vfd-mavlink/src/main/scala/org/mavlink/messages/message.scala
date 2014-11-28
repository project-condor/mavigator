package org.mavlink.messages

import org.mavlink.Packet

import org.mavlink.enums.SystemStatus
import org.mavlink.enums.SystemStatus._

sealed trait Message
case class Heartbeat(`type`: Byte, autopilot: Byte, baseMode: Byte, customMode: Int, systemStatus: SystemStatus, mavlinkVersion: Byte) extends Message
case class RadioStatus(rssi: Byte, remoteRssi: Byte, txBuf: Byte, noise: Byte, remoteNoise: Byte, rxErrors: Short, fixed: Short) extends Message
case class Attitude(time: Int, roll: Float, pitch: Float, heading: Float) extends Message
case class Pressure(time: Int, pressure: Float, diffPressure: Float, temperature: Short) extends Message
case class Battery(id: Byte, function: Byte, `type`: Byte, temperature: Short,
    voltages: Seq[Short], current: Short, currentConsumed: Int, energyConsumed: Int, remaining: Byte) extends Message
case class Unknown(id: Int, payload: Seq[Byte]) extends Message

object Message {

  def unpack(id: Byte,  payload: Seq[Byte])(implicit mkReader: Seq[Byte] => PayloadReader): Message = {
    val r = mkReader(payload)
    
    id & 0xff match {
      case 0 => 
        val cm = r.int32
        Heartbeat(r.int8, r.int8, r.int8, cm, SystemStatus(r.int8), r.int8)
      case 29 =>
        Pressure(r.int32, r.float, r.float, r.int16)
      case 30 =>
        Attitude(r.int32, r.float, r.float, r.float)
      case 109 =>
        val re = r.int16
        val fi = r.int16 
        RadioStatus(r.int8, r.int8, r.int8, r.int8, r.int8, re, fi)
      case 147 =>
        val cc = r.int32
        val ec = r.int32
        val t = r.int16
        val v = for (i <- 0 until 10) yield r.int16
        val c = r.int16
        val id = r.int8
        val fct = r.int8
        val tpe = r.int8
        val rm = r.int8
        Battery(id, fct, tpe, t, v, c, cc, ec, rm)
      
      case u => Unknown(u, payload)
    }
  }
  
  def pack(m: Message)(implicit mkWriter: Array[Byte] => PayloadWriter): (Byte, Seq[Byte]) = {
    val (id, size) = m match {
      case _: Heartbeat => (0, 9) 
      case _: RadioStatus => (109, 9)
      case u: Unknown => (u.id, u.payload.length) 
    }
    
    val arr = new Array[Byte](size)
    val w = mkWriter(arr)
    
    m match {
      case Heartbeat(tp, a, b, cm, s, mv) => w.int32(cm); w.int8(tp); w.int8(b); w.int8(s.id.toByte); w.int8(mv);
      case RadioStatus(r, rr, tx, n, rn, rx, fi) => w.int16(rx); w.int16(fi); w.int8(r); w.int8(rr); w.int8(tx); w.int8(n); w.int8(rn);
      case Unknown(_, payload) => for (p <- payload) w.int8(p)
    }
    
    (id.toByte, arr)
    
  }
    
    

}
  