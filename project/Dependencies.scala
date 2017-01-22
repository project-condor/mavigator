package mavigator

import sbt._
import sbt.Keys._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

object Dependencies {

  final val AkkaVersion = "2.4.16"
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % AkkaVersion
  val akkaStream = "com.typesafe.akka" %% "akka-stream" % AkkaVersion

  final val AkkaHttpVersion = "10.0.1"
  val akkaHttp = "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
  val akkaHttpCore = "com.typesafe.akka" %% "akka-http-core" % AkkaHttpVersion

  final val AkkaSerialVersion = "4.0.0-RC1"
  val akkaSerial = "ch.jodersky" %% "akka-serial-core" % AkkaSerialVersion
  val akkaSerialNative = "ch.jodersky" % "akka-serial-native" % AkkaSerialVersion % Runtime
  val akkaSerialStream = "ch.jodersky" %% "akka-serial-stream" % AkkaSerialVersion

  val reactiveStreams = "org.reactivestreams" % "reactive-streams" % "1.0.0"

  val jsDom = Def.setting{"org.scala-js" %%% "scalajs-dom" % "0.9.1"}
  val scalatags = Def.setting{"com.lihaoyi" %%% "scalatags" % "0.6.2"}
  val scalarx = Def.setting{"com.lihaoyi" %%% "scalarx" % "0.3.2"}

}
