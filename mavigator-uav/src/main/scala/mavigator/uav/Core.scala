package mavigator
package uav

import akka.NotUsed
import akka.actor.{ActorLogging, ActorRef, ActorSystem, Props}
import akka.stream.Materializer
import akka.stream.actor.{ActorPublisher, ActorPublisherMessage}
import akka.stream.scaladsl.{ Flow, RunnableGraph, Sink, Source }
import akka.util.ByteString
import org.mavlink.Assembler
import org.mavlink.messages._
import org.reactivestreams.{Publisher, Subscriber}
import scala.concurrent.duration._

/** A core enables dynamic creation and removal of clients and backend connections.
  * It is essentially a dynamic stream multiplexer. */
class Core(implicit val system: ActorSystem, val materializer: Materializer) {
  import Core._

  /** The actor responsible for forwarding messages from the backend
    * to this core. */
  private lazy val backendPublisherActor: ActorRef = system.actorOf(BackendPublisher())

  /** Publisher that forwards messages received from the backend. */
  private lazy val backendPublisher: Publisher[ByteString] = ActorPublisher(backendPublisherActor)

  private lazy val backend = Flow.fromSinkAndSourceMat(
    Sink.ignore, //FIXME: this will need to be changed for controlling the uav from the browser
    Source.fromPublisher(backendPublisher)
  )((toBackend, fromBackend) => (toBackend, fromBackend))

  private lazy val clients = Flow.fromSinkAndSourceMat(
    Sink.asPublisher[ByteString](true),
    Source.asSubscriber[ByteString]
  )((toClient, fromClient) => (toClient, fromClient))

  private lazy val runnable: RunnableGraph[Publisher[ByteString]] = {
    backend.joinMat(clients){case (_, (toClient, _)) =>
      toClient
    }

  }

  private lazy val clientPublisher: Publisher[ByteString] = {
    system.log.info("Core started.")
    runnable.run()
  }

  def setBackend(): Flow[ByteString, ByteString, NotUsed] = {
    val sink = Sink.actorRef(backendPublisherActor, BackendPublisher.BackendComplete)
    val source = Source.empty[ByteString] //FIXME: this will need to be changed for controlling the uav from the browser
    Flow.fromSinkAndSource(sink, source)
  }

  def connect(): Flow[ByteString, ByteString, NotUsed] = {
    Flow.fromSinkAndSource(
      Sink.ignore,
      Source.fromPublisher(clientPublisher)
    )
  }

}

object Util {
  import akka.stream.scaladsl._
  import akka.stream.FlowShape


  def merge[A](preferred: Source[A, _], other: Flow[A, A, _]): Flow[A, A, _] = {
    val graph = GraphDSL.create(preferred, other)((_, _)) { implicit builder =>
      (pref, flow) =>

      import GraphDSL.Implicits._

      val merge = builder.add(MergePreferred[A](1))

      pref.out ~> merge.preferred
      flow.out ~> merge.in(0)

      FlowShape(flow.in, merge.out)
    }
    Flow.fromGraph(graph)
  }

  def assembler: Flow[Message, ByteString, _] = Flow[Message].map{ msg =>
    val as = new Assembler(1, 1)
    val (id, payload) = Message.pack(msg)
    val bytes = as.assemble(id, payload).toArray
    ByteString(bytes)
  }

}


object Core {

  private class BackendPublisher extends ActorPublisher[ByteString] with ActorLogging {
    import akka.stream.actor.ActorPublisherMessage._

    override def preStart() = {
      log.info("Starting backend publisher actor...")
    }

    private var fromBackend: ByteString = null

    def receive = {

      case msg: ByteString =>
        fromBackend = msg
        deliver()

      case BackendPublisher.BackendComplete =>
        sys.error("Backend completed normally, this should not happen.")

      // subscriber requests more
      case ActorPublisherMessage.Request(_) =>
        deliver()

      //subscriber cancels
      case ActorPublisherMessage.Cancel =>
        sys.error("Subscriber cancelled backend stream, this should not happen.")

    }

    def deliver() = if (fromBackend != null && totalDemand > 0 && isActive) {
      onNext(fromBackend)
      fromBackend = null
    }

  }

  private object BackendPublisher {
    case object BackendComplete
    def apply(): Props = Props(classOf[BackendPublisher])
  }

}
