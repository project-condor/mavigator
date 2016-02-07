package mavigator
package uav

import mock._
import akka._
import akka.actor._
import akka.util._
import akka.stream.scaladsl._

class Uav(system: ExtendedActorSystem) extends Extension {

  private lazy val config = system.settings.config.getConfig("mavigator.uav")

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
