package mavigator

import akka.actor.ActorSystem
import akka.actor.Extension
import akka.actor.ExtensionId
import akka.actor.ExtensionIdProvider
import akka.actor.ExtendedActorSystem

import scala.concurrent.duration.Duration
import com.typesafe.config.Config
import java.util.concurrent.TimeUnit

import akka.actor.ActorRef
import akka.actor.Props
import mavigator.uav.MockConnection
import mavigator.uav.SerialConnection

class MavigatorImpl(system: ExtendedActorSystem) extends Extension {

  private val config = system.settings.config.getConfig("mavigator")

  val interface: String = config.getString("interface")

  val port: Int = config.getInt("port")

  /** Mavlink system ID identifying the base station */
  val systemId: Byte = config.getInt("system_id").toByte

  val tpe = config.getString("connection.type")

  /** Actor representing a connection channel to UAVs. This actor
    * implements the protocol defined in [mavigator.uav.Connection] */
  val uav: ActorRef = {
    val config = this.config.getConfig("connection")
    val tpe = config.getString("type")
    val heartbeat = config.getInt("heartbeat")
    val compId = config.getString("component_id").toByte

    val props = tpe match {
      case "mock" =>
        val remote = config.getInt("mock.remote_system_id").toByte
        val prescaler = config.getInt("mock.prescaler")
        MockConnection(systemId, compId, remote, prescaler)

      case "serial" =>
        val serial = config.getConfig("serial")
        SerialConnection(
          systemId,
          compId,
          heartbeat,
          serial.getString("port"),
          serial.getInt("baud"),
          serial.getBoolean("two_stop_bits"),
          serial.getInt("parity")
        )

      case unknown => throw new IllegalArgumentException("Unsupported connection type '" + unknown + "'")

    }

    system.actorOf(props, name = "uav-connection")
  }

}

object Mavigator extends ExtensionId[MavigatorImpl] with ExtensionIdProvider {
  
  override def lookup = Mavigator
  
  override def createExtension(system: ExtendedActorSystem) =
    new MavigatorImpl(system)

}

