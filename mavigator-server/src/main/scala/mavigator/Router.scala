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

  val socketUrl = "ws://localhost:8080/mavlink"

  def route(implicit system: ActorSystem): Route = (
    path("dashboard" / IntNumber) { id =>
      get {
        val html = mavigator.views.html.app(
          "Mavigator",
          "mavigator_dashboard_Main",
          Map(
            "socketUrl" -> socketUrl,
	    "remoteSystemId" -> "0",
            "systemId" -> "0",
	    "componentId" -> "0"
          )
        )
        complete(html)
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
        val html = mavigator.views.html.app(
          "Index",
          "mavigator_index_Main",
          Map(
            "socketUrl" -> socketUrl
          )
        )
        complete(html)
      }
    }
  )

  implicit val twirlHtml : ToEntityMarshaller[Html] = Marshaller.StringMarshaller.wrap(`text/html`){(h: Html) =>
    h.toString
  }
  
}
