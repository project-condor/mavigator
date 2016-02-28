package mavigator
package uav

import akka.stream.scaladsl._
import akka.stream._
import org.reactivestreams._

private[uav] class Multiplexer[In, Out](service: Flow[In, Out, _])(implicit materializer: Materializer) {

  private val endpoint: Flow[Out, In, (Publisher[Out], Subscriber[In])] = Flow.fromSinkAndSourceMat(
    Sink.asPublisher[Out](fanout = true),
    Source.asSubscriber[In])((pub, sub) => (pub, sub))

  private lazy val (publisher, subscriber) = (service.joinMat(endpoint)(Keep.right)).run()

  def connect(client: Flow[Out, In, _]) = {
    Source.fromPublisher(publisher).via(client).to(Sink.ignore).run()
  }

}
