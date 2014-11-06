package org.mavlink.messages

trait Message {}

case class Heartbeat(
  `type`: Int,
  autopilot: Int,
  baseMode: Int,
  customMode: Int,
  systemStatus: Int,
  mavlinkVersion: Int
) extends Message

object Message {

}