package uav

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.Terminated
import scala.concurrent.duration.FiniteDuration
import scala.collection.mutable.ArrayBuffer
import java.util.concurrent.TimeUnit._
import akka.io.IO
import com.github.jodersky.flow._
import com.github.jodersky.flow.Serial

object UavConnection {
  def dummy = Props(classOf[DummyConnection])
  def fcu = Props(classOf[FcuConnection])

  trait Event
  trait Command
  case object Register extends Command

  case class Data(
    roll: Double,
    pitch: Double,
    heading: Double,
    altitude: Double,
    temperature: Double
  ) extends Event
}

trait UavConnection {selfs: Actor =>
  private val _clients = new ArrayBuffer[ActorRef]
  def clients = _clients.toSeq
  def register(client: ActorRef) = {
    _clients += client;
    selfs.context.watch(client)
  }
  def unregister(client: ActorRef) = _clients -= client
}

class DummyConnection extends Actor with UavConnection {
  import UavConnection._
  import context._

  var time = 0.0
  val messageInterval = FiniteDuration(20, MILLISECONDS)

  def flightData(time: Double) = {
    val speed = 5.0 / 1000
    val roll = 5.0/180*math.Pi
    val pitch = 10.0/180*math.Pi
    Data(
      roll,
      pitch,
      (roll * time * speed) % math.Pi,
      (pitch * time * speed),
      22
    )
  }


  override def preStart() = {
    context.system.scheduler.schedule(messageInterval, messageInterval){
      time += messageInterval.toMillis
      clients foreach (_ ! flightData(time))
    }
  }

  def receive = {
    case Register => register(sender)
    case Terminated(client) => unregister(client)
    case _ => ()
  }

}


class FcuConnection extends Actor with UavConnection {
  import UavConnection._
  import context._

  val port = "/dev/ttyACM0"
  val settings = SerialSettings(
    baud = 9600,
    characterSize = 8,
    twoStopBits = false,
    parity = Parity.None
  )

  override def preStart() = {
    IO(Serial) ! Serial.Open(port, settings)
  }

  def receive = {
    case Register => register(sender)
    case Terminated(client) => unregister(client)
    case Serial.CommandFailed(cmd: Serial.Open, reason: AccessDeniedException) => println("you're not allowed to open that port!")
    case Serial.CommandFailed(cmd: Serial.Open, reason) => println("could not open port for some other reason: " + reason)
    case Serial.Opened(settings) => {
      val operator = sender
      
    }
    case Serial.Received(bstr) => 
      val str = (new String(bstr.toArray, "UTF-8")).trim
      

      val LinePattern = ".*[(](.+)[)].*".r
      val Number = "([-]?\\d+[.]\\d+?)".r
      val Components = "(.+),(.+),(.+),(.+),(.+)".r

      str match {
        case LinePattern(Components(Number(r),Number(p),Number(h),Number(a),Number(t))) => 
          val data = Data(r.toDouble, p.toDouble, h.toDouble, a.toDouble, t.toDouble)
           println(data)
          for (client <- clients) {
            client ! data
          }
        case _ => println("unknown message: " + str)
      }
  }

}

