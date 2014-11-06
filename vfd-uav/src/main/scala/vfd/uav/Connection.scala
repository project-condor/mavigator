package vfd.uav

import scala.collection.mutable.ArrayBuffer

import akka.actor.Actor
import akka.actor.ActorRef

object Connection {
  trait Event
  trait Command
  case object Register extends Command
  case class Received(bytes: Array[Byte]) extends Event

}

trait Connection { that: Actor =>
  private val _clients = new ArrayBuffer[ActorRef]
  def clients = _clients.toSeq
  def register(client: ActorRef) = {
    _clients += client;
    that.context.watch(client)
  }
  def unregister(client: ActorRef) = _clients -= client
}
