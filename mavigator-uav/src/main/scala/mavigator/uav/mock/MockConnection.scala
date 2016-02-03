package mavigator
package uav
package mock

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
import scala.concurrent.duration._
import org.mavlink.messages.Message
import mock.RandomFlightPlan

class MockConnection(
  localSystemId: Byte,
  localComponentId: Byte,
  remoteSystemId: Byte,
  prescaler: Int
)
  extends Actor with ActorLogging with Connection with MavlinkUtil {

  import context._

  override val systemId = remoteSystemId
  override val componentId = remoteSystemId

  val plan = new RandomFlightPlan

  def scheduleMessage(delay: FiniteDuration)(fct: => Message) = system.scheduler.schedule(delay, delay) {
    sendAll(Received(assemble(fct)))
  }
  def scheduleBytes(delay: FiniteDuration)(fct: => Array[Byte]) = system.scheduler.schedule(delay, delay) {
    sendAll(Received(ByteString(fct)))
  }

  override def preStart() = {
    //increment state
    system.scheduler.schedule(0.01.seconds * prescaler, 0.01.seconds * prescaler) { plan.tick(0.01) }

    //send messages
    scheduleMessage(0.1.seconds * prescaler)(plan.position)
    scheduleMessage(0.05.seconds * prescaler)(plan.attitude)
    scheduleMessage(0.05.seconds * prescaler)(plan.motors)
    scheduleMessage(0.1.seconds * prescaler)(plan.distance)
    scheduleMessage(1.seconds)(plan.heartbeat)

    //simulate noisy line
    scheduleBytes(0.3.seconds * prescaler)(MockPackets.invalidCrc)
    scheduleBytes(1.5.seconds * prescaler)(MockPackets.invalidOverflow)
  }

  override def receive = handleRegistration

}

object MockConnection {
  def apply(systemId: Byte, componentId: Byte, remoteSystemId: Byte, prescaler: Int = 1) =
    Props(classOf[MockConnection], systemId, componentId, remoteSystemId, prescaler)
}

object MockPackets {
  val invalidCrc = Array(254, 1, 123, 13, 13).map(_.toByte)
  val invalidOverflow = {
    val data = Array.fill[Byte](Packet.MaxPayloadLength + 100)(42)
    data(0) = -2
    data(1) = 2
    data(1) = -1
    data
  }
}
