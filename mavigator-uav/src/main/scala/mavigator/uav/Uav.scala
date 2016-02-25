package mavigator
package uav

import java.lang.IllegalArgumentException
import mock._
import akka._
import akka.actor._
import akka.util._
import akka.stream.scaladsl._

class Uav(system: ExtendedActorSystem) extends Extension {

  private lazy val config = system.settings.config.getConfig("mavigator.uav")
  private lazy val tpe = config.getString("type")
  private lazy val componentId = config.getInt("componentId").toByte
  private lazy val heartbeat = config.getInt("heartbeat")
  private lazy val connection = config.getConfig(tpe)

  lazy val source = tpe match {
    case "mock" =>
      new MockConnection(
        connection.getInt("remote_system_id").toByte,
        componentId,
        connection.getDouble("prescaler")
      )

    case "serial" => ???

    case _ => throw new IllegalArgumentException(s"Unsupported connection type: $tpe")
  }

  def connect(): Flow[ByteString, ByteString, NotUsed] = {
    Flow.fromSinkAndSource(
      Sink.ignore,
      (new MockConnection(0,0,1)).data
    )
  }

}

object Uav extends ExtensionId[Uav] with ExtensionIdProvider {

  override def lookup = Uav

  override def createExtension(system: ExtendedActorSystem) = new Uav(system)

  def apply()(implicit system: ActorSystem) = super.apply(system)

}
