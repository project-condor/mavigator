package mavigator.dashboard

import scala.scalajs.js
import scala.scalajs.js.Any.fromFunction1
import org.mavlink.Packet
import org.mavlink.Parser
import org.mavlink.Parser.Errors._
import org.mavlink.messages.Message
import org.scalajs.dom
import scala.concurrent.duration._
import rx._
import rx.ops._
import scala.concurrent.ExecutionContext.Implicits.global

class MavlinkSocket(url: String, val remoteSystemId: Int) {
  implicit val scheduler = new DomScheduler

  lazy val packet: Var[Packet] = Var(Packet.empty)
  lazy val message: Rx[Message] = packet.map{p => 
    Message.unpack(p.messageId, p.payload)
  }
  
  object stats {
    private val DebounceTime = 1.seconds
    
    private[MavlinkSocket] val _crcErrors = Var(0)
    private[MavlinkSocket] val _overflows = Var(0)
    private[MavlinkSocket] val _wrongIds = Var(0)
    private[MavlinkSocket] val _packets = Var(0)
    
    val crcErrors = _crcErrors.debounce(DebounceTime)
    val overflows = _overflows.debounce(DebounceTime)
    val wrongIds = _wrongIds.debounce(DebounceTime)
    val packets = _packets.debounce(DebounceTime)
    val open = Var(false)
  }

 private val parser = new Parser(
    {
      case pckt@Packet(seq, `remoteSystemId`, compId, msgId, payload) =>
        packet() = pckt
        stats._packets() += 1
      case _ =>
        stats._wrongIds() += 1
    },
    {
      case CrcError => stats._crcErrors() += 1
      case OverflowError => stats._overflows() += 1
    })

  private val connection = new dom.WebSocket(url)

  connection.binaryType = "arraybuffer"

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
