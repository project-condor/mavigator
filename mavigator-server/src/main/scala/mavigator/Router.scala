package mavigator

import akka.actor._
import akka.stream._
import akka.stream.scaladsl._
import akka.http.scaladsl._
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.ws._
import akka.http.scaladsl.server._
import uav.Uav
import akka.util._

import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model.MediaType
import play.twirl.api.{ Xml, Txt, Html }


object Router {

  /** Serialize Twirl `Html` to `text/html`. */
  implicit val twirlHtmlMarshaller = twirlMarshaller[Html](`text/html`)

   /** Serialize Twirl formats to `String`. */
  def twirlMarshaller[A <: AnyRef: Manifest](contentType: MediaType): ToEntityMarshaller[A] =
    Marshaller.StringMarshaller.wrap(contentType)(_.toString)

  import Directives._

  def route(implicit system: ActorSystem): Route = (
    path("info") {
      get {
        val f: Html = mavigator.views.html.dashboard(
          "socket",
          0,
          0,
          0
        )
        complete(f)
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

        val fromWebSocket = Flow[Message].collect{
          case BinaryMessage.Strict(data) => data
        }

        val toWebSocket = Flow[ByteString].map{bytes =>
          //BinaryMessage(bytes)
          TextMessage(bytes.decodeString("UTF-8"))
        }

        val bytes = Uav().connect()

        handleWebSocketMessages(fromWebSocket via bytes via toWebSocket)
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
