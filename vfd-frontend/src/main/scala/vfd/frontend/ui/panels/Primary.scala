package vfd.frontend.ui.panels

import org.mavlink.messages.Attitude
import org.mavlink.messages.Message
import org.mavlink.messages.Pressure

import rx.core.Obs
import rx.core.Rx
import rx.core.Var
import scalatags.JsDom.all.div
import vfd.frontend.ui.Components
import vfd.frontend.util.Environment

object Primary {

  def apply(message: Rx[Message])(implicit env: Environment) = {
    val pitchRoll = Var((0.0, 0.0)) // Rx{(attitude().pitch.toDouble, attitude().roll.toDouble)}
    val heading = Var(0.0) //Rx{attitude().heading.toDouble}
    val altitude = Var(0.0) //Rx{pressure().pressure.toDouble}

    Obs(message) {
      message() match {
        case Attitude(time, roll, pitch, h) =>
          pitchRoll() = (pitch, roll)
          heading() = h
        case Pressure(time, p, _, t) => altitude() = p
        case _ => ()
      }
    }

    div(
      Components.heading(heading, "33%"),
      Components.attitude(pitchRoll, "33%"),
      Components.altitude(altitude, "33%"))
  }

}