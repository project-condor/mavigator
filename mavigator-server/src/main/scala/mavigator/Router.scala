package mavigator

import akka.actor._
import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.Uri.Path
import akka.http.scaladsl.model.ws._
import akka.http.scaladsl.server._
import akka.util._
import akka.stream.scaladsl._
import play.twirl.api.Html
import uav.Uav


object Router {
  import Directives._

  final val SocketEndpoint = "mavlink"

  def withSocketUri: Directive1[Uri] = extractUri.map { uri =>
    uri.withScheme("ws").withPath(Path.Empty / SocketEndpoint)
  }

  def route(implicit system: ActorSystem): Route = (
    path("whoami") {
      get {
        withSocketUri { sock =>
          complete(sock.toString)
        }
      }
    } ~
      path("cockpit" / IntNumber) { id =>
        get {
          withSocketUri { socket =>
            val html = mavigator.views.html.app(
              "Mavigator",
              "mavigator_cockpit_Main",
              Map(
                "socketUrl" -> socket.toString,
	        "remoteSystemId" -> id.toString
              )
            )
            complete(html)
          }
        }
      } ~
      path(SocketEndpoint) {
        get {
          val fromWebSocket = Flow[Message].collect{
            case BinaryMessage.Strict(data) => data
          }

          val toWebSocket = Flow[ByteString].map{bytes =>
            BinaryMessage(bytes)
          }

          val backend = Uav().connect()

          handleWebSocketMessages(fromWebSocket via backend via toWebSocket)
        }
      } ~
      pathEndOrSingleSlash {
        get {
          withSocketUri { socket =>
            val html = mavigator.views.html.app(
              "Index",
              "mavigator_index_Main",
              Map(
                "socketUrl" -> socket.toString
              )
            )
            complete(html)
          }
        }
      } ~
      pathPrefix("assets") {
        get {
          encodeResponse {
            getFromResourceDirectory("assets")
          }
        }
      }
  )

  /** Enables completing requests with html. */
  implicit val twirlHtml : ToEntityMarshaller[Html] =
    Marshaller.StringMarshaller.wrap(`text/html`){ h: Html =>
      h.toString
    }

}
