package mavigator
package uav
package mock

import org.mavlink.enums._
import org.mavlink.messages.{Heartbeat, Message}
import akka.stream.scaladsl._
import scala.concurrent.duration._
import scala.util.Random
import akka.stream._
import akka.NotUsed
import akka.util._
import org.mavlink._
import Attributes._


//case class Heartbeat(`type`: Byte, autopilot: Byte, baseMode: Byte, customMode: Int, systemStatus: Byte, mavlinkVersion: Byte) extends Message

class MockConnection(
  remoteSystemId: Byte,
  remoteComponentId: Byte,
  prescaler: Double
) {
  import MockConnection._

  private def delayed(delaySeconds: Double)(message: RandomFlightPlan => Message): Flow[RandomFlightPlan, Message, NotUsed] = {
    val dt = delaySeconds / prescaler
    Flow[RandomFlightPlan].withAttributes(inputBuffer(1,1)).delay(dt.seconds).map(message)
  }

  val messages: Source[Message, _] = streamFromPlan(new RandomFlightPlan)(
    delayed(2)(_.heartbeat),
    delayed(0.2)(_.position),
    delayed(0.05)(_.attitude),
    delayed(0.05)(_.motors),
    delayed(0.1)(_.distance)
  )


  private lazy val assembler = new Assembler(remoteSystemId, remoteComponentId)

  /** Assembles a message into a bytestring representing a packet sent from this connection. */
  def assemble(message: Message): ByteString = {
    val (messageId, payload) = Message.pack(message)
    val packet: Packet = assembler.assemble(messageId, payload)
    ByteString(packet.toArray)
  }

  val data = messages.map{ msg =>
    if (msg.isInstanceOf[Heartbeat]){println(msg)}
    assemble(msg)
  }

}

object MockConnection {

  final val ClockTick: FiniteDuration = 0.01.seconds

  private def streamFromPlan(plan: RandomFlightPlan)(messages: Flow[RandomFlightPlan, Message, _]*): Source[Message, NotUsed] = {

    import GraphDSL.Implicits._

    Source.fromGraph(GraphDSL.create() { implicit b =>

      val clock = Source.tick(ClockTick, ClockTick, plan) map { plan =>
        plan.tick(0.01)
        plan
      }
      val bcast = b.add(Broadcast[RandomFlightPlan](messages.length))
      val merge = b.add(Merge[Message](messages.length))

      clock ~> bcast
      for (message <- messages) {
        bcast ~> message ~> merge
      }

      SourceShape(merge.out)
    })
  }

}
