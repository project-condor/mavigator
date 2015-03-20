import sbt._
import sbt.Keys._
import util._
import play._
import play.PlayImport.PlayKeys._
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

import com.github.jodersky.mavlink.sbt._
import com.github.jodersky.mavlink.sbt.MavlinkKeys._

object ApplicationBuild extends Build {

  //settings common to all projects
  val common = Seq(
    scalaVersion := "2.11.6",
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
        "org.webjars" % "bootstrap" % "3.3.1",
        "org.webjars" % "font-awesome" % "4.2.0",
        "org.webjars" % "jquery" % "2.1.3"
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
        "com.typesafe.akka" %% "akka-actor" % "2.3.9",
        "com.github.jodersky" %% "flow" % "2.1.0",
        "com.github.jodersky" % "flow-native" % "2.1.0"
      )
    )
  )

  lazy val dashboard = (
    Project("vfd-dashboard", file("vfd-dashboard"))
    enablePlugins(ScalaJSPlugin)
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