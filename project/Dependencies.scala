package mavigator

import sbt._
import sbt.Keys._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

object Dependencies {

  val AkkaVersion = "2.4.2-RC1"

  val akkaActor = "com.typesafe.akka" %% "akka-actor" % AkkaVersion
  val akkaHttp = "com.typesafe.akka" %% "akka-http" % AkkaVersion
  val akkaHttpCore = "com.typesafe.akka" %% "akka-http-core" % AkkaVersion
  val akkaStreams = "com.typesafe.akka" %% "akka-stream" % AkkaVersion

  val flow = "com.github.jodersky" %% "flow" % "2.4.0"
  val flowNative = "com.github.jodersky" % "flow-native" % "2.4.0" % Runtime

  val jsDom = Def.setting{"org.scala-js" %%% "scalajs-dom" % "0.8.2"}
  val scalatags = Def.setting{"com.lihaoyi" %%% "scalatags" % "0.5.3"}
  val scalarx = Def.setting{"com.lihaoyi" %%% "scalarx" % "0.2.8"}

}
