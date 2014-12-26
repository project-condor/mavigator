package vfd.frontend

import scala.scalajs.js
import scala.scalajs.js.Any.fromFunction1

import org.mavlink.Packet
import org.mavlink.Parser
import org.mavlink.messages.Message
import org.scalajs.dom

import rx.core.Rx
import rx.core.Var
import rx.ops.RxOps

class MavlinkSocket(url: String, remoteSystemId: Int) {

  lazy val packet: Var[Packet] = Var(Packet.Empty)
  lazy val message: Rx[Message] = packet.map{p => 
    Message.unpack(p.messageId, p.payload)
  }

  object stats {
    val crcErrors = Var(0)
    val overflows = Var(0)
    val wrongIds = Var(0)
    val packets = Var(0)
    val open = Var(false)
  }

  private val parser = new Parser(
    pckt => {
      pckt match {
        case Packet(seq, `remoteSystemId`, compId, msgId, payload) =>
          packet() = pckt
          stats.packets() += 1
        case _ =>
          stats.wrongIds() += 1
      }
    },
    err => {
      err match {
        case Parser.ParseErrors.CrcError => stats.crcErrors() += 1
        case Parser.ParseErrors.OverflowError => stats.overflows() += 1
      }
    })

  private val connection = new dom.WebSocket(url)

  connection.binaryType = "arraybuffer";
  connection.onopen = (e: dom.Event) => {
    stats.open() = true
  }
  connection.onmessage = (e: dom.MessageEvent) => {
    val buffer = e.data.asInstanceOf[js.typedarray.ArrayBuffer]
    val view = new js.typedarray.DataView(buffer)

    for (i <- 0 until view.byteLength) {
      parser.push(view.getInt8(i))
    }
  }
  connection.onclose = (e: dom.CloseEvent) => {
    stats.open() = false
  }

}