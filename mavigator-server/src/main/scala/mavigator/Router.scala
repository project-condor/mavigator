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
  import Directives._

  def route(implicit system: ActorSystem): Route = (
    path("info") {
      get {
        val f: Html = mavigator.views.html.dashboard(
          "ws://localhost:8080/mavlink",
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

  /** Serialize Twirl `Html` to `text/html`. */
  implicit val twirlHtmlMarshaller = twirlMarshaller[Html](`text/html`)

   /** Serialize Twirl formats to `String`. */
  def twirlMarshaller[A <: AnyRef: Manifest](contentType: MediaType): ToEntityMarshaller[A] =
    Marshaller.StringMarshaller.wrap(contentType)(_.toString)

}
