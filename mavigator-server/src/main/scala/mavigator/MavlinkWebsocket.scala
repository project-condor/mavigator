package mavigator

import akka.actor.Terminated
import akka.actor._
import akka.http.scaladsl._
import akka.http.scaladsl.model.ws.Message
import akka.http.scaladsl.model.ws.TextMessage
import akka.http.scaladsl.server._
import akka.stream.OverflowStrategy
import akka.stream._
import akka.stream.scaladsl.Flow
import akka.stream.scaladsl.GraphDSL
import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.Source
import akka.util.ByteString
import mavigator.uav.Connection
import scala.concurrent.Await
import scala.concurrent.duration.Duration


/**
  * Adapted from https://github.com/jrudolph/akka-http-scala-js-websocket-chat
  */
class MavlinkWebsocket(system: ActorSystem) {

  /*
  GraphDSL.create(Source.actorRef[Connection.Event](1, OverflowStrategy.fail)) {implicit builder =>
    import GraphDSL.Implicits._

    //source: SourceShape[Connection.Event] =>
    source =>

    val inSink = builder.materializedValue.map { client: ActorRef =>
      Sink.actorRef[Connection.Command](
        Mavigator(system).uav,
        Connection.Unregister(client)
      )
    }

    (inSink., source.out)

    //FlowShape(inSink.in, source.out)
    //???
  }
   */

  /** Sink that forwards incomming (from the browser) messages to the uav. */
  private val inSink = Sink.actorRef[Connection.Command](
    Mavigator(system).uav,
    Connection.Send(ByteString("goodbye")) //unregister
  )

  /** Source that emmits messages comming from the uav. */
  private val outSource = Source.actorRef[Connection.Event](
    bufferSize = 1,
    overflowStrategy = OverflowStrategy.fail
  ) mapMaterializedValue { client => // a client is spawned for every outSource materialization
    Mavigator(system).uav.tell(Connection.Register, client)
  }

  private val flow: Flow[Connection.Command, Connection.Event, _] = Flow.fromSinkAndSource(inSink, outSource)

  @deprecated("WIP", "0.0")
  val wsflow = Flow[Message].collect{
    case TextMessage.Strict(msg) => Connection.Send(ByteString(msg))
      // unpack incoming WS text messages...
      // This will lose (ignore) messages not received in one chunk (which is
      // unlikely because chat messages are small) but absolutely possible
      // FIXME: We need to handle TextMessage.Streamed as well.
  }.via(flow).map {
    case msg: Connection.Event =>
      TextMessage.Strict(msg.toString) // ... pack outgoing messages into WS JSON messages ...
  }
 
}

