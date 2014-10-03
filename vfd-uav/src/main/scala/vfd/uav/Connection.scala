package vfd.uav

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import scala.collection.mutable.ArrayBuffer

object Connection {
  def dummy = Props(classOf[DummyConnection])
  def fcu(port: String, baud: Int) = Props(classOf[FcuConnection], port, baud)

  trait Event
  trait Command
  case object Register extends Command
  case class NewDataFrame(df: DataFrame) extends Event

}

trait Connection {that: Actor =>
  private val _clients = new ArrayBuffer[ActorRef]
  def clients = _clients.toSeq
  def register(client: ActorRef) = {
    _clients += client;
    that.context.watch(client)
  }
  def unregister(client: ActorRef) = _clients -= client
}
