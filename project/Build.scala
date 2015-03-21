import sbt._
import sbt.Keys._
import sbt.Project.projectToRef

import play._
import play.PlayImport.PlayKeys._

import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

import com.github.jodersky.mavlink.sbt._
import com.github.jodersky.mavlink.sbt.MavlinkKeys._

import playscalajs.ScalaJSPlay
import playscalajs.PlayScalaJS.autoImport._
import com.typesafe.sbt.web.Import._


object ApplicationBuild extends Build {

  //settings common to all projects
  val common = Seq(
    scalaVersion := "2.11.6",
    scalacOptions ++= Seq("-feature", "-deprecation"),
    mavlinkDialect := (baseDirectory in ThisBuild).value / "mavlink" / "concise.xml"
  )

  //root super-project
  lazy val root = (
    Project("root", file(".")).aggregate(
      main,
      uav,
      dashboard
    )
    settings(
      //goto main project on load
      onLoad in Global := (Command.process("project vfd-main", _: State)) compose (onLoad in Global).value
    )
  )

  //main play project
  lazy val main = (
    Project("vfd-main", file("vfd-main"))
    enablePlugins(PlayScala)
    enablePlugins(SbtMavlink)
    settings(common: _*)
    settings(
      resolvers += Resolver.url("scala-js-releases", url("http://dl.bintray.com/content/scala-js/scala-js-releases"))(Resolver.ivyStylePatterns),
      scalaJSProjects := Seq(dashboard),
      pipelineStages := Seq(scalaJSProd),
      libraryDependencies ++= Seq(
        "org.webjars" % "bootstrap" % "3.3.1",
        "org.webjars" % "font-awesome" % "4.2.0",
        "org.webjars" % "jquery" % "2.1.3"
      )
    )
    dependsOn(uav)
    aggregate(projectToRef(dashboard))
  )

  //communication backend
  lazy val uav = (
    Project("vfd-uav", file("vfd-uav"))
    enablePlugins(SbtMavlink)
    settings(common: _*)
    settings(
      libraryDependencies ++= Seq(
        "com.typesafe.akka" %% "akka-actor" % "2.3.9",
        "com.github.jodersky" %% "flow" % "2.1.0",
        "com.github.jodersky" % "flow-native" % "2.1.0"
      )
    )
  )

  //web frontend
  lazy val dashboard = (
    Project("vfd-dashboard", file("vfd-dashboard"))
    enablePlugins(ScalaJSPlugin)
    enablePlugins(ScalaJSPlay)
    enablePlugins(SbtMavlink)
    settings(common: _*)
    settings(
      libraryDependencies ++= Seq(
        "org.scala-js" %%% "scalajs-dom" % "0.8.0",
        "com.lihaoyi" %%% "scalatags" % "0.4.6",
        "com.lihaoyi" %%% "scalarx" % "0.2.8"
      )
    )
  )

}