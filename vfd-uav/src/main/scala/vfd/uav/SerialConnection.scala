package vfd.uav

import com.github.jodersky.flow.Parity
import com.github.jodersky.flow.SerialSettings

import akka.actor.Actor
import akka.actor.Props

class SerialConnection(id: Byte, heartbeat: Int, port: String, settings: SerialSettings) extends Actor with Connection {
  import context._
  
  //TODO implement actor logic

  def receive = {
    case _ => ()
  }
/*
  override def preStart() = {
    context.system.scheduler.schedule(messageInterval, messageInterval){
      self ! Connection.Write(Array(-2, 9, -121, 20, -56, 0, 0, 0, 0, 0, 2, 0, 0, 3, 3, -112, 76).map(_.toByte))
    }
  }


  def receive = closed

  def closed: Receive = {
    case Connection.Register(client) => 
      register(client)
      IO(Serial) ! Open(port, settings)
      context become opening

    case Terminated(client) if (clients contains client) => unregister(client)

    case Connection.Write(data) =>
      IO(Serial) ! Open(port, settings)
      context become opening

  }

  def opening: Receive = {
    case Connection.Register(client) => register(client)
    case Terminated(client) if (clients contains client) => unregister(client)

    case Connection.Write(data) =>

    case Serial.CommandFailed(cmd: Serial.Open, reason) =>
      Log(reason)
      //for (c <- clients) client ! Error //TODO send proper error code
      context become closed

    case Serial.Opened(settings) =>
      val operator = sender
      context watch operator
      context become open(operator)

  }

  def open(operator: ActorRef): Receive = {
    case Terminated(`operator`) => 
      //for (client <- clients) ! Error //TODO send error code
      context become closed

    case Connection.Write(data) => operator ! ByteString(data)
  }
*/
}

object SerialConnection {
  def apply(id: Byte, hearbeat: Int, port: String, baud: Int, tsb: Boolean, parity: Int) = {
    val settings = SerialSettings(
      baud,
      8,
      tsb,
      parity match {
        case 0 => Parity.None
        case 1 => Parity.Odd
        case 2 => Parity.Even
      }
    )
    Props(classOf[SerialConnection], id, hearbeat, port, settings)
  }
}