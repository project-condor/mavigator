package vfd.uav

import java.util.concurrent.TimeUnit.MILLISECONDS
import scala.concurrent.duration.FiniteDuration
import akka.actor.Actor
import akka.actor.Props
import akka.actor.Terminated
import akka.actor.actorRef2Scala
import akka.actor.ActorLogging
import scala.util.Random

class MockConnection extends Actor with ActorLogging with Connection {
  import Connection._
  import context._

  val messageInterval = FiniteDuration(500, MILLISECONDS)
  
  override def preStart() = {
    context.system.scheduler.schedule(messageInterval, messageInterval) {
      val data = MockPackets.random()
      
      this.log.debug("sending mock flight data: " + data.mkString("(", ",", ")"))
      clients foreach (_ ! Received(data))
    }
  }

  def receive = {
    case Connection.Register => register(sender)
    case Terminated(client) => unregister(client)
  }

}

object MockConnection {
  def apply = Props(classOf[MockConnection])
}

object MockPackets { 
  
  def random() = {
    Random.nextInt(2) match {
      case 0 => invalidCrc
      case 1 => invalidOverflow
    }
    
  }
  
  val invalidCrc = Array(254,1,123,13,13).map(_.toByte)
  val invalidOverflow = {
    val data = Array.fill[Byte](1006)(42)
    data(0) = -2
    data(1) = 2
    data(1) = -1
    data
  }
  
}