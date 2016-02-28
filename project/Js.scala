package mavigator

import sbt._
import sbt.Keys._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

object Js {

  def dependsOnJs(proj: Project): Seq[Setting[_]] = Seq(
    resourceGenerators in Compile += Def.task{
      val js: File = (fastOptJS in (proj, Compile)).value.data
      val map = js.getParentFile / (js.name + ".map")

      val out = (resourceManaged in Compile).value / "assets" / "js"

      val toCopy = Seq(
        js -> out / js.name,
        map -> out / map.name
      )
      IO.copy(toCopy).toSeq
    }.taskValue
  )

}
