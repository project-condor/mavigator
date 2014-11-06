package vfd.uav

import java.util.concurrent.TimeUnit.MILLISECONDS

import scala.concurrent.duration.FiniteDuration

import akka.actor.Actor
import akka.actor.Props
import akka.actor.Terminated
import akka.actor.actorRef2Scala

class DummyConnection extends Actor with Connection {
  import context._

  var time = 0.0
  val messageInterval = FiniteDuration(50, MILLISECONDS)

  def flightData(time: Double) = {
    new Array[Byte](10)
  }

  override def preStart() = {
    context.system.scheduler.schedule(messageInterval, messageInterval) {
      time += messageInterval.toMillis
      clients foreach (_ ! flightData(time))
    }
  }

  def receive = {
    case Connection.Register => register(sender)
    case Terminated(client) => unregister(client)
  }

}

object DummyConnection {
  def apply = Props(classOf[DummyConnection])
}

