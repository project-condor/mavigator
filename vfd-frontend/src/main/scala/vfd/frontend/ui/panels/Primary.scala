package vfd.frontend.ui.panels

import rx.core.Var
import scalatags.JsDom.all.div
import vfd.frontend.util._
import vfd.frontend.ui.Components
import vfd.frontend.util.Environment
import org.mavlink.messages.Message
import org.mavlink.messages.Attitude
import rx.core.Rx
import org.mavlink.messages.Pressure

object Primary {

  def apply(packet: Rx[Message])(implicit env: Environment) = {
    val attitude = packet.only[Attitude]
    val pressure = packet.only[Pressure]
    
    val pitchRoll = Rx{(attitude().pitch.toDouble, attitude().roll.toDouble)}
    val heading = Rx{attitude().heading.toDouble}
    val altitude = Rx{pressure().pressure.toDouble}

    div(
      Components.heading(heading, "33%"),
      Components.attitude(pitchRoll, "33%"),
      Components.altitude(altitude, "33%"))
  }

}