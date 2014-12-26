package vfd.uav

import scala.collection.mutable.ArrayBuffer
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Terminated
import akka.actor.actorRef2Scala
import akka.util.ByteString
import org.mavlink.Assembler
import org.mavlink.messages.Message
import org.mavlink.Parser
import org.mavlink.Packet
import akka.actor.ActorLogging

/** Protocol definition. */
object Connection {
  trait Event
  trait Command

  /** Received data from the uav (or any other systems on the link) */
  case class Received(bstr: ByteString) extends Event

  /** The connection closed or could not be opened */
  case class Closed(message: String) extends Event

  /** Register the sender to be notified on events */
  case object Register extends Command

  /** Send given bytes out to the uav (or any other systems on the link) */
  case class Send(bstr: ByteString) extends Command
}

/** Common behavior of connection actors. */
trait Connection { myself: Actor =>

  /** Current clients that should be notified on incoming messages. */
  private val _clients = new ArrayBuffer[ActorRef]
  def clients = _clients.toSeq

  /** Adds a client to the client list and acquires a deathwatch. */
  protected def register(client: ActorRef) = {
    _clients += client;
    myself.context.watch(client)
  }

  /** Remove client and release deathwatch. */
  protected def unregister(client: ActorRef) = {
    _clients -= client
    myself.context.unwatch(client)
  }

  /** Sends a message to all registered clients. */
  protected def sendAll(msg: Any) = clients foreach (_ ! msg)

  /**
   * Common registration behavior. Manages the events `Register` and `Terminated` by
   * registering and unregistering clients.
   */
  protected def registration: Receive = {
    case Connection.Register => register(sender)
    case Terminated(client) if clients contains client => unregister(client)
  }
}