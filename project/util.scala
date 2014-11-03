import sbt._
import Keys._
import play.Play._
import scala.scalajs.sbtplugin.ScalaJSPlugin._
import ScalaJSKeys._
import com.typesafe.sbt.packager.universal.UniversalKeys

package object util extends UniversalKeys {

  val scalajsOutputDir = Def.settingKey[File]("directory for javascript files output by scalajs")


  implicit class ScalaJSPlayProject(val project: Project) {

    private def copySourceMapsTask(scalajs: Project) = Def.task {
      val scalaFiles = (Seq(scalajs.base) ** ("*.scala")).get
      for (scalaFile <- scalaFiles) {
        val target = new File((classDirectory in Compile).value, scalaFile.getPath)
        IO.copyFile(scalaFile, target)
      }
    }

    def dependsOnJs(scalajs: Project): Project = project.settings(
      scalajsOutputDir := (classDirectory in Compile).value / "public" / "lib",
      compile in Compile <<= (compile in Compile) dependsOn (fastOptJS in (scalajs, Compile)) dependsOn copySourceMapsTask(scalajs),
      dist <<= dist dependsOn (fullOptJS in (scalajs, Compile)),
      stage <<= stage dependsOn (fullOptJS in (scalajs, Compile))
    ).settings(
      // ask scalajs project to put its outputs in scalajsOutputDir
      Seq(packageLauncher, fastOptJS, fullOptJS) map { packageJSKey =>
        crossTarget in (scalajs, Compile, packageJSKey) := scalajsOutputDir.value
      } : _*
    )

  }

}