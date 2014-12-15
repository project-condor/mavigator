import sbt._
import sbt.Keys._
import play.twirl.sbt.SbtTwirl
import play.twirl.sbt.Import._

object BuildBuild extends Build {

  lazy val root = (
    Project("root", file("."))
    settings(
      addSbtPlugin("com.typesafe.play" %% "sbt-plugin" % "2.3.4"),
      addSbtPlugin("org.scala-lang.modules.scalajs" % "scalajs-sbt-plugin" % "0.5.5")
    )
    dependsOn(mavlinkPlugin)
  )

  lazy val mavlinkLibrary = (
    Project("mavlink-library", file("mavlink-library"))
    enablePlugins(SbtTwirl)
    settings(
      libraryDependencies += "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.2",
      TwirlKeys.templateImports += "com.github.jodersky.mavlink._"
    )
  )

  lazy val mavlinkPlugin = (
    Project("mavlink-plugin", file("mavlink-plugin"))
    settings(
      sbtPlugin := true
    )
    dependsOn(mavlinkLibrary)
  )

}

