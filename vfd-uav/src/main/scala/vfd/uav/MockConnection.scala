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

class MockConnection extends Actor with ActorLogging with Connection {
  import Connection._
  import context._

  val messageInterval = FiniteDuration(100, MILLISECONDS)

  override def preStart() = {
    context.system.scheduler.schedule(messageInterval, messageInterval) {
      val data = MockPackets.random

      this.log.debug("sending mock flight data: " + data.mkString("(", ",", ")"))
      sendAll(Received(ByteString(data)))
    }
  }

  def receive = registration

}

object MockConnection {
  def apply = Props(classOf[MockConnection])
}

object MockPackets {

  private implicit class RichMessage(val message: Message) extends AnyVal {
    def bytes: Array[Byte] = {
      val (id, payload) = Message.pack(message)
      Packet(5, 42, 1, id, payload).toSeq.toArray
    }
  }

  def messages = Heartbeat(0) ::
    Motor(Random.nextInt(101).toByte, Random.nextInt(101).toByte, Random.nextInt(101).toByte, Random.nextInt(101).toByte) ::
    Attitude((Random.nextInt(160) - 80).toShort, (Random.nextInt(160) - 80).toShort.toShort, Random.nextInt(360).toShort) ::
    Power(Random.nextInt(12000).toShort) :: Nil

  def valid: Array[Byte] = messages.flatMap(_.bytes).toArray

  val invalidCrc = Array(254, 1, 123, 13, 13).map(_.toByte)
  val invalidOverflow = {
    val data = Array.fill[Byte](Packet.MaxPayloadLength + 10)(42)
    data(0) = -2
    data(1) = 2
    data(1) = -1
    data
  }

  def randomInvalid = Random.nextInt(2) match {
    case 0 => invalidCrc
    case 1 => invalidOverflow
  }

  def random: Array[Byte] = Random.nextInt(messages.length + 1) match {
    case 0 => randomInvalid
    case i => messages(i - 1).bytes
  }
}