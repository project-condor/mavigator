package mavigator
package cockpit

import org.scalajs.dom.html
import scalatags.JsDom.all._

import util._

/** Provides main cockpit layout. */
trait Layout { self: Page with Instruments =>

  /** Elements to display in the mode control pannel (top panel). */
  def mcp = div(id := "mcp")(
    img(src := asset("images/logo-invert.svg"), style:="height: 20px; margin: 5px;"),
    span(`class`:="mode warning")("Demo System"),
    div(`style` := "float: right")(
      
    )
  )

  /** Element to deisplay on heads-up display (main area). */
  def hud = div(id :="hud")(
    horizonOverlay.element,
    attitudeOverlay.element
  )

  val layoutStyle = """
    #cockpit {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        align-items: stretch;

        background-color: pink;
    }

    #mcp {
        flex: 0 1 0;
        background-color: #222222;
    }
    #hud {
        flex: 1 1 auto;
        position: relative;
        background-color: lightblue;
    }"""

  override def styles = Seq(layoutStyle) ++ instrumentStyles

  override def elements: Seq[html.Element] = Seq(div(id := "cockpit")(
    mcp,
    hud
  ).render)

}
