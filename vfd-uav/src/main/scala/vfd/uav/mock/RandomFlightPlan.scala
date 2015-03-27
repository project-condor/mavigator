package vfd.uav.mock

import scala.util.Random

import org.mavlink.Mavlink
import org.mavlink.enums.MavAutopilot
import org.mavlink.enums.MavModeFlag
import org.mavlink.enums.MavState
import org.mavlink.enums.MavType
import org.mavlink.messages.Attitude
import org.mavlink.messages.GlobalPositionInt
import org.mavlink.messages.Heartbeat

class RandomFlightPlan {
  
  private var time: Double = 0
  private var x: Double = 0.0
  private var y: Double = 0.0
  private var vX: Double = 0.0
  private var vY: Double = 0.0

  def tick(delta: Double) {
    val aX = Random.nextDouble() * 5
    val aY = Random.nextDouble() * 5

    x += vX * delta
    y += vY * delta
    vX += aX * delta
    vY += aY * delta

    time += delta
  }

  private val EarthRadius = 6000000
  private val StartLat = 46.518513 //N
  private val StartLon = 6.566923 //E

  def position = GlobalPositionInt(
    (time * 1000).toInt,
    (StartLat + x / EarthRadius).toInt,
    (StartLon + y / EarthRadius).toInt,
    0,
    0,
    (vX * 100).toShort,
    (vY * 100).toShort,
    0,
    0
  )

  def attitude = Attitude(
    (time * 1000).toInt,
    (time / 5 * math.Pi * 2).toFloat,
    (time / 4 * math.Pi * 2).toFloat,
    (time / 3 * math.Pi * 2).toFloat,
    0,
    0,
    0
  )

  def heartbeat = Heartbeat(
    MavType.MavTypeGeneric.toByte,
    MavAutopilot.MavAutopilotGeneric.toByte,
    (MavModeFlag.MavModeFlagSafetyArmed | MavModeFlag.MavModeFlagManualInputEnabled).toByte,
    0, //no custom mode
    MavState.MavStateActive.toByte,
    Mavlink.MavlinkVersion
  )

}