package org.mavlink.messages

case class Heartbeat(
  customMode: Int,
  `type`: Byte,
  autopilot: Byte,
  baseMode: Byte,
  systemStatus: Byte,
  mavlinkVersion: Byte) extends Message