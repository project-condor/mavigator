package mavigator.uav

import akka.actor.ActorLogging
import scala.collection.mutable.ArrayBuffer

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Terminated
import akka.actor.actorRef2Scala
import akka.util.ByteString

/** Protocol definition. */
object Connection {

  /** Any messages received and transmitted by actors implementing this protocol. */
  sealed trait Message

  /** Messages emitted by actors implementing this protocol. */
  trait Event extends Message

  /** Received data from the uav (or any other systems on the link) */
  case class Received(bstr: ByteString) extends Event

  /** The connection closed or could not be opened */
  case class Closed(message: String) extends Event

  /** Messages that can be received by actors implementing this protocol. */
  trait Command extends Message

  /** Register the sender to be notified on events */
  case object Register extends Command

  case class Unregister(client: ActorRef) extends Command

  /** Send given bytes out to the uav (or any other systems on the link) */
  case class Send(bstr: ByteString) extends Command

}

/** Common behavior of connection actors. */
trait Connection { myself: Actor with ActorLogging =>

  private val _clients = new ArrayBuffer[ActorRef]

  /** Current clients that should be notified on incoming messages. */
  def clients = _clients.toSeq

  /** Adds a client to the client list and acquires a deathwatch. */
  protected def register(client: ActorRef): Unit = {
    _clients += client
    myself.context.watch(client)
    myself.log.info("Client registered {}", client)
  }

  /** Remove client and release deathwatch. */
  protected def unregister(client: ActorRef): Unit = if (clients contains client) {
    _clients -= client
    myself.context.unwatch(client)
    myself.log.info("Client unregistered {}", client)
  }

  /** Sends a message to all registered clients. */
  protected def sendAll(msg: Any): Unit = clients foreach (_ ! msg)

  /** Common registration behavior. Manages the messages `Register` and `Terminated` by
    * registering and unregistering clients. */
  protected def handleRegistration: Receive = {
    case Connection.Register => register(sender)
    case Terminated(client) => unregister(client)
    case other => myself.log.warning("Unknown message: {}", other)
  }

}
