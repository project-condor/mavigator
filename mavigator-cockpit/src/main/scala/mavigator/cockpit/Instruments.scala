package mavigator
package cockpit

import org.scalajs.dom
import org.scalajs.dom.html

import scalatags.JsDom.all._

import util.Page

trait Instruments { page: Page =>

  trait Instrument[A] {
    def element: html.Element
    def update(newValue: A): Unit
  }

  /** Common behaviour for svg-based instruments. */
  abstract class SvgInstrument[A](
    path: String
  ) extends Instrument[A] {

    override def element: html.Element = objectElement

    /** SVG object element that contains the rendered instrument */
    lazy val objectElement: html.Object = SvgInstrument.svgObject(path)

    /** Retrieves an element of the underlying SVG document by ID. */
    protected def part(id: String): html.Element =
      objectElement.contentDocument.getElementById(id).asInstanceOf[html.Element]

    /** Movable parts of the instrument */
    protected def moveable: Seq[html.Element]

    /** Called when element has been loaded. */
    private def load(event: dom.Event): Unit = {
      for (part <- moveable) {
        part.style.transition = "transform 20ms ease-out"
      }
    }

    element.addEventListener("load", (e: dom.Event) => load(e))
  }

  /** Contains helpers for SVG instruments. */
  object SvgInstrument {

    /** Retrieves an SVG object element by its instrument's name. */
    def svgObject(path: String): html.Object = {
      val fullPath = page.asset("images/" + path)
      `object`(`type` := "image/svg+xml", "data".attr := fullPath, width := 100.pct)(
        "Error loading instrument " + fullPath).render
    }

    /** Applies translation styling to an element. */
    def translate(elem: html.Element, x: Int, y: Int): Unit = {
      elem.style.transform = "translate(" + x + "px, " + y + "px)";
    }

    /** Applies rotation styling to an element. */
    def rotate(elem: html.Element, rad: Double): Unit = {
      elem.style.transform = "rotateZ(" + rad + "rad)";
    }

  }

  lazy val attitudeOverlay = new SvgInstrument[(Float, Float)]("hud/attitude.svg") {
    override def element = div(`class`:="hud-overlay")(objectElement).render
    private lazy val pitchPart = part("pitch")
    private lazy val rollPart = part("roll")
    override lazy val moveable = Seq(pitchPart, rollPart)

    override def update(pitchRoll: (Float, Float)) = {
      import SvgInstrument._
      val (pitch, roll) = pitchRoll
      translate(pitchPart, 0, (pitch * 180 / math.Pi * 10).toInt) // 1deg === 10px
      rotate(rollPart, roll)
    }
  }

  lazy val horizonOverlay = new SvgInstrument[(Float, Float)]("hud/horizon.svg") {
    import SvgInstrument._

    override def element = div(`class`:="hud-overlay")(objectElement).render
    lazy val horizon = part("horizon")
    lazy override val moveable = Seq(horizon)

    override def update(pitchRoll: (Float, Float)) = {
      val (pitch, roll) = pitchRoll

      val t = (pitch * 180 / math.Pi * 10).toInt // 1deg === 10px

      horizon.style.transform = s"rotateZ(${roll}rad)translate(0px, ${t}px) "
    }
  }


  val overlayStyle = """
    |.hud-overlay {
    |    position: absolute;
    |    left: 0;
    |    right: 0;
    |    top: 0;
    |    bottom: 0;
    |
    |    display: flex;
    |    flex-direction: row;
    |    justify-content: center;
    |    align-items: center;
    |}
    |.hud-overlay > * {
    |    flex: 1 1 0;
    |    width: 100%;
    |    height: 100%;
    |    max-width: 100%;
    |    max-height: 100%;
    |}""".stripMargin


  def mode(name: String, kind: String, on: Boolean = false) = {
    div(`class` := s"mode $kind ${if (!on) "off"}")(name)
  }

    //TODO make these into real instruments and lazy vals
  def modes = div(style := "float: right;")(
    mode("LINK", "danger", true),
    mode("BAT", "warning", true),
    mode("GPS", "warning", true),
    mode("STABILIZED", "info", true)
  )

  val modeStyle = """
.mode {
    display: inline-block;
    box-sizing: border-box;
    text-decoration: normal;
    margin-right: 5px;
    padding: 5px;
}

.mode.danger {
    color: #d9534f;
    text-shadow: 0 0 5px #d9534f;
    animation: danger-blink 0.5s linear infinite;
    -webkit-animation: danger-blink 0.5s linear infinite;
}

.mode.warning {
    color: #f0ad4e;
    text-shadow: 0 0 5px #f0ad4e;
}

.mode.info {
    color: #5bc0de;
    text-shadow: 0 0 5px #5bc0de;
}

.mode.success {
    color: #5cb85c;
    text-shadow: 0 0 5px #5cb85c;
}

.mode.off {
    color: #e6e6e6;
    text-shadow: none;
    animation: none;
    -webkit-animation: none;
}
"""

  def instrumentStyles: Seq[String] = Seq(overlayStyle, modeStyle)

}
