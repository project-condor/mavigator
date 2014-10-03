package vfd.uav

import akka.actor.Actor
import akka.actor.Terminated
import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit._

class DummyConnection extends Actor with Connection {
  import context._

  var time = 0.0
  val messageInterval = FiniteDuration(20, MILLISECONDS)

  def flightData(time: Double) = {
    val speed = 5.0 / 1000
    val roll = 5.0/180*math.Pi
    val pitch = 10.0/180*math.Pi
    Connection.NewDataFrame(DataFrame(
      roll,
      pitch,
      (roll * time * speed) % math.Pi,
      (pitch * time * speed),
      22
    ))
  }


  override def preStart() = {
    context.system.scheduler.schedule(messageInterval, messageInterval){
      time += messageInterval.toMillis
      clients foreach (_ ! flightData(time))
    }
  }

  def receive = {
    case Connection.Register => register(sender)
    case Terminated(client) => unregister(client)
  }

}



