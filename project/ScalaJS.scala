package vfd

import sbt.Keys._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

object ScalaJS {

  val settings = Seq(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.8.0",
      "com.lihaoyi" %%% "scalatags" % "0.5.2",
      "com.lihaoyi" %%% "scalarx" % "0.2.8"
    )
  )

}
