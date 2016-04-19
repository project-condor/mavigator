package mavigator
package cockpit

import org.mavlink._
import org.mavlink.Parser.Errors._
import org.mavlink.messages._

import org.scalajs.dom
import scalajs.js

trait MavlinkWebSockets { self: Instruments =>

  private class MavlinkWebSocket(url: String, remoteSystemId: Int) {

    private var _open = false
    def open: Boolean = _open

    private val parser = new Parser(
      {
        case pckt@Packet(seq, `remoteSystemId`, compId, msgId, payload) =>
          val msg = Message.unpack(pckt.messageId, pckt.payload)
          onMessage(msg)
            //_packets() = _packets.now + 1
        case _ =>
          //_wrongIds() = _wrongIds.now + 1
      },
      {
        case CrcError => //_crcErrors() = _crcErrors.now + 1
        case OverflowError => //_overflows() = _overflows.now + 1
      }
    )

    private val connection = new dom.WebSocket(url)

    connection.binaryType = "arraybuffer"

    connection.onopen = (e: dom.Event) => {
      _open = true
    }
    connection.onmessage = (e: dom.MessageEvent) => {
      val buffer = e.data.asInstanceOf[js.typedarray.ArrayBuffer]
      val view = new js.typedarray.DataView(buffer)

      for (i <- 0 until view.byteLength) {
        parser.push(view.getInt8(i))
      }
    }
    connection.onclose = (e: dom.CloseEvent) => {
      _open = false
    }

  }

  private def onMessage(msg: Message): Unit = msg match {
    case a: Attitude =>
      attitudeOverlay.update((a.pitch, a.roll))
      horizonOverlay.update((a.pitch, a.roll))
    case Stability(v) => {
      unstable.update(v != 0)
      println("stability " + v)
    }


    case _ => ()
  }

  def connect(url: String, remoteSystemId: Int): Unit = new MavlinkWebSocket(url, remoteSystemId)
}
