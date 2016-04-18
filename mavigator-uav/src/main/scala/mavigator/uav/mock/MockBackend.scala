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
import org.mavlink.messages.Message


/** A test connection that produces random MAVLink messages. */
class MockBackend(
  remoteSystemId: Byte,
  remoteComponentId: Byte,
  prescaler: Double
) {
  import MockBackend._

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

  private val data: Source[ByteString, NotUsed] = messages.map{ message =>
    val (messageId, payload) = Message.pack(message)
    val packet = assembler.assemble(messageId, payload)
    ByteString(packet.toArray)
  }

}

object MockBackend extends Backend {

  final val ClockTick: FiniteDuration = 0.02.seconds

  private def fromPlan(plan: RandomFlightPlan)(messages: Flow[RandomFlightPlan, Message, _]*): Source[Message, NotUsed] = {
    import GraphDSL.Implicits._
    Source.fromGraph(GraphDSL.create() { implicit b =>

      val clock = Source.tick(ClockTick, ClockTick, plan) map { plan =>
        plan.tick(ClockTick.toMillis / 1000.0)
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


  override def init(core: Core) = {
    import core.materializer
    import core.system

    system.log.info("Initializing mock backend...")

    val config = system.settings.config.getConfig("mavigator.uav.mock")

    val mock = new MockBackend(
      config.getInt("remote_system_id").toByte,
      config.getInt("remote_component_id").toByte,
      config.getDouble("prescaler")
    )

    val mockFlow = Flow.fromSinkAndSource(
      Sink.ignore,
      mock.data
    )

    (mockFlow join core.setBackend()).run()

  }

}
