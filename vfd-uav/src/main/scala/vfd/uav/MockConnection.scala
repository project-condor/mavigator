package vfd.uav

import java.util.concurrent.TimeUnit.MILLISECONDS
import scala.concurrent.duration.FiniteDuration
import scala.util.Random
import Connection.Received
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.util.ByteString
import org.mavlink.messages._
import org.mavlink.Packet

class MockConnection(localSystemId: Byte, localComponentId: Byte, remoteSystemId: Byte) extends Actor with ActorLogging with Connection with MavlinkUtil {
  import Connection._
  import context._
  
  override val systemId = remoteSystemId
  override val componentId = remoteSystemId

  val MessageInterval = FiniteDuration(100, MILLISECONDS)
  
  def randomData: ByteString = Random.nextInt(MockPackets.Messages + 1) match {
    case 0 => ByteString(MockPackets.invalid)
    case i => assemble(MockPackets.message(i - 1))
  }
  
  override def preStart() = context.system.scheduler.schedule(MessageInterval, MessageInterval) {
    sendAll(Received(randomData))
  }

  def receive = registration

}

object MockConnection {
  def apply(systemId: Byte, componentId: Byte, remoteSystemId: Byte) = Props(classOf[MockConnection], systemId, componentId, remoteSystemId)
}

object MockPackets {
  private val r = new Random
  private implicit class RichRandom(val r: Random) extends AnyVal {
    def nextByte(): Byte = r.nextInt().toByte
    def nextByte(max: Int): Byte = r.nextInt(max).toByte
  }
  
  def heartbeat = Heartbeat(0)
  def motor = Motor(r.nextByte(101), r.nextByte(101), r.nextByte(101), r.nextByte(101))
  def attitude = Attitude((r.nextInt(160) - 80).toShort, (r.nextInt(160) - 80).toShort, r.nextInt(360).toShort)
  def power = Power(Random.nextInt(12000).toShort)
  
  val Messages = 4
  def message(i: Int) = i match {
    case 0 => heartbeat
    case 1 => motor
    case 2 => attitude
    case 3 => power
  }

  val invalidCrc = Array(254, 1, 123, 13, 13).map(_.toByte)
  val invalidOverflow = {
    val data = Array.fill[Byte](Packet.MaxPayloadLength + 10)(42)
    data(0) = -2
    data(1) = 2
    data(1) = -1
    data
  }

  def invalid = r.nextInt(2) match {
    case 0 => invalidCrc
    case 1 => invalidOverflow
  }
}