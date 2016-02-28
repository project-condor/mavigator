package mavigator

import sbt._
import sbt.Keys._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

object Dependencies {

  final val AkkaVersion = "2.4.2-RC1"
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % AkkaVersion
  val akkaHttp = "com.typesafe.akka" %% "akka-http-experimental" % AkkaVersion
  val akkaHttpCore = "com.typesafe.akka" %% "akka-http-core" % AkkaVersion
  val akkaStream = "com.typesafe.akka" %% "akka-stream" % AkkaVersion

  val reactiveStreams = "org.reactivestreams" % "reactive-streams" % "1.0.0"

  final val FlowVersion = "2.5.0-M2"
  val flow = "com.github.jodersky" %% "flow-core" % FlowVersion
  val flowNative = "com.github.jodersky" % "flow-native" % FlowVersion % Runtime
  val flowStream = "com.github.jodersky" % "flow-stream" % FlowVersion

  val jsDom = Def.setting{"org.scala-js" %%% "scalajs-dom" % "0.9.0"}
  val scalatags = Def.setting{"com.lihaoyi" %%% "scalatags" % "0.5.4"}
  val scalarx = Def.setting{"com.lihaoyi" %%% "scalarx" % "0.3.1"}

}
