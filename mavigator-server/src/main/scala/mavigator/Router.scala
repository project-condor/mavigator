package mavigator

import akka.actor._
import akka.stream._
import akka.stream.scaladsl._
import akka.http.scaladsl._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server._


class Router(system: ActorSystem) {

  import Directives._

  val route: Route = (
    path("info") {
      get {
        val f: play.twirl.api.Html = mavigator.views.html.index()
        complete(f.body)
      }
    } ~
    path("dashboard" / IntNumber) { id =>
      get {
        //get dashboard for remote sys id
        ???
      }
    } ~
    path("mavlink") {
      get {
        /*extractUpgradeToWebsocket{ upgrade =>
          //upgrade.handleMessagesWith(inSink: Sink[Message, _$3], outSource: Source[Message, _$4])
          ???
         }*/
        handleWebsocketMessages((new MavlinkWebsocket(system)).wsflow)
      }
    } ~
    pathPrefix("assets") {
      get {
        encodeResponse {
          getFromResourceDirectory("assets")
        }
      }
    } ~
    pathEndOrSingleSlash {
      get {
        complete("hello world")
      }
    }
  )

}

import akka.http.scaladsl.model.ws._
import akka.stream.scaladsl._

object SocketService {

  /*
  val out: Source[OutgoingMessage, ActorRef] = Source.actorRef[OutgoingMessage](0, OverflowStrategy.fail)
  val in = Sink.actorRef(ref: ActorRef, onCompleteMessage: Any)
   */


  /*
  Flow[Message, Message, _] { implicit builder =>

    val in: Flow[Message, IncomingMessage, _] = Flow[Message].map {
      case TextMessage.Strict(txt) => IncomingMessage(s"frpm websocket $txt")
      case _ => ???
    }

    val out: Flow[OutgoingMessage, Message, _] = Flow[OutgoingMessage].map {
      case OutgoingMessage(txt) => TextMessage(s"to websocket $text")
    }

    val chatActorSink = Sink.actorRef[ChatEvent](chatRoomActor, UserLeft(user))

  }
 */
}

/*
object EchoService {

  val flow: Flow[Message, Message, _] = Flow[Message].map {
    case TextMessage.Strict(txt) => TextMessage("ECHO: " + txt)
    case _ => TextMessage("Message type unsupported")
  }

}
 */

