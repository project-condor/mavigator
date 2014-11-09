package vfd.uav

import scala.collection.mutable.ArrayBuffer

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Terminated
import akka.actor.actorRef2Scala
import akka.util.ByteString

object Connection {
  trait Event
  trait Command

  //received data from the uav (or any other systems on the link)
  case class Received(bstr: ByteString) extends Event

  //the connection closed or could be opened
  case class Closed(message: String) extends Event

  //register the sender to be notified on events
  case object Register extends Command

  //send given bytes out to the uav (or any other systems on the link)
  case class Send(bstr: ByteString) extends Command
}

trait Connection { that: Actor =>
  private val _clients = new ArrayBuffer[ActorRef]

  def clients = _clients.toSeq

  def register(client: ActorRef) = {
    _clients += client;
    that.context.watch(client)
  }

  def unregister(client: ActorRef) = _clients -= client

  def sendAll(msg: Any) = clients foreach (_ ! msg)

  def registration: Receive = {
    case Connection.Register => register(sender)
    case Terminated(client) if clients contains client => unregister(client)
  }
}