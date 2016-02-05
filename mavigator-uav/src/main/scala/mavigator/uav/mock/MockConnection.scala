package mavigator
package uav
package mock

import org.mavlink.messages.Message
import akka.stream.scaladsl._
import scala.concurrent.duration._
import scala.util.Random
import akka.stream._
import akka.NotUsed
import akka.util._
import org.mavlink._
import Attributes._

class MockConnection(
  remoteSystemId: Byte,
  remoteComponentId: Byte,
  prescaler: Double
) {
  import MockConnection._

  private def stream(delaySeconds: Double)(message: RandomFlightPlan => Message): Flow[RandomFlightPlan, Message, NotUsed] = {
    val dt = delaySeconds / prescaler
    Flow[RandomFlightPlan].delay(dt.seconds).withAttributes(inputBuffer(1,1)).map(message)
  }

  def foo(messages: Flow[RandomFlightPlan, Message, _]*): Source[Message, NotUsed] = {
    import GraphDSL.Implicits._

    Source.fromGraph(GraphDSL.create() { implicit b =>

      val plan = new RandomFlightPlan

      //graph components
      val clock = Source.tick(1.seconds, 0.01.seconds, plan).map{plan =>
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

  val messages: Source[Message, _] = foo(
    stream(0.2)(_.position),
    stream(0.05)(_.attitude),
    stream(0.05)(_.motors),
    stream(0.1)(_.distance)
  )


  private lazy val assembler = new Assembler(remoteSystemId, remoteComponentId)

  /** Assembles a message into a bytestring representing a packet sent from this connection. */
  def assemble(message: Message): ByteString = {
    val (messageId, payload) = Message.pack(message)
    val packet: Packet = assembler.assemble(messageId, payload)
    ByteString(packet.toArray)
  }

  val data = messages.map{ msg =>
    println(msg)
    assemble(msg)
  }

}

object MockConnection {

  final val T0 = 1.seconds

}
