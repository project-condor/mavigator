package com.github.jodersky.sbt
package mavlink

import sbt._
import sbt.Keys._
import java.io.File

object MavlinkKeys {
   
  lazy val mavlinkDialect = settingKey[File]("Dialect definition from which to generate files.")
  lazy val mavlinkTarget = settingKey[File]("Target directory to contain all generated mavlink files.")
  lazy val mavlinkGenerate = taskKey[Seq[File]]("Generate mavlink files.")
  
}