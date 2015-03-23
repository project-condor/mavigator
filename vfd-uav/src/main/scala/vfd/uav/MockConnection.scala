package vfd.uav

import java.util.concurrent.TimeUnit.MILLISECONDS

import scala.concurrent.duration.FiniteDuration
import scala.util.Random

import org.mavlink.Packet
import org.mavlink.enums.MavAutopilot
import org.mavlink.enums.MavModeFlag
import org.mavlink.enums.MavState
import org.mavlink.enums.MavType
import org.mavlink.messages.Heartbeat

import Connection.Received
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.util.ByteString

class MockConnection(localSystemId: Byte, localComponentId: Byte, remoteSystemId: Byte) extends Actor with ActorLogging with Connection with MavlinkUtil {
  import Connection._
  import context._

  override val systemId = remoteSystemId
  override val componentId = remoteSystemId

  val MessageInterval = FiniteDuration(1000, MILLISECONDS)

  def randomData: ByteString =
    assemble(
      Heartbeat(
        MavType.MavTypeGeneric.toByte,
        MavAutopilot.MavAutopilotGeneric.toByte,
        (MavModeFlag.MavModeFlagSafetyArmed | MavModeFlag.MavModeFlagManualInputEnabled).toByte,
        0, //no custom mode
        MavState.MavStateActive.toByte,
        0 //TODO properly implement read-only fields
        ))

  override def preStart() = context.system.scheduler.schedule(MessageInterval, MessageInterval) {
    sendAll(Received(randomData))
  }

  def receive = registration

}

object MockConnection {
  def apply(systemId: Byte, componentId: Byte, remoteSystemId: Byte) = Props(classOf[MockConnection], systemId, componentId, remoteSystemId)
}

object MockPackets {
  private val r = new Random
  private implicit class RichRandom(val r: Random) extends AnyVal {
    def nextByte(): Byte = r.nextInt().toByte
    def nextByte(max: Int): Byte = r.nextInt(max).toByte
  }

  val invalidCrc = Array(254, 1, 123, 13, 13).map(_.toByte)
  val invalidOverflow = {
    val data = Array.fill[Byte](Packet.MaxPayloadLength + 10)(42)
    data(0) = -2
    data(1) = 2
    data(1) = -1
    data
  }

  def invalid = r.nextInt(2) match {
    case 0 => invalidCrc
    case 1 => invalidOverflow
  }
}