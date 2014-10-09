package vfd.uav

import akka.actor.Actor
import akka.actor.Terminated
import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit._

class DummyConnection extends Actor with Connection {
  import context._

  var time = 0.0
  val messageInterval = FiniteDuration(50, MILLISECONDS)

  def flightData(time: Double) = {
    Connection.NewDataFrame(DataFrame(
      math.sin(time/6000) * math.Pi,
      math.sin(time/5050) * math.Pi/4,
      time/5000 * 2 * math.Pi,
      time/1000,
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



