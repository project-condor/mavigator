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

import sbtunidoc.Plugin._

import com.typesafe.sbt.SbtSite._
import com.typesafe.sbt.SbtGhPages._
import com.typesafe.sbt.SbtGit._

object ApplicationBuild extends Build {

  // settings common to all projects
  val commonSettings = Seq(
    scalaVersion := "2.11.6",
    scalacOptions ++= Seq("-feature", "-deprecation")
  )

  // root super-project
  lazy val root = (
    Project("root", file("."))
    settings(commonSettings: _*)
    settings(unidocSettings: _*)
    settings(site.settings: _*)
    settings(ghpages.settings: _*)
    settings(
      //goto main project on load
      onLoad in Global := (Command.process("project vfd-main", _: State)) compose (onLoad in Global).value,
      site.addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), "latest/api"),
      git.remoteRepo := "git@github.com:jodersky/vfd.git"
    )
    aggregate(
      bindings,
      main,
      uav,
      dashboard,
      index
    )
  )

  // empty project that uses SbtMavlink to generate protocol bindings
  lazy val bindings = (
    Project("vfd-bindings", file("vfd-bindings"))
    enablePlugins(SbtMavlink)
    enablePlugins(ScalaJSPlugin)
    settings(commonSettings: _*)
    settings(
      mavlinkDialect := baseDirectory.value / "mavlink" / "common.xml"
    )
  )

  // main play project
  lazy val main = (
    Project("vfd-main", file("vfd-main"))
    enablePlugins(PlayScala)
    dependsOn(bindings)
    dependsOn(uav)
    settings(commonSettings: _*)
    settings(
      scalaJSProjects := Seq(dashboard, index),
      pipelineStages := Seq(scalaJSProd),
      libraryDependencies ++= Seq(
        "org.webjars" % "bootstrap" % "3.3.1",
        "org.webjars" % "font-awesome" % "4.2.0",
        "org.webjars" % "jquery" % "2.1.3"
      )
    )
    aggregate(
      projectToRef(dashboard),
      projectToRef(index)
    )
  )

  // communication backend
  lazy val uav = (
    Project("vfd-uav", file("vfd-uav"))
    dependsOn(bindings)
    settings(commonSettings: _*)
    settings(
      libraryDependencies ++= Seq(
        "com.typesafe.akka" %% "akka-actor" % "2.3.10",
        "com.github.jodersky" %% "flow" % "2.1.1",
        "com.github.jodersky" % "flow-native" % "2.1.1"
      )
    )
  )

  // web frontends
  val scalajsSettings = Seq(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.8.0",
      "com.lihaoyi" %%% "scalatags" % "0.5.0",
      "com.lihaoyi" %%% "scalarx" % "0.2.8"
    )
  )

  // main cockpit front-end
  lazy val dashboard = (
    Project("vfd-dashboard", file("vfd-dashboard"))
    enablePlugins(ScalaJSPlugin)
    enablePlugins(ScalaJSPlay)
    dependsOn(bindings)
    settings(commonSettings: _*)
    settings(scalajsSettings: _*)
  )

  // landing page providing selection of drone
  lazy val index = (
    Project("vfd-index", file("vfd-index"))
    enablePlugins(ScalaJSPlugin)
    enablePlugins(ScalaJSPlay)
    dependsOn(bindings)
    settings(commonSettings: _*)
    settings(scalajsSettings: _*)
  )

}
