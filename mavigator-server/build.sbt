import mavigator.{Dependencies, MavigatorBuild, Js}

enablePlugins(SbtTwirl)

MavigatorBuild.defaultSettings

libraryDependencies ++= Seq(
  Dependencies.akkaHttp,
  Dependencies.akkaHttpCore,
  Dependencies.akkaStream,
  Dependencies.flowNative //FIXME runtime dependencies from uav are not included, is this an sbt bug?
)

Js.dependsOnJs(MavigatorBuild.cockpit)

fork in run := true
connectInput in run := true
cancelable in Global := true


/*
 * Deployment configuration
 */
enablePlugins(UniversalPlugin, DebianPlugin, DockerPlugin)
enablePlugins(JavaServerAppPackaging)


name in Universal := "mavigator"
packageName in Universal := "mavigator"
executableScriptName in Universal := "mavigator"

name in Linux := (name in Universal).value
packageName in Linux := (packageName in Universal).value
executableScriptName in Linux := (executableScriptName in Universal).value

maintainer in Linux := "Jakob Odersky <jakob@odersky.com>"
packageSummary in Linux := "Virtual cockpit for drones."
packageDescription in Linux := "Compatible with devices using the MAVLink protocol."

version in Debian := version.value
debianPackageDependencies in Debian ++= Seq(
  "java8-runtime-headless",
  "bash (>= 2.05a-11)"
)

import com.typesafe.sbt.packager.archetypes.ServerLoader
serverLoading in Debian := ServerLoader.Systemd


name in Docker := "mavigator"
packageName in Docker := "mavigator"
executableScriptName := "mavigator"
maintainer in Docker := "Jakob Odersky <jakob@odersky.com>"

dockerBaseImage := "java:8"
dockerExposedPorts += 8080
