package mavigator.dashboard.ui

import mavigator.util.Environment
import mavigator.dashboard.ui.instruments._

import scalatags.JsDom.all._
import rx._

class Hud(attitude: Rx[(Double, Double)])(implicit env: Environment) {

  private def overlay(name: String, z: Int, thinnerThanWide: Boolean = false) = {
    val direction = if (thinnerThanWide) "row" else "column"
    div(
      style:=
        "position: absolute; left: 0; right: 0; top: 0; bottom: 0;" +
        s"display: flex; align-content: center; align-items: stretch; flex-direction: $direction;"+
        s"z-index: $z;"
    )(
      `object`(`type`:="image/svg+xml", "data".attr:=env.asset("images/hud/" + name + ".svg"), style := "flex: 1 1 100%;")
    )
  }

  object Horizon extends SvgInstrument[(Double, Double)] {
    import SvgInstrument._

    val value = attitude

    lazy val element = `object`(`type`:="image/svg+xml", "data".attr:=env.asset("images/hud/horizon.svg"), style:="flex: 1 1 100%;").render
    lazy val horizon = part("horizon")
    lazy val moveable = Seq(horizon)
  
    protected def update(pitchRoll: (Double, Double)) = {
      rotate(horizon, pitchRoll._2)
      //translate(horizon, 0, (pitchRoll._1 * 180 / math.Pi).toInt) // 1deg === 1px
    }
  }

  val element = div(
      style:=
        "position: absolute; left: 0; right: 0; top: 0; bottom: 0;" +
        "display: flex; align-content: stretch; align-items: stretch; flex-direction: row;"+
        "z-index: 0;"
  )(
       Horizon.element
    )
}

