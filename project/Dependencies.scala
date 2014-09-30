import sbt._
import scala.scalajs.sbtplugin.ScalaJSPlugin._

object Dependencies {

  val flow = "org.github.jodersky" %% "flow" % "2.0.5"
  val flowNative = "org.github.jodersky" % "flow-native" % "2.0.5"

  val dom = "org.scala-lang.modules.scalajs" %%%! "scalajs-dom" % "0.6"
  val rx = "com.scalarx" %%%! "scalarx" % "0.2.5"

  val bootstrap = "org.webjars" % "bootstrap" % "3.2.0"
  val fontawesome = "org.webjars" % "font-awesome" % "4.2.0"
  val jquery = "org.webjars" % "jquery" % "2.1.1"


  def backend = Seq(bootstrap, fontawesome, jquery)
  def frontend = Seq(dom, rx)
}