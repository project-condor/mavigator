package mavigator.uav
package mock

import scala.util.Random

import org.mavlink.Mavlink
import org.mavlink.enums._
import org.mavlink.messages._

class RandomFlightPlan {

  private var time: Double = 0 //current time in seconds
  private def millis = (time * 1000).toInt
  private def micros = (time * 1E6).toInt

  // an oscilliating function
  private def osc[A](min: A, max: A, period: Double, offset: Double = 0)(implicit n: Numeric[A]): A = {
    val amplitude = (n.toDouble(max) - n.toDouble(min)) / 2
    val base = (n.toDouble(max) + n.toDouble(min)) / 2
    val factor = math.sin(2 * math.Pi * (time + offset) / period)
    n.fromInt((base + amplitude * factor).toInt)
  }

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

  private final val EarthRadius = 6000000 //m
  private final val StartLat = 46.518513 //deg N
  private final val StartLon = 6.566923 //deg E

  def position = GlobalPositionInt(
    millis,
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
    millis,
    (2 * math.Pi * time / 6).toFloat,
    (math.sin(2 * math.Pi * time / 5) * math.Pi / 6).toFloat,
    (2 * math.Pi * time / 4).toFloat,
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

  private final val DistanceMin: Short = 10
  private final val DistanceMax: Short = 500
  def distance = DistanceSensor(
    timeBootMs = millis,
    minDistance = DistanceMin,
    maxDistance = DistanceMax,
    currentDistance = osc(DistanceMin, DistanceMax, 6),
    `type` = MavDistanceSensor.MavDistanceSensorUltrasound.toByte,
    id = 0: Byte,
    orientation = -1: Byte,
    covariance = 3: Byte)

  private final val MotorsMax: Short = 2000 //usec, ppm signal => 100%
  private final val MotorsMin: Short = 1000 //usec, ppm signal => 0%
  def motors = ServoOutputRaw(
    timeUsec = micros,
    port = 0: Byte,
    servo1Raw =  osc(MotorsMin, MotorsMax, 6, 0),
    servo2Raw = osc(MotorsMin, MotorsMax, 6, 5),
    servo3Raw = osc(MotorsMin, MotorsMax, 6, 2),
    servo4Raw = osc(MotorsMin, MotorsMax, 6, 4),
    servo5Raw = 0,
    servo6Raw = 0,
    servo7Raw = 0,
    servo8Raw = 0
  )

}
