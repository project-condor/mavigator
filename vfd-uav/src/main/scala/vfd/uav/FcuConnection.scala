package vfd.uav

import akka.actor.Actor
import akka.actor.Props
import akka.actor.Terminated
import akka.io.IO
import com.github.jodersky.flow._
import com.github.jodersky.flow.Serial


class FcuConnection(port: String, baud: Int) extends Actor with Connection {
  import context._

  val settings = SerialSettings(
    baud = this.baud,
    characterSize = 8,
    twoStopBits = false,
    parity = Parity.None
  )

  override def preStart() = {
    IO(Serial) ! Serial.Open(port, settings)
  }

  def receive = {
    case Connection.Register => register(sender)
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
          val data = Connection.NewDataFrame(DataFrame(r.toDouble, p.toDouble, h.toDouble, a.toDouble, t.toDouble))
           println(data)
          for (client <- clients) {
            client ! data
          }
        case _ => println("unknown message: " + str)
      }
  }

}
