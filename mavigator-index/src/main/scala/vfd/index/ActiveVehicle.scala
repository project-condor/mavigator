package vfd.index

import org.mavlink.enums.MavAutopilot
import org.mavlink.enums.MavState
import org.mavlink.enums.MavType
import org.mavlink.messages.Heartbeat

case class ActiveVehicle(systemId: Int, vehicleType: String, autopilot: String, state: String)

object ActiveVehicle {

  def fromHeartbeat(id: Int, hb: Heartbeat) = ActiveVehicle(
    id,
    vehicleType(hb.`type`),
    autopilot(hb.autopilot),
    state(hb.systemStatus))

  def vehicleType(tpe: Int) = tpe match {
    case MavType.MavTypeGeneric => "Generic"
    case MavType.MavTypeQuadrotor => "Quadcopter"
    case MavType.MavTypeFixedWing => "Fixed Wing"
    case _ => "Other"
  }

  def autopilot(tpe: Int) = tpe match {
    case MavAutopilot.MavAutopilotGeneric => "Generic"
    case MavAutopilot.MavAutopilotInvalid => "Invalid"
    case MavAutopilot.MavAutopilotPixhawk => "Pixhawk"
    case _ => "Other"
  }

  def state(s: Int) = s match {
    case MavState.MavStateActive => "Active"
    case MavState.MavStateBoot => "Booting"
    case MavState.MavStateCalibrating => "Calibrating"
    case MavState.MavStateCritical => "Critical"
    case MavState.MavStateEmergency => "Emergency"
    case MavState.MavStatePoweroff => "Poweroff"
    case MavState.MavStateStandby => "Standby"
    case MavState.MavStateUninit => "Uninitialized"
    case _ => "Unknown"
  }

}