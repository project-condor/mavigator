import sbt._
import sbt.Keys._
import util._
import play._
import play.PlayImport.PlayKeys._
import scala.scalajs.sbtplugin.ScalaJSPlugin
import scala.scalajs.sbtplugin.ScalaJSPlugin.ScalaJSKeys._
import Dependencies._

import com.github.jodersky.sbt.mavlink.MavlinkKeys._
import com.github.jodersky.sbt.SbtMavlink

object ApplicationBuild extends Build {

  //settings common to all projects
  val common = Seq(
    scalaVersion := "2.11.4",
    scalacOptions ++= Seq("-feature", "-deprecation"),
    mavlinkDialect := (baseDirectory in ThisBuild).value / "conf" / "concise.xml"
  )

  lazy val root = Project("root", file(".")).aggregate(
    uav,
    backend,
    frontend
  )

  lazy val uav = (
    Project("vfd-uav", file("vfd-uav"))
    enablePlugins(SbtMavlink)
    settings(common: _*)
    settings(
      libraryDependencies ++= Seq(
        akkaActor,
        flow,
        flowNative
      )
    )
  )

  lazy val backend = (
    Project("vfd-backend", file("vfd-backend"))
    enablePlugins(PlayScala)
    enablePlugins(SbtMavlink)
    settings(common: _*)
    settings(
      resolvers += Resolver.url("scala-js-releases", url("http://dl.bintray.com/content/scala-js/scala-js-releases"))(Resolver.ivyStylePatterns),
      libraryDependencies ++= Seq(
        bootstrap,
        fontawesome,
        jquery
      )
    )
    dependsOn(uav)
    dependsOnJs(frontend)
  )

  lazy val frontend = (
    Project("vfd-frontend", file("vfd-frontend"))
    settings(ScalaJSPlugin.scalaJSSettings: _*)
    enablePlugins(SbtMavlink)
    settings(common: _*)
    settings(
      libraryDependencies ++= Seq(
        rx,
        dom,
        tag
      )
    )
  )

}