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
    mavlinkDialect := (baseDirectory in ThisBuild).value / "mavlink" / "concise.xml"
  )

  lazy val root = Project("root", file(".")).aggregate(
    main,
    uav,
    dashboard
  )

  lazy val main = (
    Project("vfd-main", file("vfd-main"))
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
    dependsOnJs(dashboard)
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

  lazy val dashboard = (
    Project("vfd-dashboard", file("vfd-dashboard"))
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