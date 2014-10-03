import sbt._
import sbt.Keys._
import play._
import play.PlayImport.PlayKeys._
import scala.scalajs.sbtplugin.ScalaJSPlugin
import scala.scalajs.sbtplugin.ScalaJSPlugin.ScalaJSKeys._

package object util {

  implicit class ScalaJSPlayProject(val project: Project) {
    def dependsOnJs(reference: Project): Project = project.settings(
      resourceGenerators in Compile += Def.task{
        val outDir: File = (resourceManaged in Compile).value / "public" / "lib"

        val optimized: Seq[File] = (fastOptJS in (reference, Compile)).value.allCode.map(_.path).map(file(_))

        val outFiles = optimized.map(file => outDir / file.name)

        for ((opt, out) <- optimized zip outFiles) {
          if (!out.exists || out.lastModified < opt.lastModified) {
            IO.copyFile(opt, out, true)
            val map = opt.getParentFile / (out.name + ".map")
            IO.copyFile(map, outDir / map.name)
          }
        }
        outFiles
      }.taskValue,
      playMonitoredFiles ++= (watchSources in reference).value.map(_.getCanonicalPath)
    )
  }

}