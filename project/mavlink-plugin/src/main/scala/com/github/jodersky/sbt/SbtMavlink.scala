package com.github.jodersky.sbt

import sbt._
import Keys._
import plugins._
import mavlink.MavlinkKeys._
import sbt.Project.Initialize
import com.github.jodersky.mavlink.Main

object SbtMavlink extends AutoPlugin {

  override def requires = JvmPlugin

  lazy val generate = Def.task[Seq[File]] {
    streams.value.log.info("Generating mavlink files...")
    Main.run((mavlinkDialect in Compile).value, (mavlinkTarget in Compile).value).map(_.getAbsoluteFile)
  }

  override val projectSettings: Seq[Setting[_]] = Seq(
    mavlinkTarget in Compile := (sourceManaged in Compile).value,
    mavlinkGenerate in Compile := generate.value,
    sourceGenerators in Compile += (mavlinkGenerate in Compile).taskValue
  )
}
