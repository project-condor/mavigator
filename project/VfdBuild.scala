package vfd

import sbt._
import sbt.Keys._
import sbt.Project.projectToRef

object VfdBuild extends Build {

  // settings common to all projects
  val defaultSettings = Seq(
    scalaVersion := "2.11.7",
    scalacOptions ++= Seq("-feature", "-deprecation")
  )

  // root super-project
  lazy val root = Project(
    id = "root",
    base = file("."),
    aggregate = Seq(bindings, main, uav, dashboard, index)
  )

  // empty project that uses SbtMavlink to generate protocol bindings
  lazy val bindings = Project(
    id = "vfd-bindings",
    base = file("vfd-bindings")
  )

  // main play project
  lazy val main = Project(
    id = "vfd-main",
    base = file("vfd-main"),
    dependencies = Seq(bindings, uav),
    aggregate = Seq(
      projectToRef(dashboard),
      projectToRef(index)
    )
  )

  // communication backend
  lazy val uav = Project(
    id = "vfd-uav",
    base = file("vfd-uav"),
    dependencies = Seq(bindings)
  )

  // main cockpit front-end
  lazy val dashboard = Project(
    id = "vfd-dashboard",
    base = file("vfd-dashboard"),
    dependencies = Seq(bindings)
  )

  // landing page providing selection of drone
  lazy val index = Project(
    id = "vfd-index",
    base = file("vfd-index"),
    dependencies = Seq(bindings)
  )

}
