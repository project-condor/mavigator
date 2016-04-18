package mavigator
package uav

import java.lang.IllegalArgumentException

import akka._
import akka.actor._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.util._

import mock._
import serial._

//TODO: the whole backend system feels hacky, it probably needs a major redesign
class Uav(system: ExtendedActorSystem) extends Extension {

  private val materializer = ActorMaterializer()(system)

  private lazy val config = system.settings.config.getConfig("mavigator.uav")
  private lazy val tpe = config.getString("type")

  private lazy val core = new Core()(system, materializer)

  lazy val backend: Backend = tpe match {
    case "mock" => MockBackend
    case "serial" => SerialBackend
    case _ => throw new IllegalArgumentException(s"Unsupported connection type: $tpe")
  }

  def init(): Unit = {
    backend.init(core)
  }

  def connect(): Flow[ByteString, ByteString, NotUsed] = core.connect()

}

object Uav extends ExtensionId[Uav] with ExtensionIdProvider {

  override def lookup = Uav

  override def createExtension(system: ExtendedActorSystem) = new Uav(system)

  def apply()(implicit system: ActorSystem) = super.apply(system)

}
