package plugins

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.actorRef2Scala
import play.api.Application
import play.api.Plugin
import play.api.libs.concurrent.Akka
import vfd.uav.Connection
import vfd.uav.MockConnection
import vfd.uav.SerialConnection

class UavPlugin(app: Application) extends Plugin {

  private lazy val config = app.configuration.getConfig("uav")

  lazy val systemId = config.flatMap(_.getInt("system_id")).getOrElse(1)

  private lazy val connection = {
    val conn = config.flatMap(_.getConfig("connection"))
    val tpe = conn.flatMap(_.getString("type")).getOrElse("mock")
    val heartbeat = conn.flatMap(_.getInt("heartbeat")).getOrElse(2000)
    val id = conn.flatMap(_.getInt("component_id")).getOrElse(99).toByte

    val props = tpe match {
      case "mock" =>
        MockConnection.apply

      case "serial" =>
        val serial = config.flatMap(_.getConfig("serial"))
        SerialConnection(
          id,
          heartbeat,
          serial.flatMap(_.getString("port")).getOrElse("/dev/ttyUSB0"),
          serial.flatMap(_.getInt("baud")).getOrElse(115200),
          serial.flatMap(_.getBoolean("two_stop_bits")).getOrElse(false),
          serial.flatMap(_.getInt("parity")).getOrElse(0))

      case unknown => throw new RuntimeException("Unsupported connection type '" + unknown + "'")
    }

    Akka.system(app).actorOf(props, name = "uav-connection")
  }

  def register(websocket: ActorRef): Props = Props(classOf[ClientConnection], websocket, connection)

}

class ClientConnection(websocket: ActorRef, uav: ActorRef) extends Actor with ActorLogging {

  override def preStart = {
    uav ! Connection.Register
  }

  def receive = {

    case Connection.Received(bstr) =>
      log.info(bstr.toArray.mkString(","))
      websocket ! bstr.toArray


    case Connection.Closed(msg) =>
      log.warning(msg)
      context stop self

  }

}