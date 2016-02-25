package mavigator
package uav
package mock

import scala.concurrent.duration._

import akka.NotUsed
import akka.stream._
import akka.stream.Attributes._
import akka.stream.scaladsl._
import akka.util._
import org.mavlink._
import org.mavlink.messages.{Heartbeat, Message}


class MockConnection(
  remoteSystemId: Byte,
  remoteComponentId: Byte,
  prescaler: Double
) {
  import MockConnection._

  private lazy val assembler = new Assembler(remoteSystemId, remoteComponentId)

  private def delayed(delaySeconds: Double)(message: RandomFlightPlan => Message): Flow[RandomFlightPlan, Message, NotUsed] = {
    val dt = delaySeconds / prescaler
    Flow[RandomFlightPlan].withAttributes(inputBuffer(1,1)).delay(dt.seconds).map(message)
  }

  private val messages: Source[Message, NotUsed] = fromPlan(new RandomFlightPlan)(
    delayed(2)(_.heartbeat),
    delayed(0.2)(_.position),
    delayed(0.05)(_.attitude),
    delayed(0.05)(_.motors),
    delayed(0.1)(_.distance)
  )

  val data: Source[ByteString, NotUsed] = messages.map{ message =>
    val (messageId, payload) = Message.pack(message)
    val packet = assembler.assemble(messageId, payload)
    ByteString(packet.toArray)
  }

}

object MockConnection {

  final val ClockTick: FiniteDuration = 0.02.seconds

  private def fromPlan(plan: RandomFlightPlan)(messages: Flow[RandomFlightPlan, Message, _]*): Source[Message, NotUsed] = {
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
