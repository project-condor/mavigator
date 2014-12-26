package plugins

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import vfd.uav.Connection
import akka.util.ByteString

/**
 * Interfaces traffic from a websocket with a connection to a UAV.
 */
class UavClientConnection(websocket: ActorRef, uav: ActorRef) extends Actor with ActorLogging {

  override def preStart = {
    uav ! Connection.Register
  }

  def receive = {

    case Connection.Received(bstr) =>
      websocket ! bstr.toArray

    case Connection.Closed(msg) =>
      log.warning(msg)
      context stop self
      
    case fromClient: Array[Byte] =>
      uav ! Connection.Send(ByteString(fromClient))

  }

}