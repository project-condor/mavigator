package vfd.uav

import java.util.concurrent.TimeUnit.MILLISECONDS
import scala.concurrent.duration.FiniteDuration
import org.mavlink.Packet
import org.mavlink.Parser
import org.mavlink.Assembler
import org.mavlink.messages.Heartbeat
import org.mavlink.messages.Ack
import org.mavlink.messages.Message
import com.github.jodersky.flow.Parity
import com.github.jodersky.flow.Serial
import com.github.jodersky.flow.SerialSettings
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.Terminated
import akka.actor.actorRef2Scala
import akka.io.IO
import akka.util.ByteString
import org.mavlink.messages.Ping

class SerialConnection(
  val systemId: Byte,
  val componentId: Byte,
  heartbeatInterval: Option[FiniteDuration],
  port: String,
  settings: SerialSettings) extends Actor with ActorLogging with Connection with MavlinkUtil {

  import context._

  override def preStart() = heartbeatInterval foreach { interval =>
    context.system.scheduler.schedule(interval, interval) {
      self ! Connection.Send(assemble(Heartbeat(0)))
    }
  }

  def _closed: Receive = {

    case Connection.Register =>
      register(sender)
      IO(Serial) ! Serial.Open(port, settings)
      context become opening

    case Connection.Send(_) =>
      IO(Serial) ! Serial.Open(port, settings)
      context become opening

  }

  def _opening: Receive = {

    case Serial.CommandFailed(cmd: Serial.Open, reason) =>
      sendAll(Connection.Closed(reason.toString))
      context become closed

    case Serial.Opened(_) =>
      context watch (sender)
      context become opened(sender)

    case Connection.Send(_) => () // ignore
    /*
    	 * During opening, any outgoing messages are discarded.
       * By using some kind of message stashing, maybe messages could be treated
       * once the port has been opened. However, in such a case failure also needs
       * to be considered, thus complicating the protocol. Since opening is typically
       * quite fast and since mavlink uses heartbeats and acknowledgements (in certain
       * circumstances) anyway, keeping messages is not really required.  
       */

  }

  def _opened(operator: ActorRef): Receive = {

    case Terminated(`operator`) =>
      sendAll(Connection.Closed("Serial connection crashed."))
      context become closed

    case Serial.Closed =>
      sendAll(Connection.Closed("Serial connection was closed."))
      context become closed

    case Serial.Received(bstr) =>
      sendAll(Connection.Received(bstr))
      incoming.push(bstr)

    case Connection.Send(bstr) =>
      outgoing.push(bstr)
    //no sending is enabled

  }

  def receive = closed
  def closed = _closed orElse registration
  def opening = _opening orElse registration
  def opened(op: ActorRef) = _opened(op) orElse registration

}

object SerialConnection {
  def apply(
    systemId: Byte,
    componentId: Byte,
    heartbeatInterval: Int,
    port: String,
    baud: Int,
    tsb: Boolean,
    parity: Int): Props = {

    val settings = SerialSettings(
      baud,
      8,
      tsb,
      parity match {
        case 0 => Parity.None
        case 1 => Parity.Odd
        case 2 => Parity.Even
      })
    val hb = if (heartbeatInterval == 0) None else Some(FiniteDuration(heartbeatInterval, MILLISECONDS))

    Props(classOf[SerialConnection], systemId, componentId, hb, port, settings)
  }
}