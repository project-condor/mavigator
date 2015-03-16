import sbt._
import sbt.Keys._
import play._
import play.PlayImport.PlayKeys._
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import com.typesafe.sbt.packager.universal.UniversalKeys

package object util extends UniversalKeys {

  implicit class ScalaJSPlayProject(val scalajvm: Project) {

    val jsOutputDir = settingKey[File]("Directory for javascript files")

    def dependsOnJs(scalajs: Project): Project = scalajvm.settings(
      jsOutputDir := (classDirectory in Compile).value / "public" / "lib",
      compile in Compile <<= (compile in Compile) dependsOn (fastOptJS in (scalajs, Compile)),
      dist <<= dist dependsOn (fullOptJS in (scalajs, Compile)),
      stage <<= stage dependsOn (fullOptJS in (scalajs, Compile)),
      playMonitoredFiles += (scalaSource in (scalajs, Compile)).value.getCanonicalPath
    ).settings(
      Seq(packageScalaJSLauncher, fastOptJS, fullOptJS) map { packageJSKey =>
        crossTarget in (scalajs, Compile, packageJSKey) := jsOutputDir.value
      }: _*
    )

  }

}