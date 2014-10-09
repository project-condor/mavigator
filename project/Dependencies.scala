import sbt._
import scala.scalajs.sbtplugin.ScalaJSPlugin._

object Dependencies {

  val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.3.6"

  val flow = "com.github.jodersky" %% "flow" % "2.0.6"
  val flowNative = "com.github.jodersky" % "flow-native" % "2.0.6"

  val bootstrap = "org.webjars" % "bootstrap" % "3.2.0"
  val fontawesome = "org.webjars" % "font-awesome" % "4.2.0"
  val jquery = "org.webjars" % "jquery" % "2.1.1"

  val dom = "org.scala-lang.modules.scalajs" %%%! "scalajs-dom" % "0.6"
  val tag = "com.scalatags" %%%! "scalatags" % "0.4.1"
  val rx = "com.scalarx" %%%! "scalarx" % "0.2.6"
  val pickling = "org.scalajs" %%%! "scalajs-pickling" % "0.3.1"
  val picklingPlay = "org.scalajs" %% "scalajs-pickling-play-json" % "0.3.1"


}