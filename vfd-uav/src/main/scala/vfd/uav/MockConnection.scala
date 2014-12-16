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

  val messageInterval = FiniteDuration(500, MILLISECONDS)

  override def preStart() = {
    context.system.scheduler.schedule(messageInterval, messageInterval) {
      val data = MockPackets.random()

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

  def random(): Array[Byte] = Random.nextInt(4) match {
    case 0 => randomInvalid()
    case 1 => Heartbeat(0).bytes
    case 2 => Motor(Random.nextInt(101).toByte, Random.nextInt(101).toByte, Random.nextInt(101).toByte, Random.nextInt(101).toByte).bytes
    case 3 => Attitude((Random.nextInt(160) - 80).toShort, (Random.nextInt(160) - 80).toShort.toShort, Random.nextInt(360).toShort).bytes
  }

  def randomInvalid() = Random.nextInt(2) match {
    case 0 => invalidCrc
    case 1 => invalidOverflow
  }

  val invalidCrc = Array(254, 1, 123, 13, 13).map(_.toByte)
  val invalidOverflow = {
    val data = Array.fill[Byte](1006)(42)
    data(0) = -2
    data(1) = 2
    data(1) = -1
    data
  }

}