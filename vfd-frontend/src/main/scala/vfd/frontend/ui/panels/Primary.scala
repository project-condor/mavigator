package vfd.frontend.ui.panels

import org.mavlink.messages._
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
        case Attitude(roll, pitch, yaw) =>
          pitchRoll() = (roll, pitch)
          heading() = yaw
        case _ => ()
      }
    }

    div(
      Components.compass(heading, "33%"),
      Components.horizon(pitchRoll, "33%"),
      Components.altimeter(altitude, "33%"))
  }

}